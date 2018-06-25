import akka.actor.ActorSystem
import com.lunatech.slack.client.api.SlackClient
import com.lunatech.slack.client.models._
import org.scalatest.FunSuite
import org.scalatest.Matchers._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ClientTest extends FunSuite {
  implicit val system = ActorSystem("slack")

  private val token = system.settings.config.getString("test.token")
  private val channel = system.settings.config.getString("test.channel")
  private val userId = system.settings.config.getString("test.userId")
  private val client = new SlackClient(token)

  test("test slach command parse") {

  }

  test("Slack client should send a message to a channel") {
    val response = Await.result(client.postMessage(channel, "This is a message"), Duration.create(20, "s"))
    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel (ChatMessage)") {
    val message = ChatMessage(channel, "this is a message with ChatMessage")
      .addAttachment(AttachmentField("update your API", "toto")
        .addAction(ActionField("Button", "Primary button").asPrimaryButton)
        .addField(Field("First Field", "firstvalue", short = true))
        .addField(Field("Seconde Field", "second value", short = true))
        .addField(Field("Long field", "This is a long field"))
        .withPretext("This is the pretext")
        .withText("YOLO")
      )

    val response = Await.result(client.postMessage(message), Duration.create(20, "s"))

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send an ephemeral message to a channel") {
    val response = Await.result(client.postEphemeral(channel, "This is an ephemeral message", userId), Duration.create(20, "s"))

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel with buttons") {

    val attachment = AttachmentField("upgrade your client api", "action_test")
      .addAction(ActionField("Default button", "Default"))
      .addAction(ActionField("Primary button", "Primary").asPrimaryButton)
      .addAction(ActionField("Danger button", "Danger").asDangerButton.withConfirmation("Are you sure ?"))

    val response = Await.result(
      client.postMessage(channel, "This is a message with buttons", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )

    response shouldBe an[MessageResponse]
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

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel with a menu listing users") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      List(ActionField("User menu", "Users").asUserMenu())
    )

    val response = Await.result(
      client.postMessage(channel, "This is a message with a menu listing users", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel with a menu listing channels") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      List(ActionField("Channels menu", "Channels").asChannelMenu())
    )

    val response = Await.result(
      client.postMessage(channel, "This is a message with a menu listing channels", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel with a menu listing conversations") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      List(ActionField("Channels menu", "Converations").asConversationMenu())
    )

    val response = Await.result(
      client.postMessage(channel, "This is a message with a menu listing converations", attachments = Some(Seq(attachment))), Duration.create(20, "s")
    )
    response shouldBe an[MessageResponse]
  }

  test("Slack client should delete a message") {
    val sendMessage = Await.result(client.postMessage(channel, "This message should be deleted"), Duration.create(20, "s"))
    val ts = sendMessage.ts.getOrElse("")
    val response = Await.result(client.deleteMessage(channel, ts), Duration.create(20, "s"))

    response shouldBe an[ChatResponse]
  }

  test("Slack client should update a message") {
    val sendMessage = Await.result(client.postMessage(channel, "This message should be updated"), Duration.create(20, "s"))
    val ts = sendMessage.ts.getOrElse("")
    val response = Await.result(client.updateMessage(channel, "Message has been updated", ts), Duration.create(20, "s"))

    response shouldBe an[ChatResponse]
  }

  test("Slack should return a permalink URL for a specific message") {
    val sendMessage = Await.result(client.postMessage(channel, "Message to test permalink"), Duration.create(20, "s"))
    val ts = sendMessage.ts.getOrElse("")
    val response = Await.result(client.getPermalinkMessage(channel, ts), Duration.create(20, "s"))

    response shouldBe an[PermaLink]
  }

  test("Slack client should return a list of all channels") {
    Await.result(client.channelList(), Duration.create(20, "s")) shouldBe an[Channels]
  }

  test("Slack client should send a meMessage") {
    val response = Await.result(client.meMessage(channel, "This is a meMessage"), Duration.create(20, "s"))

    response shouldBe an[ChatResponse]
  }

  test("Slack client should open a direct message channel") {
    Await.result(client.imOpen(userId), Duration.create(20, "s")) shouldBe an[String]
  }

  test("Slack client should close a direct message channel") {
    val openChannel = Await.result(client.imOpen(userId, None, Some(true)), Duration.create(20, "s"))
    val response = Await.result(client.imClose(openChannel), Duration.create(20, "s"))

    response shouldBe an[None.type]
  }

  test("Slack client should return information about a user") {
    Await.result(client.userInfo(userId), Duration.create(20, "s")) shouldBe an[UserInfo]
  }

  test("Slack client should return a list of all users in a slack team") {
    Await.result(client.usersList(), Duration.create(20, "s")) shouldBe an[UsersList]
  }

  test("Slack client should find a user with an email address") {
    Await.result(client.userLookupByEmail(system.settings.config.getString("test.email")), Duration.create(20, "s")) shouldBe an[UserInfo]
  }
}