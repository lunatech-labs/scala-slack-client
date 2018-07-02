package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class ChannelResponse(channel: ChannelId)

case class ChannelId(id: String)

object ChannelId {
  implicit val channelIdFormat = Json.format[ChannelId]
}

object ChannelResponse {
  implicit val responseChannelFormat = Json.format[ChannelResponse]
}