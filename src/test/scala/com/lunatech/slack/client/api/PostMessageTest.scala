package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.{AttachmentResponse, EmbeddedMessageResponse, MessageResponse}
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.concurrent.ExecutionContext.Implicits.global

class PostMessageTest extends FlatSpec {

  it should "parse json to MessageResponse" in {
    val attachment = AttachmentResponse(Some("This is an attachment"), Some(1), Some("This is an attachment's fallback"))
    val embeddedMessageResponse = EmbeddedMessageResponse(Some("Here's a message for you"), Some("ecto1"), Some("B19LU7CSY"),
      Some(Seq(attachment)), Some("message"), Some("bot_message"), Some("1503435956.000247"))

    val messageResponse = MessageResponse(Some("C1H9RESGL"), Some("1503435956.000247"), Some(embeddedMessageResponse))
    val response = SlackClient.jsonToClass[MessageResponse](json)

    response.isSuccess shouldBe true
    response.get shouldBe messageResponse
  }

  it should "return an error" in {
    val response = SlackClient.jsonToClass[MessageResponse](error)

    response.isFailure shouldBe true
    response.failed.get.getMessage shouldBe "Error: too_many_attachments"
  }

  val error =
    """
      |{
      |    "ok": false,
      |    "error": "too_many_attachments"
      |}
    """.stripMargin

  val json =
    """
      | {
      |    "ok": true,
      |    "channel": "C1H9RESGL",
      |    "ts": "1503435956.000247",
      |    "message": {
      |        "text": "Here's a message for you",
      |        "username": "ecto1",
      |        "bot_id": "B19LU7CSY",
      |        "attachments": [
      |            {
      |                "text": "This is an attachment",
      |                "id": 1,
      |                "fallback": "This is an attachment's fallback"
      |            }
      |        ],
      |        "type": "message",
      |        "subtype": "bot_message",
      |        "ts": "1503435956.000247"
      |    }
      |}
      |
    """.stripMargin
}
