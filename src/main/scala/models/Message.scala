package models

import com.fasterxml.jackson.core.{JsonParseException, JsonParser}
import play.api.libs.json.{JsResult, JsValue, Json}

case class Payload(
                    `type`: String,
                    actions: Seq[ActionsField],
                    callback_id: String,
                    team: TeamField,
                    channel: NameField,
                    user: NameField,
                    action_ts: String,
                    message_ts: String,
                    attachment_id: String,
                    token: String,
                    is_app_unfurl: Boolean,
                    original_message: Message,
                    response_url: String,
                    trigger_id: String
                  )

object Payload {
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

  def getPayload(payload: String): Payload = {
    val jsonPayload: JsValue = Json.parse(payload)

    val payloadFromJson: JsResult[Payload] = Json.fromJson[Payload](jsonPayload)

    payloadFromJson.getOrElse(throw new JsonParseException(null, "Bad payload format"))
  }
}

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

case class AttachmentField(
                            fallback: String,
                            callback_id: String,
                            actions: Seq[ActionField],
                            text: Option[String] = None,
                            title: Option[String] = None,
                            id: Option[Int] = None,
                            color: Option[String] = None,
                            attachment_type: Option[String] = None
                          )

case class ActionField(
                        name: String,
                        text: String,
                        `type`: String,
                        id: Option[String] = None,
                        value: Option[String] = None,
                        confirm: Option[ConfirmField] = None,
                        style: Option[String] = None,
                        data_source: Option[String] = None,
                        options: Option[Seq[BasicField]] = None,
                        option_groups: Option[Seq[OptionGroupsField]] = None,
                        min_query_length: Option[Int] = None,
                        selected_options: Option[Seq[OptionField]] = None
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
