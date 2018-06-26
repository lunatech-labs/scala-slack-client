package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class ActionsField(
  name: String,
  `type`: String,
  selected_options: Option[Seq[SelectedOption]],
  value: Option[String],
)

case class SelectedOption(value: String)

case class TeamField(id: String, domain: String)

case class NameField(id: String, name: String)

case class ConfirmField(
  text: String,
  title: Option[String] = None,
  ok_text: Option[String] = None,
  dismiss_text: Option[String] = None
)

case class OptionGroupsField(
  text: String,
  options: Seq[BasicField]
)

case class OptionField(options: Seq[BasicField])

case class BasicField(text: String, value: String, description: Option[String] = None)

case class ChatMessage(
  channel: String,
  text: String,
  as_user: Option[Boolean] = None,
  attachments: Option[Seq[AttachmentField]] = None,
  icon_emoji: Option[String] = None,
  icon_url: Option[String] = None,
  link_names: Option[Boolean] = None,
  mrkdwn: Option[Boolean] = None,
  parse: Option[String] = None,
  reply_broadcast: Option[Boolean] = None,
  thread_ts: Option[String] = None,
  unfurl_links: Option[Boolean] = None,
  unfurl_media: Option[Boolean] = None,
  username: Option[String] = None
) {

  def asUser(username: Option[String]): ChatMessage = copy(as_user = Some(true), username = username)

  def withUsername(username: String): ChatMessage = copy(username = Some(username))

  def addAttachment(attachmentField: AttachmentField): ChatMessage = {
    attachments match {
      case Some(atts) => copy(attachments = Some(atts :+ attachmentField))
      case None => copy(attachments = Some(List(attachmentField)))
    }
  }

  def withIconEmoji(iconEmoji: String): ChatMessage = copy(icon_emoji = Some(iconEmoji))

  def withIconUrl(iconUrl: String): ChatMessage = copy(icon_url = Some(iconUrl))

  def disableNamesLinking: ChatMessage = copy(link_names = Some(false))

  def enableNamesLinking: ChatMessage = copy(link_names = Some(true))

  def disableMarkDownParsing: ChatMessage = copy(mrkdwn = Some(false))

  def withingThread(threadTs: String, isBroadcast: Boolean = false): ChatMessage = copy(thread_ts = Some(threadTs), reply_broadcast = Some(isBroadcast))

  def asBroadcastMessage: ChatMessage = copy(reply_broadcast = Some(true))

  def enableLinkUnfurling: ChatMessage = copy(unfurl_links = Some(true))

  def disableLinkUnfurling: ChatMessage = copy(unfurl_links = Some(false))

  def enableMediaUnfurling: ChatMessage = copy(unfurl_media = Some(true))

  def disableMediaUnfurling: ChatMessage = copy(unfurl_media = Some(false))

}

object ChatMessage {
  implicit val chatMessageFormat = Json.format[ChatMessage]

  def apply(
    channel: String,
    text: String
  ): ChatMessage = new ChatMessage(channel, text)
}


object BasicField {
  implicit val basicFieldFormat = Json.format[BasicField]
}

object ConfirmField {
  implicit val confirmFieldFormat = Json.format[ConfirmField]
}

object OptionField {
  implicit val optionFieldFormat = Json.format[OptionField]
}

object OptionGroupsField {
  implicit val optionGroupsFieldFormat = Json.format[OptionGroupsField]
}