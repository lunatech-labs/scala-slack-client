package com.lunatech.slack.client.models

import play.api.libs.json._

case class StaticGroupMenu(
  name: String,
  text: String,
  confirm: Option[ConfirmField] = None,
  id: Option[String] = None,
  option_groups: Option[Seq[OptionGroupsField]] = None,
  selected_options: Option[Seq[BasicField]] = None
) extends Menu {
  def withConfirmation(confirmField: ConfirmField): StaticGroupMenu = copy(confirm = Some(confirmField))

  def withId(id: String): StaticGroupMenu = copy(id = Some(id))

  def addOptionGroup(options: OptionGroupsField): StaticGroupMenu = copy(option_groups = Some(option_groups.getOrElse(Seq()) :+ options))

  def addSelectedOption(basicField: BasicField): StaticGroupMenu = copy(selected_options = Some(selected_options.getOrElse(Seq()) :+ basicField))
}

object StaticGroupMenu {
  implicit val writes: Writes[StaticGroupMenu] = menu =>
    Json.format[StaticGroupMenu].writes(menu) ++
      Json.obj("type" -> menu.`type`)

  implicit val reads: Reads[StaticGroupMenu] = Json.reads[StaticGroupMenu]
}
