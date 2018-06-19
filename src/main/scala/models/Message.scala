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
                      ) {

  def withConfirmation(confirmField: ConfirmField): ActionField = {
    ActionField(name, text, `type`, id, value, Some(confirmField), style, data_source, options, option_groups, min_query_length, selected_options)
  }

  def withConfirmation(text: String, title: Option[String] = None, ok_text: Option[String] = None, dismiss_text: Option[String] = None): ActionField = {
    withConfirmation(ConfirmField(text, title, ok_text, dismiss_text))
  }
}

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

object ActionField {
  def getDefaultButton(name: String,
                       text: String,
                       id: Option[String] = None,
                       value: Option[String] = None,
                       confirm: Option[ConfirmField] = None): ActionField = {

    ActionField(name, text, "button", id, value, confirm, Some("default"))
  }

  def getPrimaryButton(name: String,
                       text: String,
                       id: Option[String] = None,
                       value: Option[String] = None,
                       confirm: Option[ConfirmField] = None): ActionField = {

    ActionField(name, text, "button", id, value, confirm, Some("primary"))
  }

  def getDangerButton(name: String,
                      text: String,
                      id: Option[String] = None,
                      value: Option[String] = None,
                      confirm: Option[ConfirmField] = None): ActionField = {

    ActionField(name, text, "button", id, value, confirm, Some("danger"))
  }

  def getStaticMenu(name: String,
                    text: String,
                    fields: Seq[BasicField],
                    id: Option[String] = None,
                    value: Option[String] = None,
                    confirm: Option[ConfirmField] = None,
                    selected_options: Option[Seq[OptionField]] = None): ActionField = {

    ActionField(name, text, "select", id, value, confirm, None, None, Some(fields), None, None, selected_options)
  }

  def getUserMenu(name: String,
                  text: String,
                  id: Option[String] = None,
                  value: Option[String] = None,
                  confirm: Option[ConfirmField] = None,
                  selected_options: Option[Seq[OptionField]] = None): ActionField = {

    ActionField(name, text, "select", id, value, confirm, None, Some("users"), None, None, None, selected_options)
  }

  def getChannelMenu(name: String,
                     text: String,
                     id: Option[String] = None,
                     value: Option[String] = None,
                     confirm: Option[ConfirmField] = None,
                     selected_options: Option[Seq[OptionField]] = None): ActionField = {

    ActionField(name, text, "select", id, value, confirm, None, Some("channels"), None, None, None, selected_options)
  }

  def getConversationMenu(name: String,
                          text: String,
                          id: Option[String] = None,
                          value: Option[String] = None,
                          confirm: Option[ConfirmField] = None,
                          selected_options: Option[Seq[OptionField]] = None): ActionField = {

    ActionField(name, text, "select", id, value, confirm, None, Some("conversations"), None, None, None, selected_options)
  }

  def getExternalMenu(name: String,
                      text: String,
                      id: Option[String] = None,
                      value: Option[String] = None,
                      confirm: Option[ConfirmField] = None,
                      minQueryLength: Option[Int] = None,
                      selected_options: Option[Seq[OptionField]] = None): ActionField = {
    ActionField(name, text, "select", id, value, confirm, None, Some("external"), None, None, minQueryLength, selected_options)
  }
}
