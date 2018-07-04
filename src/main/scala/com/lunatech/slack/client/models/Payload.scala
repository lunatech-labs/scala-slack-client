package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class Payload(
  `type`: String,
  actions: Option[Seq[ActionsField]],
  submission: Option[Map[String, String]],
  callback_id: String,
  team: TeamField,
  channel: NameField,
  user: NameField,
  action_ts: String,
  message_ts: Option[String],
  attachment_id: Option[String],
  token: String,
  is_app_unfurl: Option[Boolean],
  original_message: Option[Message],
  response_url: String,
  trigger_id: Option[String],
)

case class Message(
  text: Option[String] = None,
  attachments: Option[Seq[AttachmentField]] = None,
  channel: Option[String] = None,
  thread_ts: Option[String] = None,
  bot_id: Option[String] = None,
  response_type: Option[String] = None,
  `type`: Option[String] = None,
  user: Option[String] = None,
  subtype: Option[String] = None,
  ts: Option[String] = None,
  replace_original: Option[Boolean] = None,
  delete_original: Option[Boolean] = None
) {

  def addAttachment(attachmentField: AttachmentField): Message = copy(attachments = Some(attachments.getOrElse(Seq()) :+ attachmentField))

  def toChannel(channel: String): Message = copy(channel = Some(channel))

  def withThreadTs(channel: String): Message = copy(thread_ts = Some(channel))

  def andReplaceOriginal : Message = copy(replace_original = Some(true))

  def andDeleteOriginal : Message = copy(delete_original = Some(true))
}

case class TeamField(id: String, domain: String)

case class NameField(id: String, name: String)

case class ActionsField(
  name: String,
  `type`: String,
  selected_options: Option[Seq[SelectedOption]],
  value: Option[String],
)

case class SelectedOption(value: String)

object SelectedOption {
  implicit val selectedFieldFormat = Json.format[SelectedOption]
}

object ActionsField {
  implicit val actionsFieldFormat = Json.format[ActionsField]
}

object NameField {
  implicit val nameFieldFormat = Json.format[NameField]
}

object TeamField {
  implicit val teamFieldFormat = Json.format[TeamField]
}

object Message {
  implicit val messageFormat = Json.format[Message]

  def apply(text: String): Message = new Message(text = Some(text))
}

object Payload {
  implicit val payloadFormat = Json.format[Payload]
}


