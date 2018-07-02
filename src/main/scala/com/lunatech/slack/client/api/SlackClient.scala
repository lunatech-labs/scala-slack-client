package com.lunatech.slack.client.api

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.lunatech.slack.client.models.{ChatMessage, MessageResponse, _}
import com.lunatech.slack.client.services.SlackCaller
import play.api.libs.json._
import play.api.libs.ws.StandaloneWSResponse

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}


class SlackClient(token: String, config: SlackClientConfig, slackCaller: SlackCaller)(implicit system: ActorSystem, materializer: Materializer) {

  /**
    * https://slack.com/api/chat.getPermalink
    */
  def getPermalinkMessage(channel: String, messageTs: String)(implicit ec: ExecutionContext): Future[PermaLink] = {
    val params: Map[String, String] = Map("channel" -> channel, "message_ts" -> messageTs)

    slackCaller.makeGetApiCall(token, config.getPermalinkMessageUrl, params)
      .flatMap(response => SlackClient.jsonToClass[PermaLink](response.body))
  }


  /**
    * https://api.slack.com/methods/chat.postMessage
    */
  def postMessage(message: ChatMessage)(implicit ec: ExecutionContext): Future[MessageResponse] = {
    slackCaller.makeApiCall(token, config.postMessageUrl, Json.toJson(message))
      .flatMap(response => SlackClient.jsonToClass[MessageResponse](response.body))
  }

  type MessageTs = String

  /**
    * https://slack.com/api/chat.postEphemeral
    */
  def postEphemeral(chatEphemeral: ChatEphemeral)(implicit ec: ExecutionContext): Future[MessageTs] = {
    slackCaller.makeApiCall(token, config.postEphemeralUrl, Json.toJson(chatEphemeral))
      .flatMap { response =>
        val bodyJson = Json.parse(response.body)
        (bodyJson \ "ok").validate[Boolean] match {
          case JsSuccess(true, _) =>
            (bodyJson \ "message_ts").validate[String] match {
              case JsSuccess(message, _) => Future.successful(message)
              case _ => Future.failed(new Exception(s"This is not the response expected : ${response.body}"))
            }
          case JsSuccess(false, _) =>
            (bodyJson \ "error").validate[String] match {
              case JsSuccess(error, _)=> Future.failed(new Exception(s"Error: $error"))
              case _ => Future.failed(new Exception(s"This is not the response expected : ${response.body}"))
            }
          case _ => Future.failed(new Exception(s"This is not the response expected : ${response.body}"))
        }
      }
  }

  /**
    * https://slack.com/api/chat.delete
    */
  def deleteMessage(channel: String, ts: String, asUser: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    slackCaller.makeApiCall(token, config.deleteMessageUrl,
      Json.obj(
        "channel" -> channel,
        "ts" -> ts,
        "as_user" -> asUser,
      ))
      .flatMap(response => SlackClient.jsonToClass[ChatResponse](response.body))
  }

  /**
    * https://slack.com/api/dialog.open
    */
  def openDialog(dialog: Dialog, triggerId: String)(implicit ec: ExecutionContext): Future[Unit] = {
    slackCaller.makeApiCall(token, config.openDialogUrl,
      Json.obj(
        "dialog" -> dialog,
        "trigger_id" -> triggerId
      )
    ).flatMap(response => {
      val jsonBody = Json.parse(response.body)
      (jsonBody \ "ok").validate[Boolean] match {
        case JsSuccess(ok, _) if ok => Future.successful(())
        case _ =>
          (jsonBody \ "error").validateOpt[String] match {
            case JsSuccess(err, _) => Future.failed(new Exception(s"Error: $err"))
            case _ => Future.failed(new Exception("There was an error with the query response"))
          }
      }
    })
  }

  /**
    * https://slack.com/api/chat.update
    */
  def updateMessage(channel: String, text: String, ts: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                    linkNames: Option[Boolean] = None, parse: Option[String] = None)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    slackCaller.makeApiCall(token, config.updateMessageUrl,
      Json.obj(
        "channel" -> channel,
        "text" -> text,
        "ts" -> ts,
        "as_user" -> asUser,
        "attachments" -> attachments,
        "link_names" -> linkNames,
        "parse" -> parse
      ))
      .flatMap(response => SlackClient.jsonToClass[ChatResponse](response.body))
  }

  /**
    * https://slack.com/api/chat.meMessage
    */
  def meMessage(channel: String, text: String)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    slackCaller.makeApiCall(token, config.meMessageUrl,
      Json.obj(
        "channel" -> channel,
        "text" -> text
      ))
      .flatMap(response => SlackClient.jsonToClass[ChatResponse](response.body))
  }

  /**
    * https://slack.com/api/channels.list
    */
  def channelList(cursor: Option[String] = None, excludeArchived: Option[Boolean] = None, excludeMembers: Option[Boolean] = None,
                  limit: Option[Int] = None)(implicit ec: ExecutionContext): Future[Channels] = {
    slackCaller.makeApiCall(token, config.channelListUrl,
      Json.obj(
        "cursor" -> cursor,
        "exclude_archived" -> excludeArchived,
        "exclude_members" -> excludeMembers,
        "limit" -> limit
      )).flatMap(response => SlackClient.jsonToClass[Channels](response.body))
  }

  /**
    * https://slack.com/api/im.close
    */
  def imClose(channel: String)(implicit ec: ExecutionContext): Future[Unit] = {
    slackCaller.makeApiCall(token, config.imCloseUrl,
      Json.obj(
        "channel" -> channel
      ))
      .flatMap { response =>
        handleResponse(response)
      }
  }

  private def handleResponse(response: StandaloneWSResponse) = {
    val jsonBody = Json.parse(response.body)
    (jsonBody \ "ok").validate[Boolean] match {
      case JsSuccess(ok, _) if ok => Future.successful(())
      case _ =>
        (jsonBody \ "error").validate[String] match {
          case JsSuccess(err, _) => Future.failed(new Exception(s"Error: $err"))
          case _ => Future.failed(new Exception("There was an error with the query response"))
        }
    }
  }

  /**
    * https://slack.com/api/im.open
    */
  def imOpen(user: String, includeLocale: Option[Boolean] = None, returnIm: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[String] = {
    val response = slackCaller.makeApiCall(token, config.imOpenUrl,
      Json.obj(
        "user" -> user,
        "include_locale" -> includeLocale,
        "return_im" -> returnIm
      ))

    response.flatMap { response =>
      val jsonBody = Json.parse(response.body)
      (jsonBody \ "ok").validate[Boolean] match {
        case JsSuccess(ok, _) if ok =>
          (jsonBody \ "channel" \ "id").validate[String] match {
            case JsSuccess(channelId, _) => Future.successful(channelId)
            case _ => Future.failed(new Exception("An error has occurred"))
          }
        case _ =>
          (jsonBody \ "error").validate[String] match {
            case JsSuccess(err, _) => Future.failed(new Exception(s"Error: $err"))
            case _ => Future.failed(new Exception("There was an error with the query response"))
          }
      }
    }
  }

  /**
    * https://slack.com/api/users.info
    */
  def userInfo(user: String, includeLocale: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[UserInfo] = {
    val params: Map[String, String] = Map("user" -> user, "include_locale" -> includeLocale.getOrElse(false).toString)

    slackCaller.makeGetApiCall(token, config.userInfoUrl, params)
      .flatMap(response => SlackClient.jsonToClass[UserInfo](response.body))
  }

  /**
    * https://slack.com/api/users.list
    */
  def usersList(cursor: Option[String] = None, includeLocale: Option[Boolean] = None, limit: Option[Int] = None
                , presence: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[UsersList] = {
    val params: Map[String, String] = Map("cursor" -> cursor.getOrElse(""), "include_locale" -> includeLocale.getOrElse(false).toString,
      "limit" -> limit.toString, "presence" -> presence.getOrElse(false).toString)

    slackCaller.makeGetApiCall(token, config.usersListUrl, params)
      .flatMap(response => SlackClient.jsonToClass[UsersList](response.body))
  }

  /**
    * https://api.slack.com/methods/users.lookupByEmail
    */
  def userLookupByEmail(email: String)(implicit ec: ExecutionContext): Future[UserInfo] = {
    val params: Map[String, String] = Map("email" -> email)

    slackCaller.makeGetApiCall(token, config.userLookupByEmailUrl, params)
      .flatMap(response => SlackClient.jsonToClass[UserInfo](response.body) match {
        case Success(s) => Future.successful(s)
        case Failure(e) => Future.failed(e)
      })
  }


}

object SlackClient {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def apply(token: String): SlackClient = new SlackClient(token, SlackConfig.getSlackClientConfig, new SlackCaller())

  def apply(token: String, config: SlackClientConfig) = new SlackClient(token, config, new SlackCaller())

  def apply(token: String, slackCaller: SlackCaller): SlackClient = new SlackClient(token, SlackConfig.getSlackClientConfig, slackCaller)

  private[api] def jsonToClass[T](response: String)(implicit ec: ExecutionContext, reads: Reads[T]) = {
    val jsonResponse = Json.parse(response)

    (jsonResponse \ "ok").validate[Boolean] match {
      case JsSuccess(ok, _) if ok => Json.fromJson[T](jsonResponse) match {
        case JsSuccess(classFromJson, _) => Success(classFromJson)
        case JsError(error) => Failure(new Exception(s"Bad json format : $error"))
      }
      case _ => (jsonResponse \ "error").validate[String] match {
        case JsSuccess(err, _) => Failure(new Exception(s"Error: $err"))
        case _ => Failure(new Exception("An error has occurred"))
      }
    }
  }
}
