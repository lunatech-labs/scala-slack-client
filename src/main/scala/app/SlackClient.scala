package app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import models._
import play.api.libs.json._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SlackClient(token: String) {
  implicit val basicFieldFormat = Json.format[BasicField]
  implicit val optionFieldFormat = Json.format[OptionField]
  implicit val optionGroupsFieldFormat = Json.format[OptionGroupsField]
  implicit val nameFieldFormat = Json.format[NameField]
  implicit val confirmFieldFormat = Json.format[ConfirmField]
  implicit val teamFieldFormat = Json.format[TeamField]
  implicit val selectedFieldFormat = Json.format[SelectedOption]
  implicit val actionFieldFormat = Json.format[ActionField]
  implicit val actionsFieldFormat = Json.format[ActionsField]
  implicit val attachmentFieldFormat = Json.format[AttachmentField]
  implicit val originalMessageFormat = Json.format[Message]
  implicit val payloadFormat = Json.format[Payload]

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val wsClient = StandaloneAhcWSClient()

  def postMessage(channel: String, text: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
                  iconEmoji: Option[String] = None, iconUrl: Option[String] = None, linkNames: Option[String] = None,
                  mrkdwn : Option[Boolean] = None, parse: Option[String] = None, replyBroadcast: Option[Boolean] = None,
                  threadTs: Option[String] = None, unfurlLinks: Option[Boolean] = None, unfurlMedia: Option[Boolean] = None,
                  username: Option[String] = None) ={
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

  private def makeApiCall(url: String, body: JsValue) = {
    wsClient.url(url)
      .withHttpHeaders(
        "Content-Type" -> "application/json",
        "Authorization"-> s"Bearer $token")
      .post(body)
  }
}
