package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class PermaLink(channel: Option[String] = None, permaLink: Option[String] = None)

object PermaLink {
  implicit val permaLinkFormat = Json.format[PermaLink]
}