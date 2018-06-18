import akka.actor.ActorSystem
import app.SlackClient
import models._
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ClientTest extends FunSuite {
  implicit val system = ActorSystem("slack")

  test("Slack client should send a message to a channel") {
    val token = system.settings.config.getString("test.token")
    val channel = system.settings.config.getString("test.channel")

    println(token)
    println(channel)

    val client = new SlackClient(token)

    assert(Await.result(client.postMessage(channel, "This is a message"), Duration.create(20, "s")).status == 200)
  }

  test("Get payload from string") {
    val actionsField: ActionsField = ActionsField("channels_list", "select", Seq(SelectedOption("CB892TULE")))
    val teamField: TeamField = TeamField("TB6N9RP9U", "testbotratp")
    val channelField: NameField = NameField("DB6CTE1C4", "directmessage")
    val userField: NameField = NameField("UB6NJCXV4", "kevin.istria")
    val actionField: ActionField = ActionField("channels_list", "Which channel changed your life this week?", "select", Some("1"), None, None, None, Some("channels"))
    val attachmentField = AttachmentField("Upgrade your Slack client to use messages like these.", "select_simple_1234", Seq(actionField), None, None, Some(1), Some("3AA3E3"))
    val originalMessage: Message = Message(Some("It's time to nominate the channel of the week"), Some(Seq(attachmentField)), None, None, Some("BB6PGS69K"), None, Some("message"), Some("UB5UAJ3QQ"), None, Some("1529179457.000037"))
    val payloadModel: Payload = Payload("interactive_message", Seq(actionsField), "select_simple_1234", teamField, channelField, userField, "1529179458.902979",
      "1529179457.000037", "1", "16OrJuAdtPKF5eygKkjOIZxx", false, originalMessage, "https://hooks.slack.com/actions/TB6N9RP9U/382786945779/hWJvPj6HxTSS2g0W5N02cNN3", "382190469344.380757873334.33143879afb834e456aabe7231562727")
    val payload =
      """
        |{
        |	"type": "interactive_message",
        |	"actions": [{
        |		"name": "channels_list",
        |		"type": "select",
        |		"selected_options": [{
        |			"value": "CB892TULE"
        |		}]
        |	}],
        |	"callback_id": "select_simple_1234",
        |	"team": {
        |		"id": "TB6N9RP9U",
        |		"domain": "testbotratp"
        |	},
        |	"channel": {
        |		"id": "DB6CTE1C4",
        |		"name": "directmessage"
        |	},
        |	"user": {
        |		"id": "UB6NJCXV4",
        |		"name": "kevin.istria"
        |	},
        |	"action_ts": "1529179458.902979",
        |	"message_ts": "1529179457.000037",
        |	"attachment_id": "1",
        |	"token": "16OrJuAdtPKF5eygKkjOIZxx",
        |	"is_app_unfurl": false,
        |	"original_message": {
        |		"type": "message",
        |		"user": "UB5UAJ3QQ",
        |		"text": "It's time to nominate the channel of the week",
        |		"bot_id": "BB6PGS69K",
        |		"attachments": [{
        |			"callback_id": "select_simple_1234",
        |			"fallback": "Upgrade your Slack client to use messages like these.",
        |			"id": 1,
        |			"color": "3AA3E3",
        |			"actions": [{
        |				"id": "1",
        |				"name": "channels_list",
        |				"text": "Which channel changed your life this week?",
        |				"type": "select",
        |				"data_source": "channels"
        |			}]
        |		}],
        |		"ts": "1529179457.000037"
        |	},
        |	"response_url": "https://hooks.slack.com/actions/TB6N9RP9U/382786945779/hWJvPj6HxTSS2g0W5N02cNN3",
        |	"trigger_id": "382190469344.380757873334.33143879afb834e456aabe7231562727"
        |}
      """.stripMargin

    val payloadFromJson: Payload = Payload.getPayload(payload)
    assert(payloadFromJson == payloadModel)
  }

}