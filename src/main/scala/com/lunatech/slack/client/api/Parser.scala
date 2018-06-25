package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.Payload
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

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

object Parser {
  def slashCommand(body: String): Try[SlashCommandPayload] = {
    val bodyMap = body.split('&')
      .map(value => value.split('='))
      .filter(token => token.length == 2)
      .map(token => token(0) -> token(1))
      .toMap

    slashCommand(bodyMap)
  }

  def slashCommand(body: Map[String, Any]): Try[SlashCommandPayload] = {

    implicit val slashCommandPayloadFormat = Json.format[SlashCommandPayload]

    val mapString: Map[String, String] = body.map {
      case (k, v) => v match {
        case v: String => k -> v
        case v: Seq[String] => k -> v.headOption.getOrElse("")
        case _ => k -> ""
      }
    }

    Json.fromJson[SlashCommandPayload](Json.toJson(mapString))
      .map(payload => Success(payload))
      .getOrElse(Failure(new Exception("The argument is not a slash command payload")))
  }

  def getPayload(body: String): Try[Payload] = {
    val bodyMap = body.split('&')
      .map(value => value.split('='))
      .filter(token => token.length == 2)
      .map(token => token(0) -> token(1))
      .toMap

    getPayload(bodyMap)
  }

  def getPayload(body: Map[String, Any]) = {
    val mapString: Map[String, String] = body.map {
      case (k, v) => v match {
        case v: String => k -> v
        case v: Seq[String] => k -> v.headOption.getOrElse("")
        case _ => k -> ""
      }
    }

    val payloadString = mapString.getOrElse("payload", "")

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
