package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class UsersList(
  members: Option[Seq[User]] = None,
  cacheTs: Option[Int] = None,
  responseMetadata: Option[Metadata] = None,
)

case class UserInfo(user: User)

case class User(
  id: String,
  teamId: Option[String] = None,
  name: Option[String] = None,
  deleted: Option[Boolean] = None,
  color: Option[String] = None,
  realName: Option[String] = None,
  tz: Option[String] = None,
  tzLabel: Option[String] = None,
  tzOffset: Option[String] = None,
  profile: Option[Profile] = None,
  isAdmin: Option[Boolean] = None,
  isOwner: Option[Boolean] = None,
  isPrimaryOwner: Option[Boolean] = None,
  isRestricted: Option[Boolean] = None,
  isUltraRestricted: Option[Boolean] = None,
  isBot: Option[Boolean] = None,
  updated: Option[Int] = None,
  has2fa: Option[Boolean] = None,
  isAppUser: Option[Boolean] = None
)

case class Profile(
  firstName: Option[String] = None,
  lastName: Option[String] = None,
  avatarHash: Option[String] = None,
  statusText: Option[String] = None,
  statusEmoji: Option[String] = None,
  realName: Option[String] = None,
  displayName: Option[String] = None,
  realNameNormalized: Option[String] = None,
  displayNameNormalized: Option[String] = None,
  email: Option[String] = None,
  team: Option[String] = None,
  alwaysActive: Option[String] = None,
  phone: Option[String] = None,
  skype: Option[String] = None
)

case class Metadata(nextCursor: Option[String] = None)

object Metadata {
  implicit val metadataResponseFormat = Json.format[Metadata]
}

object Profile {
  implicit val profileFormat = Json.format[Profile]
}

object User {
  implicit val userFormat = Json.format[User]
}

object UserInfo {
  implicit val userInfoResponseFormat = Json.format[UserInfo]
}

object UsersList {
  implicit val usersListResponseFormat = Json.format[UsersList]
}