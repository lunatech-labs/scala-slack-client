package com.lunatech.slack.client.models

case class ActionField(
  name: String,
  text: String,
  `type`: String = "button",
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

  def withConfirmation(confirmField: ConfirmField): ActionField = copy(confirm = Some(confirmField))

  def withConfirmation(text: String, title: Option[String] = None, ok_text: Option[String] = None, dismiss_text: Option[String] = None): ActionField = {
    withConfirmation(ConfirmField(text, title, ok_text, dismiss_text))
  }

  def withId(id: String): ActionField = copy(id = Some(id))

  def withValue(value: String): ActionField = copy(value = Some(value))

  def asDefaultButton: ActionField = copy(style = Some("default"), `type` = "button")

  def asPrimaryButton: ActionField = copy(style = Some("primary"), `type` = "button")

  def asDangerButton: ActionField = copy(style = Some("danger"), `type` = "button")

  def asMenu(fields: Seq[BasicField], selectedOptions: Option[Seq[OptionField]] = None): ActionField = copy(`type` = "select", options = Some(fields), selected_options = selectedOptions)

  def asMenuWithGroupOption(fields: Seq[OptionGroupsField], selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", option_groups = Some(fields), selected_options = selectedOptions)
  }

  def asChannelMenu(selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("channels"), selected_options = selectedOptions)
  }

  def asUserMenu(selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("users"), selected_options = selectedOptions)
  }

  def asConversationMenu(selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("conversation"), selected_options = selectedOptions)
  }

  def asExternalMenu(minQueryLength: Option[Int] = None, selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("external"), selected_options = selectedOptions, min_query_length = minQueryLength)
  }
}

object ActionField {
  def apply(
    name: String,
    text: String,
  ): ActionField =
    new ActionField(name, text)
}