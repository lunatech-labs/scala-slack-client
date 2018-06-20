package models

case class ActionsField(
                         name: String,
                         `type`: String,
                         selected_options: Seq[SelectedOption]
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