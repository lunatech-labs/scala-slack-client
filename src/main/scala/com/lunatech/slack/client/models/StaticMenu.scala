package com.lunatech.slack.client.models

import play.api.libs.json._

case class StaticMenu(
  name: String,
  text: String,
  confirm: Option[ConfirmField] = None,
  id: Option[String] = None,
  options: Option[Seq[BasicField]] = None,
  selected_options: Option[Seq[BasicField]] = None
) extends Menu {

  def withConfirmation(confirmField: ConfirmField): StaticMenu = copy(confirm = Some(confirmField))

  def withId(id: String): StaticMenu = copy(id = Some(id))

  def addOption(text: String, value: String, description: Option[String] = None, select: Boolean = false): StaticMenu = {
    val field = BasicField(text, value, description)

    if (select) {
      copy(selected_options = Some(selected_options.getOrElse(Seq()) :+ field)).addOption(text, value, description)
    } else {
      copy(options = Some(options.getOrElse(Seq()) :+ field))
    }
  }
}

object StaticMenu {
  implicit val writes: Writes[StaticMenu] = menu =>
    Json.format[StaticMenu].writes(menu) ++
      Json.obj("type" -> menu.`type`)

  implicit val reads: Reads[StaticMenu] = Json.reads[StaticMenu]
}
