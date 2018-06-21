# scala-slack-client

TODO abstract

## Installation

TODO

## How to Use

* Create an app with slack [here](https://api.slack.com/apps).
* Add permissions to your bot by going to *OAuth & Permissions* tab
To run the Hello world code below add **chat:write:bot** in permissions scope.
* Go to *Install App* tab and install your bot to your workspace.
* Copy the **OAuth Access Token** access token, you will need it in your app to control the bot.
* Put the code below in your scala project and your bot should send an *"Hello, world!"* in your channel **#General**

```scala
val slackClient = new SlackClient(<Bot token>)
slackClient.postMessage("#general", "Hello, Wold!")
```

## Examples

### Sending a message

A slack bot can send two type of message : In channel and Ephemeral.  
An ephemeral message can only be viewed by one User.

Our library uses [postMessage](https://api.slack.com/methods/chat.postMessage) and [postEphemeral](https://api.slack.com/methods/chat.postEphemeral) to send messages.

We provide builders to make your message easier to write.

If you want to write a message with buttons you can write it like this. (See [slack documention](https://api.slack.com/methods/chat.postMessage) for more information)

```scala
val message = ChatMessage("#general", "this is a message with ChatMessage")
      .addAttachment(AttachmentField("update your API", "buttons")
        .addAction(ActionField("PrimaryButton", "Primary button").asPrimaryButton))
        .addAction(ActionField("DefaultButton", "Default button"))
        .addAction(ActionField("DangerButton", "DangerButton").asDangerButton.withConfirmation("Are you sure"))
        .withPretext("Click one of these button")
      )

slackClient.postMessage(message)
```

Although, you can give directly your information.

```scala
val attachment = AttachmentField(...)

slackClient.postMessage(channel = "#general", text = "Hello", attachments = Some(List(attachment)))
```
