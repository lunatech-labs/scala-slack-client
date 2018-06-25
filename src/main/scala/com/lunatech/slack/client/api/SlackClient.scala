package com.lunatech.slack.client.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.lunatech.slack.client.models.{ChatMessage, MessageResponse, _}
import play.api.libs.json._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}


class SlackClient(token: String) {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val wsClient = StandaloneAhcWSClient()

  /**
    * https://slack.com/api/chat.getPermalink
    */
  def getPermalinkMessage(channel: String, messageTs: String)(implicit ec: ExecutionContext): Future[PermaLink] = {
    val params: Map[String, String] = Map("channel" -> channel, "message_ts" -> messageTs)

    makeGetApiCall(system.settings.config.getString("slack.api.getPermalinkMessage"), params)
      .flatMap(response => jsonToClass[PermaLink](response.body))
  }

  /**
    * https://api.slack.com/methods/chat.postMessage
    */
  def postMessage(channel: String, text: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                  iconEmoji: Option[String] = None, iconUrl: Option[String] = None, linkNames: Option[String] = None,
                  mrkdwn: Option[Boolean] = None, parse: Option[String] = None, replyBroadcast: Option[Boolean] = None,
                  threadTs: Option[String] = None, unfurlLinks: Option[Boolean] = None, unfurlMedia: Option[Boolean] = None,
                  username: Option[String] = None)(implicit ec: ExecutionContext): Future[MessageResponse] = {
    makeApiCall(system.settings.config.getString("slack.api.postMessage"),
      Json.obj(
        "channel" -> channel,
        "text" -> text,
        "as_user" -> asUser,
        "attachments" -> attachments,
        "icon_emoji" -> iconEmoji,
        "icon_url" -> iconUrl,
        "link_names" -> linkNames,
        "mrkdwn" -> mrkdwn,
        "parse" -> parse,
        "reply_broadcast" -> replyBroadcast,
        "thread_ts" -> threadTs,
        "unfurl_links" -> unfurlLinks,
        "unfurl_media" -> unfurlMedia,
        "username" -> username
      ))
      .flatMap(response => jsonToClass[MessageResponse](response.body))
  }

  /**
    * https://api.slack.com/methods/chat.postMessage
    */
  def postMessage(message: ChatMessage)(implicit ec: ExecutionContext): Future[MessageResponse] = {
    makeApiCall(system.settings.config.getString("slack.api.postMessage"), Json.toJson(message))
      .flatMap(response => jsonToClass[MessageResponse](response.body))
  }

  /**
    * https://slack.com/api/chat.postEphemeral
    */
  def postEphemeral(channel: String, text: String, user: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                    linkNames: Option[Boolean] = None, parse: Option[String] = None)(implicit ec: ExecutionContext): Future[MessageResponse] = {
    makeApiCall(system.settings.config.getString("slack.api.postEphemeral"),
      Json.obj(
        "channel" -> channel,
        "text" -> text,
        "user" -> user,
        "as_user" -> asUser,
        "attachments" -> attachments,
        "link_names" -> linkNames,
        "parse" -> parse
      )).flatMap(response => jsonToClass[MessageResponse](response.body))
  }

  /**
    * https://slack.com/api/chat.postEphemeral
    */
  def postEphemeral(chatEphemeral: ChatEphemeral)(implicit ec: ExecutionContext): Future[MessageResponse] = {
    makeApiCall(system.settings.config.getString("slack.api.postEphemeral"), Json.toJson(chatEphemeral))
      .flatMap(response => jsonToClass[MessageResponse](response.body))
  }

  /**
    * https://slack.com/api/chat.delete
    */
  def deleteMessage(channel: String, ts: String, asUser: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    makeApiCall(system.settings.config.getString("slack.api.deleteMessage"),
      Json.obj(
        "channel" -> channel,
        "ts" -> ts,
        "as_user" -> asUser,
      ))
      .flatMap(response => jsonToClass[ChatResponse](response.body))
  }

  /**
    * https://slack.com/api/dialog.open
    */
  def openDialog(dialog: Dialog, triggerId: String)(implicit ec: ExecutionContext) = {
    makeApiCall(system.settings.config.getString("slack.api.openDialog"),
      Json.obj(
        "dialog" -> dialog,
        "trigger_id" -> triggerId
      )
    ).flatMap(response => {
      val jsonBody = Json.parse(response.body)
      val ok = (jsonBody \ "ok").validate[Boolean].getOrElse(false)

      if (ok) {
        Future.successful(None)
      } else {
        val error = (jsonBody \ "error").validateOpt[String].getOrElse(None)

        error match {
          case Some(err) => Future.failed(new Exception(s"Error: $err"))
          case None => Future.failed(new Exception("There was an error with the query response"))
        }
      }
    })
  }

  /**
    * https://slack.com/api/chat.update
    */
  def updateMessage(channel: String, text: String, ts: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                    linkNames: Option[Boolean] = None, parse: Option[String] = None)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    makeApiCall(system.settings.config.getString("slack.api.updateMessage"),
      Json.obj(
        "channel" -> channel,
        "text" -> text,
        "ts" -> ts,
        "as_user" -> asUser,
        "attachments" -> attachments,
        "link_names" -> linkNames,
        "parse" -> parse
      ))
      .flatMap(response => jsonToClass[ChatResponse](response.body))
  }

  /**
    * https://slack.com/api/chat.meMessage
    */
  def meMessage(channel: String, text: String)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    makeApiCall(system.settings.config.getString("slack.api.meMessage"),
      Json.obj(
        "channel" -> channel,
        "text" -> text
      ))
      .flatMap(response => jsonToClass[ChatResponse](response.body))
  }

  /**
    * https://slack.com/api/channels.list
    */
  def channelList(cursor: Option[String] = None, excludeArchived: Option[Boolean] = None, excludeMembers: Option[Boolean] = None,
                  limit: Option[Int] = None)(implicit ec: ExecutionContext): Future[Channels] = {
    makeApiCall(system.settings.config.getString("slack.api.channelList"),
      Json.obj(
        "cursor" -> cursor,
        "exclude_archived" -> excludeArchived,
        "exclude_members" -> excludeMembers,
        "limit" -> limit
      )).flatMap(response => jsonToClass[Channels](response.body))
  }

  /**
    * https://slack.com/api/im.close
    */
  def imClose(channel: String)(implicit ec: ExecutionContext): Future[None.type] = {
    makeApiCall(system.settings.config.getString("slack.api.imClose"),
      Json.obj(
        "channel" -> channel
      ))
      .flatMap(response => {
        val jsonBody = Json.parse(response.body)
        val ok = (jsonBody \ "ok").validate[Boolean].getOrElse(false)

        if (ok) {
          Future.successful(None)
        } else {
          val error = (jsonBody \ "error").validateOpt[String].getOrElse(None)

          error match {
            case Some(err) => Future.failed(new Exception(s"Error: $err"))
            case None => Future.failed(new Exception("There was an error with the query response"))
          }
        }
      })
  }

  /**
    * https://slack.com/api/im.open
    */
  def imOpen(user: String, includeLocale: Option[Boolean] = None, returnIm: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[String] = {
    val response = makeApiCall(system.settings.config.getString("slack.api.imOpen"),
      Json.obj(
        "user" -> user,
        "include_locale" -> includeLocale,
        "return_im" -> returnIm
      ))

    response.flatMap(result => {
      val jsonResponse = Json.parse(result.body)
      val ok = (jsonResponse \ "ok").validate[Boolean].getOrElse(false)

      if (!ok) {
        val error = (jsonResponse \ "error").validate[String].getOrElse("An error has occurred")
        Future.failed(new Exception(s"Error : $error"))
      } else {
        val channelId = (jsonResponse \ "channel" \ "id").validate[String].getOrElse("")
        Future.successful(channelId)
      }
    })
  }

  /**
    * https://slack.com/api/users.info
    */
  def userInfo(user: String, includeLocale: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[UserInfo] = {
    val params: Map[String, String] = Map("user" -> user, "include_locale" -> includeLocale.getOrElse(false).toString)

    makeGetApiCall(system.settings.config.getString("slack.api.userInfo"), params)
      .flatMap(response => jsonToClass[UserInfo](response.body))
  }

  /**
    * https://slack.com/api/users.list
    */
  def usersList(cursor: Option[String] = None, includeLocale: Option[Boolean] = None, limit: Option[Int] = None
                , presence: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[UsersList] = {
    val params: Map[String, String] = Map("cursor" -> cursor.getOrElse(""), "include_locale" -> includeLocale.getOrElse(false).toString,
      "limit" -> limit.toString, "presence" -> presence.getOrElse(false).toString)

    makeGetApiCall(system.settings.config.getString("slack.api.usersList"), params)
      .flatMap(response => jsonToClass[UsersList](response.body))
  }

  /**
    * https://slack.com/api/users.lookupByEmail
    */
  def userLookupByEmail(email: String)(implicit ec: ExecutionContext): Future[UserInfo] = {
    val params: Map[String, String] = Map("email" -> email)

    makeGetApiCall(system.settings.config.getString("slack.api.userLookupByEmail"), params)
      .flatMap(response => jsonToClass[UserInfo](response.body))
  }

  private def makeApiCall(url: String, body: JsValue) = {
    wsClient.url(url)
      .withHttpHeaders(
        "Content-Type" -> "application/json",
        "Authorization" -> s"Bearer $token")
      .post(body)
  }

  private def makeGetApiCall(url: String, params: Map[String, String]) = {
    wsClient.url(url)
      .withHttpHeaders(
        "Content-Type" -> "application/json",
        "Authorization" -> s"Bearer $token")
      .withQueryStringParameters(params.toSeq: _*)
      .get()
  }

  private def jsonToClass[T](response: String)(implicit ec: ExecutionContext, reads: Reads[T]) = {
    val jsonResponse = Json.parse(response)

    val ok = (jsonResponse \ "ok").validate[Boolean].getOrElse(false)

    if (!ok) {
      val error = (jsonResponse \ "error").validate[String].getOrElse("An error has occurred")
      Future.failed(new Exception(s"Error : $error"))
    }
    else {
      val fromJson = Json.fromJson[T](jsonResponse)

      fromJson match {
        case success: JsSuccess[T] =>
          Future.successful(success.value)
        case error: JsError =>
          Future.failed(new Exception(s"Bad json format : $error"))
      }
    }
  }

}
