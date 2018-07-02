package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.ChatResponse
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.concurrent.ExecutionContext.Implicits.global

class DeleteMessageTest extends FlatSpec {

  it should "parse json to ChatResponse" in {
    val chatResponse = ChatResponse(Some("C024BE91L"), Some("1401383885.000061"))
    val response = SlackClient.jsonToClass[ChatResponse](json)

    response.isSuccess shouldBe true
    response.get shouldBe chatResponse
  }

  it should "return an error" in {
    val response = SlackClient.jsonToClass[ChatResponse](error)

    response.isFailure shouldBe true
    response.failed.get.getMessage shouldBe "Error: message_not_found"
  }

  val error =
    """
      |{
      |    "error": "message_not_found",
      |    "ok": false
      |}
    """.stripMargin

  val json =
    """
      |{
      |    "ok": true,
      |    "channel": "C024BE91L",
      |    "ts": "1401383885.000061"
      |}
      |
    """.stripMargin
}
