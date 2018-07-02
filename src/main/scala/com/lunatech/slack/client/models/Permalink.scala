package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class Permalink(channel: Option[String] = None, permalink: Option[String] = None)

object Permalink {
  implicit val permalinkFormat = Json.format[Permalink]
}