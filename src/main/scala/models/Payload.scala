package models

import play.api.libs.json._

import scala.util.{Failure, Success, Try}

case class Payload(
                    `type`: String,
                    actions: Option[Seq[ActionsField]],
                    submission: Option[Map[String, String]],
                    callback_id: String,
                    team: TeamField,
                    channel: NameField,
                    user: NameField,
                    action_ts: String,
                    message_ts: Option[String],
                    attachment_id: Option[String],
                    token: String,
                    is_app_unfurl: Option[Boolean],
                    original_message: Option[Message],
                    response_url: String,
                    trigger_id: Option[String],
                  )

case class Message(
                    text: Option[String] = None,
                    attachments: Option[Seq[AttachmentField]],
                    channel: Option[String] = None,
                    thread_ts: Option[String] = None,
                    bot_id: Option[String] = None,
                    response_type: Option[String] = None,
                    `type`: Option[String] = None,
                    user: Option[String] = None,
                    subtype: Option[String] = None,
                    ts: Option[String] = None,
                    replace_original: Option[Boolean] = None,
                    delete_original: Option[Boolean] = None
                  )

object Payload {
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

    if(payloadString.isEmpty) {
      Failure(new Exception("No payload"))
    } else {
      Json.fromJson[Payload](Json.parse(payloadString)) match {
        case JsSuccess(json, _) => Success(json)
        case JsError(e) => Failure(new Exception(s"Invalid Payload : $e"))
      }
    }


  }
}