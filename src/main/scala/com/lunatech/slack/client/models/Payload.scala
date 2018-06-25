package com.lunatech.slack.client.models

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
                    attachments: Option[Seq[AttachmentField]],
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
                  )

