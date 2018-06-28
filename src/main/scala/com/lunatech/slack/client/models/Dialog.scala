package com.lunatech.slack.client.models

import play.api.libs.json.Json

/**
  * https://api.slack.com/dialogs
  */

case class Dialog(title: String,
  callback_id: String,
  elements: Seq[DialogElement],
  submit_label: Option[String] = None,
  notify_on_cancel: Option[Boolean] = None) {

  def withSubmitLabel(submitLabel: String): Dialog = copy(submit_label = Some(submitLabel))

  def notifyOnCancel: Dialog = copy(notify_on_cancel = Some(true))

  def addElement(element: DialogElement): Dialog = copy(elements = elements :+ element)
}

case class DialogElement(label: String,
  name: String,
  `type`: String,
  max_length: Option[Int] = None,
  min_length: Option[Int] = None,
  optional: Option[Boolean] = None,
  hint: Option[String] = None,
  subtype: Option[String] = None,
  value: Option[String] = None,
  placeholder: Option[String] = None,
  options: Option[Seq[DialogOption]] = None,
  option_group: Option[Seq[DialogOptionGroup]] = None,
  data_source: Option[String] = None) {

  def withMaxLength(maxLength: Int): DialogElement = copy(max_length = Some(maxLength))

  def withMinLength(minLength: Int): DialogElement = copy(min_length = Some(minLength))

  def asOptional: DialogElement = copy(optional = Some(true))

  def asNotOptional: DialogElement = copy(optional = Some(false))

  def withHint(hint: String): DialogElement = copy(hint = Some(hint))

  def asEmail: DialogElement = copy(subtype = Some("email"))

  def asNumber: DialogElement = copy(subtype = Some("number"))

  def asTel: DialogElement = copy(subtype = Some("tel"))

  def asUrl: DialogElement = copy(subtype = Some("url"))

  def withValue(value: String): DialogElement = copy(value = Some(value))

  def withPlaceholder(placeholder: String): DialogElement = copy(placeholder = Some(placeholder))

  def asText: DialogElement = copy(`type` = "text")

  def asTextArea: DialogElement = copy(`type` = "textarea")

  def asMenu: DialogElement = copy(`type` = "select", data_source = Some("static"))

  def asUserMenu: DialogElement = copy(`type` = "select", data_source = Some("users"))

  def asChannelMenu: DialogElement = copy(`type` = "select", data_source = Some("channels"))

  def asConversationMenu: DialogElement = copy(`type` = "select", data_source = Some("conversations"))

  def asExternalMenu: DialogElement = copy(`type` = "select", data_source = Some("external"))

  def addOption(label: String, value: String): DialogElement = copy(options = Some(options.getOrElse(List()) :+ DialogOption(label, value)))

  def addOption(element: DialogOption): DialogElement = copy(options = Some(options.getOrElse(List()) :+ element))

  def asDialogOptionGroup(dialogOptionGroup: DialogOptionGroup): DialogElement = copy(`type` = "select", option_group = Some(Seq(dialogOptionGroup)))
}

case class DialogOption(label: String,
  value: String)

case class DialogOptionGroup(label: String,
  options: Seq[DialogOption] = List()
) {
  def addOption(label: String, value: String): DialogOptionGroup = copy(options = options :+ DialogOption(label, value))

  def addOption(option: DialogOption): DialogOptionGroup = copy(options = options :+ option)
}

object DialogOption {
  implicit val dialogOptionFormat = Json.format[DialogOption]
}

object DialogOptionGroup {
  implicit val dialogOptionGroupFormat = Json.format[DialogOptionGroup]
}

object DialogElement {
  implicit val dialogElementFormat = Json.format[DialogElement]

  def apply(label: String,
    name: String): DialogElement = new DialogElement(label, name, "text")
}

object Dialog {
  implicit val dialogFormat = Json.format[Dialog]

  def apply(title: String,
    callback_id: String
  ): Dialog = new Dialog(title, callback_id, List())
}

object DialogOption {
  implicit val dialogOptionFormat = Json.format[DialogOption]
}

object DialogOptionGroup {
  implicit val dialogOptionGroupFormat = Json.format[DialogOptionGroup]
}