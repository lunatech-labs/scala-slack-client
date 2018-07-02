# scala-slack-client

An asynchronous Scala library to interact with the Slack [API](https://api.slack.com) as of June 2018.

[ ![Codeship Status for lunatech-labs/scala-slack-client](https://app.codeship.com/projects/0a9404a0-5ce4-0136-2cc1-460bed3baf8d/status?branch=master)](https://app.codeship.com/projects/295784)

## Installation

Add this dependency in your build.sbt

```
libraryDependencies += "com.lunatech" % "scala-slack-client" % "0.1.1"
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

## Examples

### Sending a message

A slack bot can send two type of message : In channel and Ephemeral.  
An ephemeral message can only be viewed by one User.

Our library uses [postMessage](https://api.slack.com/methods/chat.postMessage) and [postEphemeral](https://api.slack.com/methods/chat.postEphemeral) to send messages.

We provide builders to make your message easier to write.

If you want to write a message with buttons you can write it like this. (See [slack documention](https://api.slack.com/methods/chat.postMessage) for more information)

```scala
val message = ChatMessage(channel = "#general", text = "this is a message with ChatMessage")
      .addAttachment(AttachmentField(fallback = "update your API", callback_id = "buttons")
        .addAction(ActionField(name = "PrimaryButton", text = "Primary button").asPrimaryButton))
        .addAction(ActionField(name = "DefaultButton", text = "Default button"))
        .addAction(ActionField(name = "DangerButton", text = "DangerButton").asDangerButton.withConfirmation("Are you sure"))
        .withPretext("Click one of these button")
      )

slackClient.postMessage(message)
```
All methods can be used the same way.

## Implemented methods

| Method              | Slack API                                             | Version |
|---------------------|-------------------------------------------------------|---------|
| userInfo            | https://slack.com/api/users.info                      | 0.1.0   |
| usersList           | https://slack.com/api/users.list                      | 0.1.0   |
| userLookupByEmail   | https://slack.com/api/users.lookupByEmail             | 0.1.0   |
| channelList         | https://slack.com/api/channels.list                   | 0.1.0   |
| deleteMessage       | https://slack.com/api/chat.delete                     | 0.1.0   |
| getPermalinkMessage | https://slack.com/api/chat.getPermalink               | 0.1.0   |
| meMessage           | https://slack.com/api/chat.meMessage                  | 0.1.0   |
| postEphemeral       | https://slack.com/api/chat.postEphemeral              | 0.1.0   |
| postMessage         | https://slack.com/api/chat.postMessage                | 0.1.0   |
| updateMessage       | https://slack.com/api/chat.update                     | 0.1.0   |
| imClose             | https://slack.com/api/im.close                        | 0.1.0   |
| openDialog          | https://slack.com/api/dialog.open                     | 0.1.0   |
| imOpen              | https://slack.com/api/im.open                         | 0.1.0   |
|                     | https://slack.com/api/chat.unfurl                     |         |
|                     | https://slack.com/api/api.test                        |         |
|                     | https://slack.com/api/apps.permissions.info           |         |
|                     | https://slack.com/api/apps.permissions.request        |         |
|                     | https://slack.com/api/apps.permissions.resources.list |         |
|                     | https://slack.com/api/apps.permissions.scopes.list    |         |
|                     | https://slack.com/api/auth.revoke                     |         |
|                     | https://slack.com/api/auth.test                       |         |
|                     | https://slack.com/api/bots.info                       |         |
|                     | https://slack.com/api/channels.archive                |         |
|                     | https://slack.com/api/channels.create                 |         |
|                     | https://slack.com/api/channels.history                |         |
|                     | https://slack.com/api/channels.info                   |         |
|                     | https://slack.com/api/channels.invite                 |         |
|                     | https://slack.com/api/channels.join                   |         |
|                     | https://slack.com/api/channels.kick                   |         |
|                     | https://slack.com/api/channels.leave                  |         |
|                     | https://slack.com/api/channels.mark                   |         |
|                     | https://slack.com/api/channels.rename                 |         |
|                     | https://slack.com/api/channels.replies                |         |
|                     | https://slack.com/api/channels.setPurpose             |         |
|                     | https://slack.com/api/channels.setTopic               |         |
|                     | https://slack.com/api/channels.unarchive              |         |
|                     | https://slack.com/api/conversations.archive           |         |
|                     | https://slack.com/api/conversations.create            |         |
|                     | https://slack.com/api/conversations.history           |         |
|                     | https://slack.com/api/conversations.info              |         |
|                     | https://slack.com/api/conversations.invite            |         |
|                     | https://slack.com/api/conversations.join              |         |
|                     | https://slack.com/api/conversations.kick              |         |
|                     | https://slack.com/api/conversations.leave             |         |
|                     | https://slack.com/api/conversations.list              |         |
|                     | https://slack.com/api/conversations.members           |         |
|                     | https://slack.com/api/conversations.open              |         |
|                     | https://slack.com/api/conversations.rename            |         |
|                     | https://slack.com/api/conversations.replies           |         |
|                     | https://slack.com/api/cconversations.setPurpose       |         |
|                     | https://slack.com/api/conversations.setTopic          |         |
|                     | https://slack.com/api/conversations.unarchive         |         |
|                     | https://slack.com/api/dnd.endDnd                      |         |
|                     | https://slack.com/api/dnd.endSnooze                   |         |
|                     | https://slack.com/api/dnd.info                        |         |
|                     | https://slack.com/api/dnd.setSnooze                   |         |
|                     | https://slack.com/api/dnd.teamInfo                    |         |
|                     | https://slack.com/api/emoji.list                      |         |
|                     | https://slack.com/api/files.comments.add              |         |
|                     | https://slack.com/api/files.comments.delete           |         |
|                     | https://slack.com/api/files.comments.edit             |         |
|                     | https://slack.com/api/files.delete                    |         |
|                     | https://slack.com/api/files.info                      |         |
|                     | https://slack.com/api/files.list                      |         |
|                     | https://slack.com/api/files.revokePublicURL           |         |
|                     | https://slack.com/api/files.sharedPublicURL           |         |
|                     | https://slack.com/api/files.upload                    |         |
|                     | https://slack.com/api/groups.archive                  |         |
|                     | https://slack.com/api/groups.create                   |         |
|                     | https://slack.com/api/groups.createChild              |         |
|                     | https://slack.com/api/groups.history                  |         |
|                     | https://slack.com/api/groups.info                     |         |
|                     | https://slack.com/api/groups.invite                   |         |
|                     | https://slack.com/api/groups.kick                     |         |
|                     | https://slack.com/api/groups.leave                    |         |
|                     | https://slack.com/api/groups.list                     |         |
|                     | https://slack.com/api/groups.mark                     |         |
|                     | https://slack.com/api/groups.open                     |         |
|                     | https://slack.com/api/groups.rename                   |         |
|                     | https://slack.com/api/groups.replies                  |         |
|                     | https://slack.com/api/groups.setPurpose               |         |
|                     | https://slack.com/api/groups.setTopic                 |         |
|                     | https://slack.com/api/groups.unarchive                |         |
|                     | https://slack.com/api/im.history                      |         |
|                     | https://slack.com/api/im.list                         |         |
|                     | https://slack.com/api/im.mark                         |         |
|                     | https://slack.com/api/im.replies                      |         |
|                     | https://slack.com/api/migration.exchange              |         |
|                     | https://slack.com/api/mpim.close                      |         |
|                     | https://slack.com/api/mpim.history                    |         |
|                     | https://slack.com/api/mpim.list                       |         |
|                     | https://slack.com/api/mpim.mark                       |         |
|                     | https://slack.com/api/mpim.open                       |         |
|                     | https://slack.com/api/mpim.replies                    |         |
|                     | https://slack.com/api/oauth.access                    |         |
|                     | https://slack.com/api/oauth.token                     |         |
|                     | https://slack.com/api/pins.add                        |         |
|                     | https://slack.com/api/pins.list                       |         |
|                     | https://slack.com/api/pins.remove                     |         |
|                     | https://slack.com/api/reactions.add                   |         |
|                     | https://slack.com/api/reactions.get                   |         |
|                     | https://slack.com/api/reactions.list                  |         |
|                     | https://slack.com/api/reactions.remove                |         |
|                     | https://slack.com/api/reminders.add                   |         |
|                     | https://slack.com/api/reminders.complete              |         |
|                     | https://slack.com/api/reminders.delete                |         |
|                     | https://slack.com/api/reminders.info                  |         |
|                     | https://slack.com/api/reminders.list                  |         |
|                     | https://slack.com/api/rtm.connect                     |         |
|                     | https://slack.com/api/rtm.start                       |         |
|                     | https://slack.com/api/search.all                      |         |
|                     | https://slack.com/api/search.files                    |         |
|                     | https://slack.com/api/search.messages                 |         |
|                     | https://slack.com/api/stars.add                       |         |
|                     | https://slack.com/api/stars.list                      |         |
|                     | https://slack.com/api/stars.remove                    |         |
|                     | https://slack.com/api/team.accessLogs                 |         |
|                     | https://slack.com/api/team.billableInfo               |         |
|                     | https://slack.com/api/team.info                       |         |
|                     | https://slack.com/api/team.integrationLogs            |         |
|                     | https://slack.com/api/team.profile.get                |         |
|                     | https://slack.com/api/usergroups.create               |         |
|                     | https://slack.com/api/usergroups.disable              |         |
|                     | https://slack.com/api/usergroups.enable               |         |
|                     | https://slack.com/api/usergroups.list                 |         |
|                     | https://slack.com/api/usergroups.update               |         |
|                     | https://slack.com/api/usergroups.users.list           |         |
|                     | https://slack.com/api/usergroups.users.update         |         |
|                     | https://slack.com/api/users.conversations             |         |
|                     | https://slack.com/api/users.deletePhoto               |         |
|                     | https://slack.com/api/users.getPresence               |         |
|                     | https://slack.com/api/users.identity                  |         |
|                     | https://slack.com/api/users.setActive                 |         |
|                     | https://slack.com/api/users.setPhoto                  |         |
|                     | https://slack.com/api/users.setPresence               |         |
|                     | https://slack.com/api/users.profile.get               |         |
|                     | https://slack.com/api/users.profile.set               |         | 

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