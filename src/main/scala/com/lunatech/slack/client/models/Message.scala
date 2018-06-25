package com.lunatech.slack.client.models

case class ActionsField(
                         name: String,
                         `type`: String,
                         selected_options: Option[Seq[SelectedOption]],
                         value: Option[String],
                       )

case class SelectedOption(
                           value: String
                         )

case class TeamField(
                      id: String,
                      domain: String
                    )

case class NameField(
                      id: String,
                      name: String
                    )

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

case class BasicField(
                       text: String,
                       value: String,
                       description: Option[String] = None
                     )

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

  def disableNamesLinking(): ChatMessage = copy(link_names = Some(false))

  def enableNamesLinking(): ChatMessage = copy(link_names = Some(true))

  def disableMarkDownParsing(): ChatMessage = copy(mrkdwn = Some(false))

  def withingThread(threadTs: String, isBroadcast: Boolean = false): ChatMessage = copy(thread_ts = Some(threadTs), reply_broadcast = Some(isBroadcast))

  def asBroadcastMessage(): ChatMessage = copy(reply_broadcast = Some(true))

  def enableLinkUnfurling(): ChatMessage = copy(unfurl_links = Some(true))

  def disableLinkUnfurling(): ChatMessage = copy(unfurl_links = Some(false))

  def enableMediaUnfurling(): ChatMessage = copy(unfurl_media = Some(true))

  def disableMediaUnfurling(): ChatMessage = copy(unfurl_media = Some(false))

}

case class ChatEphemeral(channel: String,
                         text: String,
                         user: String,
                         as_user: Option[Boolean] = None,
                         attachments: Option[Seq[AttachmentField]] = None,
                         link_names: Option[Boolean] = None,
                         parse: Option[String] = None) {

  def asUser(): ChatEphemeral = copy(as_user = Some(true))

  def addAttachment(attachmentField: AttachmentField): ChatEphemeral = {
    attachments match {
      case Some(atts) => copy(attachments = Some(atts :+ attachmentField))
      case None => copy(attachments = Some(List(attachmentField)))
    }
  }

  def disableNamesLinking(): ChatEphemeral = copy(link_names = Some(false))

  def enableNamesLinking(): ChatEphemeral = copy(link_names = Some(true))

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


object ChatMessage {
  def apply(
             channel: String,
             text: String
           ): ChatMessage = new ChatMessage(channel, text)
}