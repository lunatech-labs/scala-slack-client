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

  implicit val metadataResponseFormat = Json.format[Metadata]
  implicit val profileFormat = Json.format[Profile]
  implicit val userFormat = Json.format[User]
  implicit val usersListResponseFormat = Json.format[UsersList]
  implicit val userInfoResponseFormat = Json.format[UserInfo]

  implicit val purposeFormat = Json.format[Purpose]
  implicit val topicFormat = Json.format[Topic]
  implicit val channelAttachmentsFormat = Json.format[ChannelAttachments]
  implicit val latestFormat = Json.format[Latest]
  implicit val channelFormat = Json.format[Channel]
  implicit val channelsFormat = Json.format[Channels]
  implicit val chatMessageFormat = Json.format[ChatMessage]
  implicit val chatEphemeralFormat = Json.format[ChatEphemeral]

  implicit val attachmentResponseFormat = Json.format[AttachmentResponse]
  implicit val embeddedMessageResponseFormat = Json.format[EmbeddedMessageResponse]
  implicit val messageResponseFormat = Json.format[MessageResponse]

  implicit val chatResponseFormat = Json.format[ChatResponse]
  implicit val permaLinkFormat = Json.format[PermaLink]

  implicit val dialogOptionFormat = Json.format[DialogOption]
  implicit val dialogOptionGroupFormat = Json.format[DialogOptionGroup]
  implicit val dialogElementFormat = Json.format[DialogElement]
  implicit val dialogFormat = Json.format[Dialog]

}
