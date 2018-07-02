package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.MessageTs
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.concurrent.ExecutionContext.Implicits.global

class PostEphemeralTest extends FlatSpec {

  it should "parse json to MessageTs" in {
    val messageTs = MessageTs("1502210682.580145")
    val response = SlackClient.jsonToClass[MessageTs](json)

    response.isSuccess shouldBe true
    response.get shouldBe messageTs
  }

  it should "return an error" in {
    val response = SlackClient.jsonToClass[MessageTs](error)

    response.isFailure shouldBe true
    response.failed.get.getMessage shouldBe "Error: user_not_in_channel"
  }

  val error =
    """
      |{
      |    "ok": false,
      |    "error": "user_not_in_channel"
      |}
    """.stripMargin

  val json =
    """
      | {
      |    "ok": true,
      |    "message_ts": "1502210682.580145"
      |}
      |
    """.stripMargin
}
