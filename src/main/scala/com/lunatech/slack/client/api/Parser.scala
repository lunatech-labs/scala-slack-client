package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.Payload
import play.api.libs.json._

import scala.util.{Failure, Success}

case class SlashCommandPayload(token: Option[String],
  team_id: Option[String],
  team_domain: Option[String],
  enterprise_id: Option[String],
  enterprise_name: Option[String],
  channel_id: Option[String],
  channel_name: Option[String],
  user_id: Option[String],
  user_name: Option[String],
  command: Option[String],
  text: Option[String],
  response_url: Option[String],
  trigger_id: Option[String])

object SlashCommandPayload {
  implicit val slashCommandPayloadFormat = Json.format[SlashCommandPayload]

  def from(body: Map[String, String]): SlashCommandPayload =
    SlashCommandPayload(token = body.get("token"),
      team_id = body.get("team_id"),
      team_domain = body.get("team_domain"),
      enterprise_id = body.get("enterprise_id"),
      enterprise_name = body.get("enterprise_name"),
      channel_id = body.get("channel_id"),
      channel_name = body.get("channel_name"),
      user_id = body.get("user_id"),
      user_name = body.get("user_name"),
      command = body.get("command"),
      text = body.get("text"),
      response_url = body.get("response_url"),
      trigger_id = body.get("trigger_id")
    )
}

object Parser {
  def slashCommand(body: Map[String, Seq[String]]): SlashCommandPayload = {

    val m = body.collect {
      case (k, v) if v.length == 1 => k -> v.head
    }

    SlashCommandPayload.from(m)
  }

  def getPayload(body: Map[String, Seq[String]]) = {
    val m = body.collect {
      case (k, v) if v.length == 1 => k -> v.head
    }

    val payloadString = m.getOrElse("payload", "")

    if (payloadString.isEmpty) {
      Failure(new Exception("No payload"))
    } else {
      Json.fromJson[Payload](Json.parse(payloadString)) match {
        case JsSuccess(json, _) => Success(json)
        case JsError(e) => Failure(new Exception(s"Invalid Payload : $e"))
      }
    }
  }
}
