package com.lunatech.slack.client.api

import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import play.api.libs.json._

import scala.runtime.BoxedUnit

class OpenDialogTest extends FlatSpec {

  implicit val formatUnit: OFormat[Unit] = new OFormat[Unit] {
    def reads(js: JsValue): JsResult[Unit] = JsSuccess(())

    def writes(a: Unit): JsObject = Json.obj()
  }

  it should "parse json to Unit" in {
    val response = SlackClient.jsonToClass[Unit](json)

    response.isSuccess shouldBe true
    response.get shouldBe an[BoxedUnit]
  }

  it should "return an error" in {
    val response = SlackClient.jsonToClass[Unit](error)

    response.isFailure shouldBe true
    response.failed.get.getMessage shouldBe "Error: missing_trigger"
  }

  val error =
    """
      |{
      |    "ok": false,
      |    "error": "missing_trigger"
      |}
    """.stripMargin

  val json =
    """
      |{
      |    "ok": true
      |}
      |
    """.stripMargin
}

