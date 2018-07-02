package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class UsersList(
  members: Option[Seq[User]] = None,
  cache_ts: Option[Int] = None,
  response_metadata: Option[Metadata] = None,
)

case class UserInfo(user: User)

case class User(
  id: String,
  team_id: Option[String] = None,
  name: Option[String] = None,
  deleted: Option[Boolean] = None,
  color: Option[String] = None,
  real_name: Option[String] = None,
  tz: Option[String] = None,
  tz_label: Option[String] = None,
  tz_offset: Option[Int] = None,
  profile: Option[Profile] = None,
  is_admin: Option[Boolean] = None,
  is_owner: Option[Boolean] = None,
  is_primary_owner: Option[Boolean] = None,
  is_restricted: Option[Boolean] = None,
  is_ultra_restricted: Option[Boolean] = None,
  is_bot: Option[Boolean] = None,
  updated: Option[Int] = None,
  has_2fa: Option[Boolean] = None,
  is_app_user: Option[Boolean] = None
)

case class Profile(
  first_name: Option[String] = None,
  last_name: Option[String] = None,
  avatar_hash: Option[String] = None,
  status_text: Option[String] = None,
  status_emoji: Option[String] = None,
  real_name: Option[String] = None,
  display_name: Option[String] = None,
  real_name_normalized: Option[String] = None,
  display_name_normalized: Option[String] = None,
  email: Option[String] = None,
  team: Option[String] = None,
  always_active: Option[Boolean] = None,
  phone: Option[String] = None,
  skype: Option[String] = None,
  title: Option[String] = None
)

case class Metadata(next_cursor: Option[String] = None)

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