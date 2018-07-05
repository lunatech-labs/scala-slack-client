package com.lunatech.slack.client

import com.lunatech.slack.client.models.Payload
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

case class SlashCommandPayload(token: String,
  channel_id: String,
  channel_name: String,
  user_id: String,
  user_name: String,
  command: String,
  response_url: String,
  trigger_id: String,
  team_domain: Option[String],
  enterprise_id: Option[String],
  enterprise_name: Option[String],
  text: Option[String],
  team_id: Option[String])

object SlashCommandPayload {
  implicit val slashCommandPayloadFormat = Json.format[SlashCommandPayload]

  def from(body: Map[String, String]): Try[SlashCommandPayload] =
    Try(SlashCommandPayload(
      token = body("token"),
      team_id = body.get("team_id"),
      team_domain = body.get("team_domain"),
      enterprise_id = body.get("enterprise_id"),
      enterprise_name = body.get("enterprise_name"),
      channel_id = body("channel_id"),
      channel_name = body("channel_name"),
      user_id = body("user_id"),
      user_name = body("user_name"),
      command = body("command"),
      text = body.get("text"),
      response_url = body("response_url"),
      trigger_id = body("trigger_id")
    ))
}

object Parser {
  def slashCommand(body: Map[String, Seq[String]]): Try[SlashCommandPayload] = {

    val m = body.collect {
      case (k, v) if v.length == 1 => k -> v.head
    }

    SlashCommandPayload.from(m)
  }

  def getPayload(body: Map[String, Seq[String]]): Try[Payload] = {
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
