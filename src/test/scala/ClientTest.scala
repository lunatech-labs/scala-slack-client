import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.lunatech.slack.client.api.SlackClient
import com.lunatech.slack.client.models._
import org.scalatest.FunSuite
import org.scalatest.Matchers._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.runtime.BoxedUnit

class ClientTest extends FunSuite {
  implicit val system: ActorSystem = ActorSystem("slack")
  implicit val materializer: Materializer = ActorMaterializer()

  private val token = system.settings.config.getString("test.token")
  private val channel = system.settings.config.getString("test.channel")
  private val userId = system.settings.config.getString("test.userId")
  private val client = SlackClient(token)

  test("Slack client should send a message to a channel") {
    val chatMessage = ChatMessage(channel, "This is a message")
    val response = Await.result(client.postMessage(chatMessage), Duration.create(20, "s"))
    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel (ChatMessage)") {
    val message = ChatMessage(channel, "this is a message with ChatMessage")
      .addAttachment(AttachmentField("update your API", "toto")
        .addAction(Button("Button", "Primary button").asPrimaryButton)
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
    val ephemeralMessage = ChatEphemeral(channel, "This is an ephemeral message", userId)
    val response = Await.result(client.postEphemeral(ephemeralMessage), Duration.create(20, "s"))

    response shouldBe an[MessageTs]
  }

  test("Slack client should send a message to a channel with buttons") {

    val attachment = AttachmentField("upgrade your client api", "action_test")
      .addAction(Button("Default button", "Default"))
      .addAction(Button("Primary button", "Primary").asPrimaryButton)
      .addAction(Button("Danger button", "Danger").asDangerButton.withConfirmation("Are you sure"))

    val chatMessage = ChatMessage(channel, "This is a message with buttons").addAttachment(attachment)

    val response = Await.result(
      client.postMessage(chatMessage), Duration.create(20, "s")
    )

    response shouldBe an[MessageResponse]
  }

  val fields: List[BasicField] = List(
    BasicField("First item", "First item"),
    BasicField("Second Item", "Second Item"),
  )

  test("Slack client should send a message to a channel with a menu") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      Some(List(StaticMenu("Menu", "The menu")
        .addOption("First item", "First item")
        .addOption("Second Item", "Second Item")
        .withConfirmation("Are you sure ?")))
    )

    val chatMessage = ChatMessage(channel, "This is a message with a menu").addAttachment(attachment)

    val response = Await.result(
      client.postMessage(chatMessage), Duration.create(20, "s")
    )

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel with a menu listing users") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      Some(List(DynamicMenu("User menu", "Users", DataSource.users)))
    )

    val chatMessage = ChatMessage(channel, "This is a message with a menu listing users").addAttachment(attachment)

    val response = Await.result(
      client.postMessage(chatMessage), Duration.create(20, "s")
    )

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel with a menu listing channels") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      Some(List(DynamicMenu("Channels menu", "Channels", DataSource.channels)))
    )

    val chatMessage = ChatMessage(channel, "This is a message with a menu listing channels").addAttachment(attachment)

    val response = Await.result(
      client.postMessage(chatMessage), Duration.create(20, "s")
    )

    response shouldBe an[MessageResponse]
  }

  test("Slack client should send a message to a channel with a menu listing conversations") {
    val attachment = AttachmentField("upgrade your client api", "action_test",
      Some(List(DynamicMenu("Channels menu", "Converations", DataSource.conversations)))
    )

    val chatMessage = ChatMessage(channel, "This is a message with a menu listing converations").addAttachment(attachment)
    
    val response = Await.result(
      client.postMessage(chatMessage), Duration.create(20, "s")
    )
    response shouldBe an[MessageResponse]
  }

  test("Slack client should delete a message") {
    val chatMessage = ChatMessage(channel, "This message should be deleted")
    val sendMessage = Await.result(client.postMessage(chatMessage), Duration.create(20, "s"))
    val ts = sendMessage.ts.getOrElse("")
    val response = Await.result(client.deleteMessage(channel, ts), Duration.create(20, "s"))

    response shouldBe an[ChatResponse]
  }

  test("Slack client should update a message") {
    val chatMessage = ChatMessage(channel, "This message should be updated")
    val sendMessage = Await.result(client.postMessage(chatMessage), Duration.create(20, "s"))
    val ts = sendMessage.ts.getOrElse("")
    val response = Await.result(client.updateMessage(channel, "Message has been updated", ts), Duration.create(20, "s"))

    response shouldBe an[ChatResponse]
  }

  test("Slack should return a permalink URL for a specific message") {
    val chatMessage = ChatMessage(channel, "Message to test permalink")
    val sendMessage = Await.result(client.postMessage(chatMessage), Duration.create(20, "s"))
    val ts = sendMessage.ts.getOrElse("")
    val response = Await.result(client.getPermalinkMessage(channel, ts), Duration.create(20, "s"))

    response shouldBe an[Permalink]
  }

  test("Slack client should return a list of all channels") {
    Await.result(client.channelList(), Duration.create(20, "s")) shouldBe an[Channels]
  }

  test("Slack client should send a meMessage") {
    val response = Await.result(client.meMessage(channel, "This is a meMessage"), Duration.create(20, "s"))

    response shouldBe an[ChatResponse]
  }

  test("Slack client should open a direct message channel") {
    Await.result(client.imOpen(userId), Duration.create(20, "s")) shouldBe an[ChannelResponse]
  }

  test("Slack client should close a direct message channel") {
    val openChannel = Await.result(client.imOpen(userId, None, Some(true)), Duration.create(20, "s"))

    Await.result(client.imClose(openChannel.channel.id), Duration.create(20, "s")) shouldBe an[BoxedUnit]
  }

  test("Slack client should return information about a user") {
    Await.result(client.userInfo(userId), Duration.create(20, "s")) shouldBe an[User]
  }

  test("Slack client should return a list of all users in a slack team") {
    val a = Await.result(client.usersList(), Duration.create(20, "s")) shouldBe an[UsersList]
  }

  test("Slack client should find a user with an email address") {
    Await.result(client.userLookupByEmail(system.settings.config.getString("test.email")), Duration.create(20, "s")) shouldBe an[User]
  }
}