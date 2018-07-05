package com.lunatech.slack.client.api

import com.lunatech.slack.client.models._
import org.scalatest.FlatSpec
import play.api.libs.json._
import org.scalatest.Matchers._

class MessageTest extends FlatSpec {

  it should "build an correct attachment json" in {
    val attachment = AttachmentField(fallback = "fallback", callback_id = "callbackId")
      .withPretext("Attachment pretext")
      .withAuthorName("Author name")
      .withTitle("This is the attachment titles")
      .addField(Field(title = "Field 1", value = "This is short").asShort)
      .addField(Field(title = "Field 2", value = "This is also short").asShort)
      .addField(Field(title = "Field 3", value = "This is not short"))
      .withFooter("Footer information")
      .addAction(Button(name = "Button", text = "Button"))
      .addAction(Button(name = "Button", text = "Button"))

    val json =
      """
        |{
        |   "fallback" : "fallback",
        |   "callback_id" : "callbackId",
        |   "pretext" : "Attachment pretext",
        |   "author_name" : "Author name",
        |   "title" : "This is the attachment titles",
        |   "fields" : [
        |     {
        |       "title":"Field 1",
        |       "value":"This is short",
        |       "short":true
        |     },
        |     {
        |       "title":"Field 2",
        |       "value":"This is also short",
        |       "short":true
        |     },
        |     {
        |       "title":"Field 3",
        |       "value":"This is not short",
        |       "short":false
        |     }
        |   ],
        |   "footer" : "Footer information",
        |   "actions" : [
        |     {
        |       "type": "button",
        |       "name": "Button",
        |       "text": "Button"
        |     },
        |     {
        |       "type": "button",
        |       "name": "Button",
        |       "text": "Button"
        |     }
        |   ]
        |}
      """.stripMargin

    val attachmentJson = Json.toJson(attachment)
    val jsonToVerify = Json.parse(json)

    attachmentJson shouldBe jsonToVerify
  }

  it should "build a correct ChatMessage json" in {
    val message = ChatMessage(channel = "#General", text = "Hello, world!")
      .addAttachment(AttachmentField(fallback = "fallback", callback_id = "callback 1"))
      .addAttachment(AttachmentField(fallback = "fallback", callback_id = "callback 2"))

    val expectedJson =
      """
        |{
        |   "text" : "Hello, world!",
        |   "channel" : "#General",
        |   "attachments" : [
        |     {
        |         "fallback" : "fallback",
        |         "callback_id" : "callback 1"
        |     },
        |     {
        |         "fallback" : "fallback",
        |         "callback_id" : "callback 2"
        |     }
        |   ]
        |}
      """.stripMargin

    Json.toJson(message) shouldBe Json.parse(expectedJson)
  }
}
