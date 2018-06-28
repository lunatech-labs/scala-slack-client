# scala-slack-client

An asynchronous Scala library to interact with the Slack [API](https://api.slack.com) as of June 2018.

[ ![Codeship Status for lunatech-labs/scala-slack-client](https://app.codeship.com/projects/0a9404a0-5ce4-0136-2cc1-460bed3baf8d/status?branch=master)](https://app.codeship.com/projects/295784)

[ ![Codeship Status for lunatech-labs/scala-slack-client](https://app.codeship.com/projects/0a9404a0-5ce4-0136-2cc1-460bed3baf8d/status?branch=master)](https://app.codeship.com/projects/295784)

## Installation

Add this dependency in your build.sbt

```
libraryDependencies += "com.lunatech" % "scala-slack-client" % "0.1.0"
```

## How to Use

* Create an app with slack [here](https://api.slack.com/apps).
* Add permissions to your bot by going to *OAuth & Permissions* tab
To run the Hello world code below add **chat:write:bot** in permissions scope.
* Go to *Install App* tab and install your bot to your workspace.
* Copy the **OAuth Access Token** access token, you will need it in your app to control the bot.
* Put the code below in your scala project and your bot should send an *"Hello, world!"* in your channel **#General**

```scala
val slackClient = SlackClient(<Bot token>)
slackClient.postMessage("#general", "Hello, Wold!")
```

To process the response of your bot features you can use our Parser.  

SlashCommands :  
```
Parser.slashCommand(body: Map[String, Seq[String]]): Try[SlashCommandPayload]
```
Interactive Components : 
```
Parser.getPayload(body: Map[String, Seq[String]]): Try[Payload]
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
All methods can be used the same way.

## Implemented methods

| Method              | Slack API                                             | Version |
|---------------------|-------------------------------------------------------|---------|
| userInfo            | https://api.slack.com/methods/users.info                      | 0.1.0   |
| usersList           | https://api.slack.com/methods/users.list                      | 0.1.0   |
| userLookupByEmail   | https://api.slack.com/methods/users.lookupByEmail             | 0.1.0   |
| channelList         | https://api.slack.com/methods/channels.list                   | 0.1.0   |
| deleteMessage       | https://api.slack.com/methods/chat.delete                     | 0.1.0   |
| getPermalinkMessage | https://api.slack.com/methods/chat.getPermalink               | 0.1.0   |
| meMessage           | https://api.slack.com/methods/chat.meMessage                  | 0.1.0   |
| postEphemeral       | https://api.slack.com/methods/chat.postEphemeral              | 0.1.0   |
| postMessage         | https://api.slack.com/methods/chat.postMessage                | 0.1.0   |
| updateMessage       | https://api.slack.com/methods/chat.update                     | 0.1.0   |
| imClose             | https://api.slack.com/methods/im.close                        | 0.1.0   |
| openDialog          | https://api.slack.com/methods/dialog.open                     | 0.1.0   |
| imOpen              | https://api.slack.com/methods/im.open                         | 0.1.0   |
|                     | https://api.slack.com/methods/chat.unfurl                     |         |
|                     | https://api.slack.com/methods/api.test                        |         |
|                     | https://api.slack.com/methods/apps.permissions.info           |         |
|                     | https://api.slack.com/methods/apps.permissions.request        |         |
|                     | https://api.slack.com/methods/apps.permissions.resources.list |         |
|                     | https://api.slack.com/methods/apps.permissions.scopes.list    |         |
|                     | https://api.slack.com/methods/auth.revoke                     |         |
|                     | https://api.slack.com/methods/auth.test                       |         |
|                     | https://api.slack.com/methods/bots.info                       |         |
|                     | https://api.slack.com/methods/channels.archive                |         |
|                     | https://api.slack.com/methods/channels.create                 |         |
|                     | https://api.slack.com/methods/channels.history                |         |
|                     | https://api.slack.com/methods/channels.info                   |         |
|                     | https://api.slack.com/methods/channels.invite                 |         |
|                     | https://api.slack.com/methods/channels.join                   |         |
|                     | https://api.slack.com/methods/channels.kick                   |         |
|                     | https://api.slack.com/methods/channels.leave                  |         |
|                     | https://api.slack.com/methods/channels.mark                   |         |
|                     | https://api.slack.com/methods/channels.rename                 |         |
|                     | https://api.slack.com/methods/channels.replies                |         |
|                     | https://api.slack.com/methods/channels.setPurpose             |         |
|                     | https://api.slack.com/methods/channels.setTopic               |         |
|                     | https://api.slack.com/methods/channels.unarchive              |         |
|                     | https://api.slack.com/methods/conversations.archive           |         |
|                     | https://api.slack.com/methods/conversations.create            |         |
|                     | https://api.slack.com/methods/conversations.history           |         |
|                     | https://api.slack.com/methods/conversations.info              |         |
|                     | https://api.slack.com/methods/conversations.invite            |         |
|                     | https://api.slack.com/methods/conversations.join              |         |
|                     | https://api.slack.com/methods/conversations.kick              |         |
|                     | https://api.slack.com/methods/conversations.leave             |         |
|                     | https://api.slack.com/methods/conversations.list              |         |
|                     | https://api.slack.com/methods/conversations.members           |         |
|                     | https://api.slack.com/methods/conversations.open              |         |
|                     | https://api.slack.com/methods/conversations.rename            |         |
|                     | https://api.slack.com/methods/conversations.replies           |         |
|                     | https://api.slack.com/methods/cconversations.setPurpose       |         |
|                     | https://api.slack.com/methods/conversations.setTopic          |         |
|                     | https://api.slack.com/methods/conversations.unarchive         |         |
|                     | https://api.slack.com/methods/dnd.endDnd                      |         |
|                     | https://api.slack.com/methods/dnd.endSnooze                   |         |
|                     | https://api.slack.com/methods/dnd.info                        |         |
|                     | https://api.slack.com/methods/dnd.setSnooze                   |         |
|                     | https://api.slack.com/methods/dnd.teamInfo                    |         |
|                     | https://api.slack.com/methods/emoji.list                      |         |
|                     | https://api.slack.com/methods/files.comments.add              |         |
|                     | https://api.slack.com/methods/files.comments.delete           |         |
|                     | https://api.slack.com/methods/files.comments.edit             |         |
|                     | https://api.slack.com/methods/files.delete                    |         |
|                     | https://api.slack.com/methods/files.info                      |         |
|                     | https://api.slack.com/methods/files.list                      |         |
|                     | https://api.slack.com/methods/files.revokePublicURL           |         |
|                     | https://api.slack.com/methods/files.sharedPublicURL           |         |
|                     | https://api.slack.com/methods/files.upload                    |         |
|                     | https://api.slack.com/methods/groups.archive                  |         |
|                     | https://api.slack.com/methods/groups.create                   |         |
|                     | https://api.slack.com/methods/groups.createChild              |         |
|                     | https://api.slack.com/methods/groups.history                  |         |
|                     | https://api.slack.com/methods/groups.info                     |         |
|                     | https://api.slack.com/methods/groups.invite                   |         |
|                     | https://api.slack.com/methods/groups.kick                     |         |
|                     | https://api.slack.com/methods/groups.leave                    |         |
|                     | https://api.slack.com/methods/groups.list                     |         |
|                     | https://api.slack.com/methods/groups.mark                     |         |
|                     | https://api.slack.com/methods/groups.open                     |         |
|                     | https://api.slack.com/methods/groups.rename                   |         |
|                     | https://api.slack.com/methods/groups.replies                  |         |
|                     | https://api.slack.com/methods/groups.setPurpose               |         |
|                     | https://api.slack.com/methods/groups.setTopic                 |         |
|                     | https://api.slack.com/methods/groups.unarchive                |         |
|                     | https://api.slack.com/methods/im.history                      |         |
|                     | https://api.slack.com/methods/im.list                         |         |
|                     | https://api.slack.com/methods/im.mark                         |         |
|                     | https://api.slack.com/methods/im.replies                      |         |
|                     | https://api.slack.com/methods/migration.exchange              |         |
|                     | https://api.slack.com/methods/mpim.close                      |         |
|                     | https://api.slack.com/methods/mpim.history                    |         |
|                     | https://api.slack.com/methods/mpim.list                       |         |
|                     | https://api.slack.com/methods/mpim.mark                       |         |
|                     | https://api.slack.com/methods/mpim.open                       |         |
|                     | https://api.slack.com/methods/mpim.replies                    |         |
|                     | https://api.slack.com/methods/oauth.access                    |         |
|                     | https://api.slack.com/methods/oauth.token                     |         |
|                     | https://api.slack.com/methods/pins.add                        |         |
|                     | https://api.slack.com/methods/pins.list                       |         |
|                     | https://api.slack.com/methods/pins.remove                     |         |
|                     | https://api.slack.com/methods/reactions.add                   |         |
|                     | https://api.slack.com/methods/reactions.get                   |         |
|                     | https://api.slack.com/methods/reactions.list                  |         |
|                     | https://api.slack.com/methods/reactions.remove                |         |
|                     | https://api.slack.com/methods/reminders.add                   |         |
|                     | https://api.slack.com/methods/reminders.complete              |         |
|                     | https://api.slack.com/methods/reminders.delete                |         |
|                     | https://api.slack.com/methods/reminders.info                  |         |
|                     | https://api.slack.com/methods/reminders.list                  |         |
|                     | https://api.slack.com/methods/rtm.connect                     |         |
|                     | https://api.slack.com/methods/rtm.start                       |         |
|                     | https://api.slack.com/methods/search.all                      |         |
|                     | https://api.slack.com/methods/search.files                    |         |
|                     | https://api.slack.com/methods/search.messages                 |         |
|                     | https://api.slack.com/methods/stars.add                       |         |
|                     | https://api.slack.com/methods/stars.list                      |         |
|                     | https://api.slack.com/methods/stars.remove                    |         |
|                     | https://api.slack.com/methods/team.accessLogs                 |         |
|                     | https://api.slack.com/methods/team.billableInfo               |         |
|                     | https://api.slack.com/methods/team.info                       |         |
|                     | https://api.slack.com/methods/team.integrationLogs            |         |
|                     | https://api.slack.com/methods/team.profile.get                |         |
|                     | https://api.slack.com/methods/usergroups.create               |         |
|                     | https://api.slack.com/methods/usergroups.disable              |         |
|                     | https://api.slack.com/methods/usergroups.enable               |         |
|                     | https://api.slack.com/methods/usergroups.list                 |         |
|                     | https://api.slack.com/methods/usergroups.update               |         |
|                     | https://api.slack.com/methods/usergroups.users.list           |         |
|                     | https://api.slack.com/methods/usergroups.users.update         |         |
|                     | https://api.slack.com/methods/users.conversations             |         |
|                     | https://api.slack.com/methods/users.deletePhoto               |         |
|                     | https://api.slack.com/methods/users.getPresence               |         |
|                     | https://api.slack.com/methods/users.identity                  |         |
|                     | https://api.slack.com/methods/users.setActive                 |         |
|                     | https://api.slack.com/methods/users.setPhoto                  |         |
|                     | https://api.slack.com/methods/users.setPresence               |         |
|                     | https://api.slack.com/methods/users.profile.get               |         |
|                     | https://api.slack.com/methods/users.profile.set               |         | 

## License

MIT License

Copyright (c) 2018 [Lunatech](https://www.lunatech.com/).

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.