import akka.actor.ActorSystem
import app.SlackClient
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

}
