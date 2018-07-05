package com.lunatech.slack.client.api

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.lunatech.slack.client.models.{ChannelResponse, ChatMessage, MessageResponse, MessageTs, UserInfo, UsersList, _}
import com.lunatech.slack.client.services.SlackCaller
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}


class SlackClient(token: String, config: SlackClientConfig, slackCaller: SlackCaller)(implicit system: ActorSystem, materializer: Materializer) {
  implicit val formatUnit: OFormat[Unit] = new OFormat[Unit] {
    def reads(js: JsValue): JsResult[Unit] = JsSuccess(())

    def writes(a: Unit): JsObject = Json.obj()
  }

  /**
    * https://api.slack.com/methods/chat.getPermalink
    */
  def getPermalinkMessage(channel: String, messageTs: String)(implicit ec: ExecutionContext): Future[Permalink] = {
    val params: Map[String, String] = Map("channel" -> channel, "message_ts" -> messageTs)

    slackCaller.makeGetApiCall[Permalink](token, config.getPermalinkMessageUrl, params)
  }


  /**
    * https://api.slack.com/methods/chat.postMessage
    */
  def postMessage(message: ChatMessage)(implicit ec: ExecutionContext): Future[MessageResponse] = {
    slackCaller.makeApiCall[MessageResponse](token, config.postMessageUrl, Json.toJson(message))
  }

  /**
    * https://api.slack.com/methods/chat.postEphemeral
    */
  def postEphemeral(chatEphemeral: ChatEphemeral)(implicit ec: ExecutionContext): Future[MessageTs] = {
    slackCaller.makeApiCall[MessageTs](token, config.postEphemeralUrl, Json.toJson(chatEphemeral))
  }

  /**
    * https://api.slack.com/methods/chat.delete
    */
  def deleteMessage(channel: String, ts: String, asUser: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    slackCaller.makeApiCall[ChatResponse](token, config.deleteMessageUrl,
      Json.obj(
        "channel" -> channel,
        "ts" -> ts,
        "as_user" -> asUser
      ))
  }

  /**
    * https://api.slack.com/methods/dialog.open
    */
  def openDialog(dialog: Dialog, triggerId: String)(implicit ec: ExecutionContext): Future[Unit] = {
    slackCaller.makeApiCall[Unit](token, config.openDialogUrl,
      Json.obj(
        "dialog" -> dialog,
        "trigger_id" -> triggerId
      )
    )
  }

  /**
    * https://api.slack.com/methods/chat.update
    */
  def updateMessage(channel: String, text: String, ts: String, asUser: Option[Boolean] = None, attachments: Option[Seq[AttachmentField]] = None,
    linkNames: Option[Boolean] = None, parse: Option[String] = None)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    slackCaller.makeApiCall[ChatResponse](token, config.updateMessageUrl,
      Json.obj(
        "channel" -> channel,
        "text" -> text,
        "ts" -> ts,
        "as_user" -> asUser,
        "attachments" -> attachments,
        "link_names" -> linkNames,
        "parse" -> parse
      ))
  }

  /**
    * https://slack.com/api/chat.meMessage
    */
  def meMessage(channel: String, text: String)(implicit ec: ExecutionContext): Future[ChatResponse] = {
    slackCaller.makeApiCall[ChatResponse](token, config.meMessageUrl,
      Json.obj(
        "channel" -> channel,
        "text" -> text
      ))
  }

  /**
    * https://api.slack.com/methods/channels.list
    */
  def channelList(cursor: Option[String] = None, excludeArchived: Option[Boolean] = None, excludeMembers: Option[Boolean] = None,
    limit: Option[Int] = None)(implicit ec: ExecutionContext): Future[Channels] = {
    slackCaller.makeApiCall[Channels](token, config.channelListUrl,
      Json.obj(
        "cursor" -> cursor,
        "exclude_archived" -> excludeArchived,
        "exclude_members" -> excludeMembers,
        "limit" -> limit
      ))
  }

  /**
    * https://api.slack.com/methods/im.close
    */
  def imClose(channel: String)(implicit ec: ExecutionContext): Future[Unit] = {
    slackCaller.makeApiCall[Unit](token, config.imCloseUrl,
      Json.obj(
        "channel" -> channel
      ))
  }

  /**
    * https://api.slack.com/methods/im.open
    */
  def imOpen(user: String, includeLocale: Option[Boolean] = None, returnIm: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[ChannelResponse] = {
    slackCaller.makeApiCall[ChannelResponse](token, config.imOpenUrl,
      Json.obj(
        "user" -> user,
        "include_locale" -> includeLocale,
        "return_im" -> returnIm
      ))

  }

  /**
    * https://api.slack.com/methods/users.info
    */
  def userInfo(user: String, includeLocale: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[User] = {
    val params: Map[String, String] = Map("user" -> user,
      "include_locale" -> includeLocale).collect {
      case (k, Some(v)) => k -> v.toString
      case (k, v: String) => k -> v
    }

    slackCaller.makeGetApiCall[UserInfo](token, config.userInfoUrl, params)
      .map(userInfo => userInfo.user)
  }

  /**
    * https://api.slack.com/methods/users.list
    */
  def usersList(cursor: Option[String] = None, includeLocale: Option[Boolean] = None, limit: Option[Int] = None
    , presence: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[UsersList] = {
    val params: Map[String, String] = Map(
      "cursor" -> cursor,
      "include_locale" -> includeLocale,
      "limit" -> limit,
      "presence" -> presence).collect {
      case (k, Some(v)) => k -> v.toString
    }

    slackCaller.makeGetApiCall[UsersList](token, config.usersListUrl, params)
  }

  /**
    * https://api.slack.com/methods/users.lookupByEmail
    */
  def userLookupByEmail(email: String)(implicit ec: ExecutionContext): Future[User] = {
    val params: Map[String, String] = Map("email" -> email)

    slackCaller.makeGetApiCall[UserInfo](token, config.userLookupByEmailUrl, params)
      .map(userInfo => userInfo.user)
  }
}

object SlackClient {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def apply(token: String): SlackClient = new SlackClient(token, SlackConfig.getSlackClientConfig, new SlackCaller())

  def apply(token: String, config: SlackClientConfig) = new SlackClient(token, config, new SlackCaller())

  def apply(token: String, slackCaller: SlackCaller): SlackClient = new SlackClient(token, SlackConfig.getSlackClientConfig, slackCaller)

  def jsonToClass[T](response: String)(implicit reads: Reads[T]): Try[T] = {
    val jsonResponse = Json.parse(response)

    jsonResponse.validate[SlackResponse[T]].map{
      case SlackSuccess(s) => Success(s)
      case SlackError(e) => Failure(new Exception(s"Error: $e"))
    }.getOrElse(Failure(new Exception("Unexpected error")))
  }
}
