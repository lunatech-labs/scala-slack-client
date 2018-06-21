package models

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