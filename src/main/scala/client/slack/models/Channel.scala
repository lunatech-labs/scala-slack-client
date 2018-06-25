package client.slack.models

case class Channels(
                     channels: Option[Seq[Channel]] = None,
                     response_metadata: Option[Metadata] = None
                   )

case class Channel(
                    id: Option[String] = None,
                    name: Option[String] = None,
                    is_channel: Option[Boolean] = None,
                    created: Option[Int] = None,
                    creator: Option[String] = None,
                    is_archived: Option[Boolean] = None,
                    is_general: Option[Boolean] = None,
                    name_normalized: Option[String] = None,
                    is_shared: Option[Boolean] = None,
                    is_org_shared: Option[Boolean] = None,
                    is_member: Option[Boolean] = None,
                    is_private: Option[Boolean] = None,
                    is_mpim: Option[Boolean] = None,
                    last_read: Option[String] = None,
                    latest: Option[Latest] = None,
                    unread_count: Option[Int] = None,
                    unread_count_display: Option[Int] = None,
                    members: Option[Seq[String]] = None,
                    topic: Option[Topic] = None,
                    purpose: Option[Purpose] = None,
                    previous_names: Option[Seq[String]] = None,
                  )

case class Latest(
                   text: Option[String] = None,
                   username: Option[String] = None,
                   bot_id: Option[String] = None,
                   attachments: Option[Seq[ChannelAttachments]] = None,
                   `type`: Option[String] = None,
                   subtype: Option[String] = None,
                   ts: Option[String] = None
                 )

case class ChannelAttachments(
                               text: Option[String] = None,
                               id: Option[Int] = None,
                               fallback: Option[String] = None
                             )

case class Topic(
                  value: Option[String] = None,
                  creator: Option[String] = None,
                  last_set: Option[Int] = None
                )

case class Purpose(
                    value: Option[String] = None,
                    creator: Option[String] = None,
                    last_set: Option[Int] = None
                  )