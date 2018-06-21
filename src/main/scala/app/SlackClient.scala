package app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import models._
import play.api.libs.json._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}

class SlackClient(token: String) {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val wsClient = StandaloneAhcWSClient()

  def getPermalinkMessage(channel: String, messageTs: String) = {
    val params: Map[String, String] = Map("channel" -> channel, "message_ts" -> messageTs)

    makeGetApiCall(system.settings.config.getString("slack.api.getPermalinkMessage"), params)
  }

  def postMessage(channel: String, text: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                  iconEmoji: Option[String] = None, iconUrl: Option[String] = None, linkNames: Option[String] = None,
                  mrkdwn: Option[Boolean] = None, parse: Option[String] = None, replyBroadcast: Option[Boolean] = None,
                  threadTs: Option[String] = None, unfurlLinks: Option[Boolean] = None, unfurlMedia: Option[Boolean] = None,
                  username: Option[String] = None) = {
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
  }

  def postMessage(message: ChatMessage) = {
    makeApiCall(system.settings.config.getString("slack.api.postMessage"), Json.toJson(message))
  }

  def postEphemeral(channel: String, text: String, user: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                    linkNames: Option[Boolean] = None, parse: Option[String] = None) = {
    makeApiCall(system.settings.config.getString("slack.api.postEphemeral"),
      Json.obj(
        "channel" -> channel,
        "text" -> text,
        "user" -> user,
        "as_user" -> asUser,
        "attachments" -> attachments,
        "link_names" -> linkNames,
        "parse" -> parse
      ))
  }

  def deleteMessage(channel: String, ts: String, asUser: Option[Boolean] = None) = {
    makeApiCall(system.settings.config.getString("slack.api.deleteMessage"),
      Json.obj(
        "channel" -> channel,
        "ts" -> ts,
        "as_user" -> asUser,
      ))
  }

  def openDialog(triggerId: String) = {
    makeApiCall(system.settings.config.getString("slack.api.openDialog"),
      Json.obj()
    )
  }

  def updateMessage(channel: String, text: String, ts: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                    linkNames: Option[Boolean] = None, parse: Option[String] = None) = {
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
  }

  def meMessage(channel: String, text: String) = {
    makeApiCall(system.settings.config.getString("slack.api.meMessage"),
      Json.obj(
        "channel" -> channel,
        "text" -> text
      ))
  }

  def channelList(cursor: Option[String] = None, excludeArchived: Option[Boolean] = None, excludeMembers: Option[Boolean] = None,
                  limit: Option[Int] = None)(implicit ec: ExecutionContext) = {
    val response = makeApiCall(system.settings.config.getString("slack.api.channelList"),
      Json.obj(
        "cursor" -> cursor,
        "exclude_archived" -> excludeArchived,
        "exclude_members" -> excludeMembers,
        "limit" -> limit
      ))

    response.flatMap(result => {
      val jsonResponse = Json.parse(result.body)

      val ok = (jsonResponse \ "ok").validate[Boolean].getOrElse(false)

      if (!ok) {
        val error = (jsonResponse \ "error").validate[String].getOrElse("An error has occurred")
        Future.failed(new Exception("Error : " + error))
      }
      else {
        val channelListFromJson = Json.fromJson[Channels](jsonResponse)

        channelListFromJson match {
          case user: JsSuccess[Channels] =>
            Future.successful(user.value)
          case error: JsError =>
            Future.failed(new Exception("Bad json format : " + error))
        }
      }
    })
  }

  def imClose(channel: String) = {
    makeApiCall(system.settings.config.getString("slack.api.imClose"),
      Json.obj(
        "channel" -> channel
      ))
  }

  def imOpen(user: String, includeLocale: Option[Boolean] = None, returnIm: Option[Boolean] = None) = {
    makeApiCall(system.settings.config.getString("slack.api.imOpen"),
      Json.obj(
        "user" -> user,
        "include_locale" -> includeLocale,
        "return_im" -> returnIm
      ))
  }

  def userInfo(user: String, includeLocale: Option[Boolean] = None)(implicit ec: ExecutionContext) = {
    val params: Map[String, String] = Map("user" -> user, "include_locale" -> includeLocale.getOrElse(false).toString)

    val response = makeGetApiCall(system.settings.config.getString("slack.api.userInfo"), params)
      .map(_.body)
    jsonToClass[UserInfo](response)
  }

  private def jsonToClass[T](response: Future[String])(implicit ec: ExecutionContext, reads: Reads[T]) = {
    response.flatMap(body => {
      val jsonResponse = Json.parse(body)

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
    })
  }

  def usersList(cursor: Option[String] = None, includeLocale: Option[Boolean] = None, limit: Option[Int] = None, presence: Option[Boolean] = None)(implicit ec: ExecutionContext) = {
    val params: Map[String, String] = Map("cursor" -> cursor.getOrElse(""), "include_locale" -> includeLocale.getOrElse(false).toString,
      "limit" -> limit.toString, "presence" -> presence.getOrElse(false).toString)

    val response = makeGetApiCall(system.settings.config.getString("slack.api.usersList"), params)

    response.flatMap(result => {
      val jsonResponse = Json.parse(result.body)

      val ok = (jsonResponse \ "ok").validate[Boolean].getOrElse(false)

      if (!ok) {
        val error = (jsonResponse \ "error").validate[String].getOrElse("An error has occurred")
        Future.failed(new Exception("Error : " + error))
      }
      else {
        val usersListFromJson = Json.fromJson[UsersList](jsonResponse)

        usersListFromJson match {
          case user: JsSuccess[UsersList] =>
            Future.successful(user.value)
          case error: JsError =>
            Future.failed(new Exception("Bad json format : " + error))
        }
      }
    })
  }

  def userLookupByEmail(email: String)(implicit ec: ExecutionContext) = {
    val params: Map[String, String] = Map("email" -> email)

    val response = makeGetApiCall(system.settings.config.getString("slack.api.userLookupByEmail"), params)

    response.flatMap(result => {
      val jsonResponse = Json.parse(result.body)

      val ok = (jsonResponse \ "ok").validate[Boolean].getOrElse(false)

      if (!ok) {
        val error = (jsonResponse \ "error").validate[String].getOrElse("An error has occurred")
        Future.failed(new Exception("Error : " + error))
      }
      else {
        val userFromJson = Json.fromJson[UserInfo](jsonResponse)

        userFromJson match {
          case user: JsSuccess[UserInfo] =>
            Future.successful(user.value)
          case error: JsError =>
            Future.failed(new Exception("Bad json format : " + error))
        }
      }
    })
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
}
