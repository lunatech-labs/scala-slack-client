import akka.actor.ActorSystem
import app.SlackClient
import models._
import org.scalatest.FunSuite
import play.api.libs.json._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Success, Try}

class ClientTest extends FunSuite {
  implicit val system = ActorSystem("slack")

  private val token = system.settings.config.getString("test.token")
  private val channel = system.settings.config.getString("test.channel")
  private val userId = system.settings.config.getString("test.userId")
  private val client = new SlackClient(token)

  test("Slack client should send a message to a channel") {
    val response = Await.result(client.postMessage(channel, "This is a message"), Duration.create(20, "s"))
    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should send an ephemeral message to a channel") {
    val response = Await.result(client.postEphemeral(channel, "This is an ephemeral message", userId), Duration.create(20, "s"))
    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should send a message to a channel with buttons") {
    val attachment = AttachmentField("upgrade your client api", "action_test")
      .addAction(ActionField("Default button", "Default"))
      .addAction(ActionField("Primary button", "Primary").asPrimaryButton)
      .addAction(ActionField("Danger button", "Danger").asDangerButton.withConfirmation("Are you sure ?"))

    val response = Await.result(
      client.postMessage(channel, "This is a message with buttons", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  val fields: List[BasicField] = List(
    BasicField("First item", "First item"),
    BasicField("Second Item", "Second Item"),
  )

  test("Slack client should send a message to a channel with a menu") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      List(ActionField("Menu", "The menu").asMenu(fields).withConfirmation("Are you sure ?"))
    )

    val response = Await.result(
      client.postMessage(channel, "This is a message with a menu", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should send a message to a channel with a menu listing users") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      List(ActionField("User menu", "Users").asUserMenu())
    )

    val response = Await.result(
      client.postMessage(channel, "This is a message with a menu listing users", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should send a message to a channel with a menu listing channels") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      List(ActionField("Channels menu", "Channels").asChannelMenu())
    )

    val response = Await.result(
      client.postMessage(channel, "This is a message with a menu listing channels", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should send a message to a channel with a menu listing conversations") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      List(ActionField("Channels menu", "Converations").asConversationMenu())
    )

    val response = Await.result(
      client.postMessage(channel, "This is a message with a menu listing converations", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )
    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should delete a message") {
    val sendMessage = Await.result(client.postMessage(channel, "This message should be deleted"), Duration.create(20, "s"))
    val ts = (Json.parse(sendMessage.body) \ "ts").validate[String].getOrElse("")
    val response = Await.result(client.deleteMessage(channel, ts), Duration.create(20, "s"))

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should update a message") {
    val sendMessage = Await.result(client.postMessage(channel, "This message should be updated"), Duration.create(20, "s"))
    val ts = (Json.parse(sendMessage.body) \ "ts").validate[String].getOrElse("")
    val response = Await.result(client.updateMessage(channel, "Message has been updated", ts), Duration.create(20, "s"))

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should return a list of all channels") {
    val response = Await.result(client.channelList(), Duration.create(20, "s"))

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Slack client should send a meMessage") {
    val response = Await.result(client.meMessage(channel, "This is a meMessage"), Duration.create(20, "s"))

    assert(response.status == 200)
    assert((Json.parse(response.body) \ "ok").validate[Boolean].getOrElse(false))
  }

  test("Get payload from string") {
    val actionsField: ActionsField = ActionsField("channels_list", "select", Seq(SelectedOption("CB892TULE")))
    val teamField: TeamField = TeamField("TB6N9RP9U", "testbotratp")
    val channelField: NameField = NameField("DB6CTE1C4", "directmessage")
    val userField: NameField = NameField("usertoken", "username")
    val actionField: ActionField = ActionField("channels_list", "Which channel changed your life this week?", "select", Some("1"), None, None, None, Some("channels"))
    val attachmentField = AttachmentField("Upgrade your Slack client to use messages like these.", "select_simple_1234", Seq(actionField), None, None, Some(1), Some("3AA3E3"))
    val originalMessage: Message = Message(Some("It's time to nominate the channel of the week"), Some(Seq(attachmentField)), None, None, Some("BB6PGS69K"), None, Some("message"), Some("UB5UAJ3QQ"), None, Some("1529179457.000037"))
    val payloadModel: Payload = Payload("interactive_message", Seq(actionsField), "select_simple_1234", teamField, channelField, userField, "1529179458.902979",
      "1529179457.000037", "1", "16OrJuAdtPKF5eygKkjOIZxx", false, originalMessage, "https://hooks.slack.com/actions/TB6NDSQFD", "382190469344.380757873334.331")
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
        |		"id": "usertoken",
        |		"name": "username"
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
        |	"response_url": "https://hooks.slack.com/actions/TB6NDSQFD",
        |	"trigger_id": "382190469344.380757873334.331"
        |}
      """.stripMargin

    val payloadFromJson: Try[Payload] = Payload.getPayload(payload)
    assert(payloadFromJson.isSuccess)
    assert(payloadFromJson.get == payloadModel)
  }

}