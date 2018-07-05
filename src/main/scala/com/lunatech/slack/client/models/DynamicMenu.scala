package com.lunatech.slack.client.models

import com.lunatech.slack.client.models.DataSource.DataSource
import play.api.libs.json._

object DataSource extends Enumeration {
  type DataSource = Value
  val users, channels, external, conversation = Value

  implicit val read: Reads[DataSource] = Reads.enumNameReads(DataSource)
  implicit val write: Writes[DataSource] = Writes.enumNameWrites
}

case class DynamicMenu(
  name: String,
  text: String,
  data_source: DataSource,
  selected_options: Option[Seq[BasicField]] = None,
  confirm: Option[ConfirmField] = None,
  id: Option[String] = None,
  min_query_length: Option[Int] = None
) extends Menu {

  def withConfirmation(confirmField: ConfirmField): DynamicMenu = copy(confirm = Some(confirmField))

  def withId(id: String): DynamicMenu = copy(id = Some(id))

  def addSelectedOption(option: BasicField): DynamicMenu = copy(selected_options = Some(selected_options.getOrElse(Seq()) :+ option))

  def withMinimalQueryLength(min: Int): DynamicMenu = copy(min_query_length = Some(min))
}

object DynamicMenu {
  implicit val reads: Reads[DynamicMenu] = Json.reads[DynamicMenu]

  implicit val writes: Writes[DynamicMenu] = menu =>
    Json.format[DynamicMenu].writes(menu) ++
      Json.obj("type" -> menu.`type`)
}