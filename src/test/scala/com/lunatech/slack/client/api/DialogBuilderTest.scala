package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.{Dialog, DialogElement}
import org.scalatest.FlatSpec
import play.api.libs.json.Json
import org.scalatest.Matchers._

class DialogBuilderTest extends FlatSpec {
  it should "construct a correctly Dialog" in {
    Json.toJson(dialog) shouldBe Json.parse(json)
  }

  private val dialog = Dialog("Request a Ride", "ryde-46e2b0")
    .withSubmitLabel("Request")
    .addElement(DialogElement("Pickup Location", "loc_origin"))
    .addElement(DialogElement("Dropoff Location", "loc_destination"))
    .notifyOnCancel

  private val json =
    """
      |{
      |  "title": "Request a Ride",
      |  "callback_id": "ryde-46e2b0",
      |  "submit_label": "Request",
      |  "notify_on_cancel":true,
      |  "elements": [
      |    {
      |      "type": "text",
      |      "label": "Pickup Location",
      |      "name": "loc_origin"
      |    },
      |    {
      |      "type": "text",
      |      "label": "Dropoff Location",
      |      "name": "loc_destination"
      |    }
      |  ]
      |}
    """.stripMargin
}
