package com.lunatech.slack.client.models

import play.api.libs.json._

trait ActionField {
  val name: String
  val text: String
  val `type`: String
  val confirm: Option[ConfirmField]
  val id: Option[String]
}

object ActionField {
  implicit val reads: Reads[ActionField] = (json: JsValue) => {
    (json \ "type").validate[String] match {
      case JsSuccess("button", _) => Button.reads.reads(json)
      case JsSuccess("select", _) => Menu.reads.reads(json)
      case x: JsError => x
      case _ => JsError("Unknown action")
    }
  }

  implicit val writes: Writes[ActionField] = {
    case x: Button => Button.writes.writes(x)
    case x: DynamicMenu => DynamicMenu.writes.writes(x)
    case x: StaticMenu => StaticMenu.writes.writes(x)
    case x: StaticGroupMenu => StaticGroupMenu.writes.writes(x)
  }

}