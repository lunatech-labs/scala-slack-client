package com.lunatech.slack.client.api

import com.lunatech.slack.client.models.{DialogElement, DialogOption}
import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import play.api.libs.json.Json

class DialogElementBuilderTest extends FlatSpec {
  it should "construct a correctly DialogElement" in {
    Json.toJson(dialogElement) shouldBe Json.parse(json)
  }

  private val dialogElement = DialogElement("Pickup Location", "loc_origin")
    .asEmail
    .addOption(DialogOption("lokiju", "pouhy"))
    .withHint("dsdw")
    .withMaxLength(50)
    .withMinLength(10)
    .withPlaceholder("hello")
    .withValue("it's a trap")

  private val json =
    """
      | {
      |   "label":"Pickup Location",
      |   "name":"loc_origin",
      |   "type":"text",
      |   "max_length":50,
      |   "min_length":10,
      |   "hint":"dsdw",
      |   "subtype":"email",
      |   "value":"it's a trap",
      |   "placeholder":"hello",
      |   "options":[{"label":"lokiju","value":"pouhy"}]}
    """.stripMargin
}
