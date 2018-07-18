package com.lunatech.slack.client.models

import play.api.libs.json._


trait Menu extends ActionField {
  override val `type`: String = "select"
  val selected_options: Option[Seq[BasicField]]
}

object Menu {
  implicit val reads: Reads[Menu] = json =>
    (json \ "type").validate[String] match {
      case JsSuccess("select", _) => (json \ "data_source").validate[String] match {
        case JsSuccess(value, _) if value != "static" => DynamicMenu.reads.reads(json)
        // The field data_source is not present so it must be either a StaticMenu or a StaticGroupMeu
        case JsError(_) => (json \ "options").validate[Seq[BasicField]] match {
          case JsSuccess(_, _) => StaticMenu.reads.reads(json)
          case JsError(_) => (json \ "option_groups").validate[Seq[OptionGroupsField]] match {
            case JsSuccess(_, _) => StaticGroupMenu.reads.reads(json)
            case x: JsError => x
          }
        }
      }
      case x: JsError => x
      case _ => JsError("Unknown menu type")
    }
}
