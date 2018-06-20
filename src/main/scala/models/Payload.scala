package models

import com.fasterxml.jackson.core.JsonParseException
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

case class Payload(
                    `type`: String,
                    actions: Seq[ActionsField],
                    callback_id: String,
                    team: TeamField,
                    channel: NameField,
                    user: NameField,
                    action_ts: String,
                    message_ts: String,
                    attachment_id: String,
                    token: String,
                    is_app_unfurl: Boolean,
                    original_message: Message,
                    response_url: String,
                    trigger_id: String
                  )

object Payload {
  def getPayload(payload: String): Try[Payload] = {
    val jsonPayload: JsValue = Json.parse(payload)

    val payloadFromJson = Json.fromJson[Payload](jsonPayload)

    payloadFromJson match {
      case payload: JsSuccess[Payload] =>
        Success(payload.value)
      case error: JsError =>
        Failure(new Exception("Payload has not a correct json format"))
    }
  }
}