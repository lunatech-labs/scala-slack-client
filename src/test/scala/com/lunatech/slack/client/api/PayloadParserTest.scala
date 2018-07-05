package com.lunatech.slack.client.api

import com.lunatech.slack.client.Parser
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class PayloadParserTest extends FlatSpec {
  it should "parse map to Payload" in {
    val payload = Parser.getPayload(map)
    payload.isSuccess shouldBe true
    payload.get.`type` shouldBe "interactive_message"
    payload.get.actions.get.head.name shouldBe "latest_request"
    payload.get.actions.get.head.`type` shouldBe "button"
    payload.get.callback_id shouldBe "callback"
    payload.get.team.id shouldBe "TB6N9RP9U"
    payload.get.team.domain shouldBe "testdomain"
    payload.get.channel.id shouldBe "DB59Q4M7B"
    payload.get.channel.name shouldBe "directmessage"
    payload.get.user.id shouldBe "userid"
    payload.get.user.name shouldBe "username"
    payload.get.action_ts shouldBe "1530776955.607309"
    payload.get.message_ts.get shouldBe "1530776952.000147"
    payload.get.attachment_id.get shouldBe "1"
    payload.get.token shouldBe "tokentest"
    payload.get.is_app_unfurl.get shouldBe false
    payload.get.response_url shouldBe "responsetest"
    payload.get.trigger_id.get shouldBe "triggertest"
  }

  val map = Map("payload" -> Seq(
    """
      |{
      | "type": "interactive_message",
      | "actions": [{"name": "latest_request", "type": "button"}],
      | "callback_id": "callback",
      | "team": {"id": "TB6N9RP9U", "domain": "testdomain"},
      | "channel": {"id": "DB59Q4M7B", "name": "directmessage"},
      | "user": {"id": "userid", "name": "username"},
      | "action_ts": "1530776955.607309",
      | "message_ts": "1530776952.000147",
      | "attachment_id": "1",
      | "token": "tokentest",
      | "is_app_unfurl": false,
      | "response_url": "responsetest",
      | "trigger_id": "triggertest"
      |}
    """.stripMargin
  ))
}
