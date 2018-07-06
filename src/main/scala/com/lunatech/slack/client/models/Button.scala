package com.lunatech.slack.client.models

import play.api.libs.json._


case class Button(
  name: String,
  text: String,
  id: Option[String] = None,
  value: Option[String] = None,
  confirm: Option[ConfirmField] = None,
  style: Option[String] = None
) extends ActionField {
  val `type` = "button"

  def withConfirmation(text: String,
    title: Option[String] = None,
    ok_text: Option[String] = None,
    dismiss_text: Option[String] = None): Button = copy(confirm = Some(ConfirmField(text, title, ok_text, dismiss_text)))

  def withId(id: String): Button = copy(id = Some(id))

  def withValue(value: String): Button = copy(value = Some(value))

  def asDefaultButton: Button = copy(style = Some("default"))

  def asPrimaryButton: Button = copy(style = Some("primary"))

  def asDangerButton: Button = copy(style = Some("danger"))

}


object Button {
  implicit val reads: Reads[Button] = Json.reads[Button]

  implicit val writes: Writes[Button] = button =>
    Json.format[Button].writes(button) ++
      Json.obj("type" -> button.`type`)

}