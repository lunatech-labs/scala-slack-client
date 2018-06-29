package slackclient

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.lunatech.slack.client.api.SlackClient
import com.lunatech.slack.client.models.{AttachmentResponse, ChatMessage, EmbeddedMessageResponse, MessageResponse}
import com.lunatech.slack.client.services.SlackCaller
import org.mockito.Mockito._
import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import play.api.libs.json._
import play.api.libs.ws.StandaloneWSResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Failure


class PostMessage extends FlatSpec with MockitoSugar {

  implicit val system: ActorSystem = ActorSystem("slack")
  implicit val materializer: Materializer = ActorMaterializer()

  "postMessage" should "return ResponseMessage when called succesfully" in {
    // Mocking
    val slackCaller = mock[SlackCaller]
    val response = mock[StandaloneWSResponse]
    val message = ChatMessage("#general", "Here's a message for you")

    when(response.body) thenReturn postMessageResponse
    when(slackCaller
      .makeApiCall(
        "token",
        "https://slack.com/api/chat.postMessage",
        Json.toJson(message))) thenReturn Future.successful(response)

    // Calling the method to test
    val client = SlackClient("token", slackCaller)
    val result = Await.result(client.postMessage(message), Duration.create("1s"))

    //result shouldBe expectedResult

    // Assertions
    result shouldBe an[MessageResponse]
    result.channel shouldBe Some("C1H9RESGL")
    result.ts shouldBe Some("1503435956.000247")

    val embeddedMessageResponse = result.message.orNull

    embeddedMessageResponse shouldBe an[EmbeddedMessageResponse]
    embeddedMessageResponse.text shouldBe Some("Here's a message for you")
    embeddedMessageResponse.username shouldBe Some("ecto1")
    embeddedMessageResponse.bot_id shouldBe Some("B19LU7CSY")
    embeddedMessageResponse.`type` shouldBe Some("message")
    embeddedMessageResponse.subtype shouldBe Some("bot_message")
    embeddedMessageResponse.ts shouldBe Some("1503435956.000247")

    val responseAttachment = embeddedMessageResponse.attachments.orNull

    responseAttachment shouldBe an[Seq[AttachmentResponse]]
    responseAttachment.length shouldBe 1
    responseAttachment.head.text shouldBe Some("This is an attachment")
    responseAttachment.head.id shouldBe Some(1)
    responseAttachment.head.fallback shouldBe Some("This is an attachment's fallback")
  }

  "postMessage" should "return an error when the call failed" in {
    val slackCaller = mock[SlackCaller]
    val response = mock[StandaloneWSResponse]
    val message = ChatMessage("#general", "Here's a message for you")

    // Mocking
    when(response.body) thenReturn
      """{
        |"ok": false,
        |"error": "an error message"
        |}
      """.stripMargin
    when(slackCaller
      .makeApiCall(
        "token",
        "https://slack.com/api/chat.postMessage",
        Json.toJson(message))) thenReturn Future.successful(response)

    // Calling the method to test
    val client = SlackClient("token", slackCaller)
    val result = client.postMessage(message)

    result.onComplete { res =>
      res.isFailure shouldBe true
      res match {
        case Failure(r) => r.getMessage shouldBe "Error: an error message"
        case _ => fail
      }
    }
  }

  private def postMessageResponse =
    """
      |{
      |    "ok": true,
      |    "channel": "C1H9RESGL",
      |    "ts": "1503435956.000247",
      |    "message": {
      |        "text": "Here's a message for you",
      |        "username": "ecto1",
      |        "bot_id": "B19LU7CSY",
      |        "attachments": [
      |            {
      |                "text": "This is an attachment",
      |                "id": 1,
      |                "fallback": "This is an attachment's fallback"
      |            }
      |        ],
      |        "type": "message",
      |        "subtype": "bot_message",
      |        "ts": "1503435956.000247"
      |    }
      |}
    """.stripMargin
}
