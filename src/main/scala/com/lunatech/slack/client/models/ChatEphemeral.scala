package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class ChatEphemeral(channel: String,
  text: String,
  user: String,
  as_user: Option[Boolean] = None,
  attachments: Option[Seq[AttachmentField]] = None,
  link_names: Option[Boolean] = None,
  parse: Option[String] = None) {

  def asUser: ChatEphemeral = copy(as_user = Some(true))

  def addAttachment(attachmentField: AttachmentField): ChatEphemeral = {
    attachments match {
      case Some(atts) => copy(attachments = Some(atts :+ attachmentField))
      case None => copy(attachments = Some(List(attachmentField)))
    }
  }

  def disableNamesLinking: ChatEphemeral = copy(link_names = Some(false))

  def enableNamesLinking: ChatEphemeral = copy(link_names = Some(true))

}

case class AttachmentResponse(text: Option[String] = None,
  id: Option[Int] = None,
  fallback: Option[String] = None)

case class EmbeddedMessageResponse(text: Option[String] = None,
  username: Option[String] = None,
  bot_id: Option[String] = None,
  attachments: Option[Seq[AttachmentResponse]] = None,
  `type`: Option[String] = None,
  subtype: Option[String] = None,
  ts: Option[String] = None)

case class MessageResponse(channel: Option[String] = None,
  ts: Option[String] = None,
  message: Option[EmbeddedMessageResponse] = None)

case class ChatResponse(channel: Option[String] = None, ts: Option[String] = None, text: Option[String] = None)

object ChatEphemeral {
  implicit val chatEphemeralFormat = Json.format[ChatEphemeral]
}

object AttachmentResponse {
  implicit val attachmentResponseFormat = Json.format[AttachmentResponse]
}

object EmbeddedMessageResponse {
  implicit val embeddedMessageResponseFormat = Json.format[EmbeddedMessageResponse]
}

object MessageResponse {
  implicit val messageResponseFormat = Json.format[MessageResponse]
}

object ChatResponse {
  implicit val chatResponseFormat = Json.format[ChatResponse]
}