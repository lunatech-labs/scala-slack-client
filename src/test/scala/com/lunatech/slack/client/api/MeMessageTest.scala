package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.ChatResponse
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class MeMessageTest extends FlatSpec {

  it should "parse json to ChatResponse" in {
    val chatResponse = ChatResponse(Some("C024BE7LR"), Some("1417671948.000006"))
    val response = SlackClient.jsonToClass[ChatResponse](json)

    response.isSuccess shouldBe true
    response.get shouldBe chatResponse
  }

  it should "return an error" in {
    val response = SlackClient.jsonToClass[ChatResponse](error)

    response.isFailure shouldBe true
    response.failed.get.getMessage shouldBe "Error: invalid_auth"
  }

  val error =
    """
      |{
      |    "ok": false,
      |    "error": "invalid_auth"
      |}
    """.stripMargin

  val json =
    """
      |{
      |    "ok": true,
      |    "channel": "C024BE7LR",
      |    "ts": "1417671948.000006"
      |}
      |
    """.stripMargin
}
