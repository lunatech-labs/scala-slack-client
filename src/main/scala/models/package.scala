import play.api.libs.json.Json

package object models {
  implicit val basicFieldFormat = Json.format[BasicField]
  implicit val optionFieldFormat = Json.format[OptionField]
  implicit val optionGroupsFieldFormat = Json.format[OptionGroupsField]
  implicit val nameFieldFormat = Json.format[NameField]
  implicit val confirmFieldFormat = Json.format[ConfirmField]
  implicit val teamFieldFormat = Json.format[TeamField]
  implicit val selectedFieldFormat = Json.format[SelectedOption]
  implicit val actionFieldFormat = Json.format[ActionField]
  implicit val actionsFieldFormat = Json.format[ActionsField]
  implicit val fieldFormat = Json.format[Field]
  implicit val attachmentFieldFormat = Json.format[AttachmentField]
  implicit val originalMessageFormat = Json.format[Message]
  implicit val payloadFormat = Json.format[Payload]
  implicit val chatMessageFormat = Json.format[ChatMessage]
}
