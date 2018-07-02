package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class MessageTs(message_ts: String)

object MessageTs {
  implicit val messageTsFormat = Json.format[MessageTs]
}