package slackclient

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.lunatech.slack.client.api.SlackClient
import com.lunatech.slack.client.models.{ChatEphemeral, ChatMessage, MessageResponse}
import com.lunatech.slack.client.services.SlackCaller
import org.scalatest.FlatSpec
import org.scalatest.mockito.MockitoSugar
import play.api.libs.ws.StandaloneWSResponse
import org.mockito.Mockito._
import play.api.libs.json.Json
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


class PostEphemeral extends FlatSpec with MockitoSugar {

  implicit val system: ActorSystem = ActorSystem("slack")
  implicit val materializer: Materializer = ActorMaterializer()

  it should "send a correct response when called succesfully" in {
    // Mocking
    val slackCaller = mock[SlackCaller]
    val response = mock[StandaloneWSResponse]
    val message = ChatEphemeral(channel = "C1234567890", text = "Hello world", user = "U0BPQUNTA")

    when(response.body) thenReturn
      """
        |{
        |    "ok": true,
        |    "message_ts": "1502210682.580145"
        |}
      """.stripMargin

    when(slackCaller
      .makeApiCall(
        "token",
        "https://slack.com/api/chat.postEphemeral",
        Json.toJson(message))) thenReturn Future.successful(response)

    // Calling the method to test
    val client = SlackClient(token = "token", slackCaller = slackCaller)
    val clientResponse = client.postEphemeral(message)

    ScalaFutures.whenReady(clientResponse){result =>
      result shouldBe an[String]
      result shouldBe "1502210682.580145"
    }
  }

  it should "return an error when api send an error in the body response" in {
    val slackCaller = mock[SlackCaller]
    val response = mock[StandaloneWSResponse]
    val message = ChatEphemeral(channel = "C1234567890", text = "Hello world", user = "U0BPQUNTA")

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
        "https://slack.com/api/chat.postEphemeral",
        Json.toJson(message))) thenReturn Future.successful(response)

    // Calling the method to test
    val client = SlackClient("token", slackCaller)
    val result = client.postEphemeral(message)

    ScalaFutures.whenReady(result.failed) { failure =>
      failure.getMessage shouldBe "Error: an error message"
    }
  }

  it should "say return an error saying that the response body is not what expected" in {
    val slackCaller = mock[SlackCaller]
    val response = mock[StandaloneWSResponse]
    val message = ChatEphemeral(channel = "C1234567890", text = "Hello world", user = "U0BPQUNTA")

    // Mocking
    when(response.body) thenReturn "{\"This is\": \"an incorrect body response\"}"
    when(slackCaller
      .makeApiCall(
        "token",
        "https://slack.com/api/chat.postEphemeral",
        Json.toJson(message))) thenReturn Future.successful(response)

    // Calling the method to test
    val client = SlackClient("token", slackCaller)
    val result = client.postEphemeral(message)

    ScalaFutures.whenReady(result.failed)(
      res => res.getMessage shouldBe "This is not the response expected : {\"This is\": \"an incorrect body response\"}"
    )
  }

}
