package com.lunatech.slack.client.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object SlackConfig {
  def getSlackClientConfig(implicit system: ActorSystem, materializer: ActorMaterializer) =
    SlackClientConfig(
      postMessageUrl = system.settings.config.getString("slack.api.postMessage"),
      postEphemeralUrl = system.settings.config.getString("slack.api.postEphemeral"),
      deleteMessageUrl = system.settings.config.getString("slack.api.deleteMessage"),
      updateMessageUrl = system.settings.config.getString("slack.api.updateMessage"),
      getPermalinkMessageUrl = system.settings.config.getString("slack.api.getPermalinkMessage"),
      meMessageUrl = system.settings.config.getString("slack.api.meMessage"),
      channelListUrl = system.settings.config.getString("slack.api.channelList"),
      imCloseUrl = system.settings.config.getString("slack.api.imClose"),
      imOpenUrl = system.settings.config.getString("slack.api.imOpen"),
      userInfoUrl = system.settings.config.getString("slack.api.userInfo"),
      usersListUrl = system.settings.config.getString("slack.api.usersList"),
      userLookupByEmailUrl = system.settings.config.getString("slack.api.userLookupByEmail"),
      openDialogUrl = system.settings.config.getString("slack.api.openDialog")
    )
}

case class SlackClientConfig(
  postMessageUrl: String,
  postEphemeralUrl: String,
  deleteMessageUrl: String,
  updateMessageUrl: String,
  getPermalinkMessageUrl: String,
  meMessageUrl: String,
  channelListUrl: String,
  imCloseUrl: String,
  imOpenUrl: String,
  userInfoUrl: String,
  usersListUrl: String,
  userLookupByEmailUrl: String,
  openDialogUrl: String
)
