package com.lunatech.slack.client.api

import com.lunatech.slack.client.Parser
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class SlashCommandParserTest extends FlatSpec {
  it should "parse map to SlashCommandPayload" in {
    val res = Parser.slashCommand(map)
    res.isSuccess shouldBe true
    res.get.token shouldBe "token"
    res.get.team_id.get shouldBe "TB6N9RP9U"
    res.get.team_domain.get shouldBe "testdomain"
    res.get.channel_id shouldBe "DB59Q4M7B"
    res.get.channel_name shouldBe "directmessage"
    res.get.user_id shouldBe "UB6NJCXV4"
    res.get.user_name shouldBe "username"
    res.get.command shouldBe "/test"
    res.get.text shouldBe None
    res.get.response_url shouldBe "urltest"
    res.get.trigger_id shouldBe "triggertest"
  }
  private val map = Map("token" -> Seq("token"),
    "team_id" -> Seq("TB6N9RP9U"),
    "team_domain" -> Seq("testdomain"),
    "channel_id" -> Seq("DB59Q4M7B"),
    "channel_name" -> Seq("directmessage"),
    "user_id" -> Seq("UB6NJCXV4"),
    "user_name" -> Seq("username"),
    "command" -> Seq("/test"),
    "text" -> Seq(),
    "response_url" -> Seq("urltest"),
    "trigger_id" -> Seq("triggertest")
  )
}
