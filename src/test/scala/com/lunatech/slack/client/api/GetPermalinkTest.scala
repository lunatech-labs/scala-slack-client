package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.Permalink
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.concurrent.ExecutionContext.Implicits.global

class GetPermalinkTest extends FlatSpec {

  it should "parse json to Permalink" in {
    val permaLink = Permalink(Some("43HDSD4334"), Some("https://ghostbusters.slack.com/archives/C1H9RESGA/p135854651500008"))
    val response = SlackClient.jsonToClass[Permalink](json)

    response.isSuccess shouldBe true
    response.get shouldBe permaLink
  }

  it should "return an error" in {
    val response = SlackClient.jsonToClass[Permalink](error)

    response.isFailure shouldBe true
    response.failed.get.getMessage shouldBe "Error: channel_not_found"
  }

  val error =
    """
      |{
      |    "ok": false,
      |    "error": "channel_not_found"
      |}
    """.stripMargin

  val json =
    """
      | {
      |   "ok": true,
      |   "channel": "43HDSD4334",
      |   "permalink": "https://ghostbusters.slack.com/archives/C1H9RESGA/p135854651500008"
      | }
      |
    """.stripMargin
}
