package app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import models._
import play.api.libs.json._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

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
                  limit: Option[Int] = None) = {
    makeApiCall(system.settings.config.getString("slack.api.channelList"),
      Json.obj(
        "cursor" -> cursor,
        "exclude_archived" -> excludeArchived,
        "exclude_members" -> excludeMembers,
        "limit" -> limit
      ))
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

  def userInfo(user: String, includeLocale: Option[Boolean] = None) = {
    val params: Map[String, String] = Map("user" -> user, "include_locale" -> includeLocale.getOrElse(false).toString)

    makeGetApiCall(system.settings.config.getString("slack.api.userInfo"), params)
  }

  def usersList(cursor: Option[String] = None, includeLocale: Option[Boolean] = None, limit: Option[Int] = None, presence: Option[Boolean] = None) = {
    val params: Map[String, String] = Map("cursor" -> cursor.getOrElse(""), "include_locale" -> includeLocale.getOrElse(false).toString,
      "limit" -> limit.toString, "presence" -> presence.getOrElse(false).toString)

    makeGetApiCall(system.settings.config.getString("slack.api.usersList"), params)
  }

  def userLookupByEmail(email: String) = {
    val params: Map[String, String] = Map("email" -> email)

    makeGetApiCall(system.settings.config.getString("slack.api.userLookupByEmail"), params)
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
