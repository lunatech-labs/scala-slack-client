package com.lunatech.slack.client.api

import com.lunatech.slack.client.models._
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class ChannelList extends FlatSpec {

  it should "Parse json correctly to ChannelList" in {
    val randomTopic = Topic(
      value = Some("Other stuff"),
      creator = Some("U0G9QF9C6"),
      last_set = Some(1449709352)
    )

    val randomPurpose = Purpose(
      value = Some("A place for non-work-related flimflam, faffing, hodge-podge or jibber-jabber you'd prefer to keep out of more focused work-related channels."),
      creator = Some(""),
      last_set = Some(0)
    )

    val randomChan = Channel(
      id = Some("C0G9QF9GW"),
      name = Some("random"),
      is_channel = Some(true),
      created = Some(1449709280),
      creator = Some("U0G9QF9C6"),
      is_archived = Some(false),
      is_general = Some(false),
      name_normalized = Some("random"),
      is_shared = Some(false),
      is_org_shared = Some(false),
      is_member = Some(true),
      is_private = Some(false),
      is_mpim = Some(false),
      members = Some(Seq("U0G9QF9C6", "U0G9WFXNZ")),
      topic = Some(randomTopic),
      purpose = Some(randomPurpose),
      previous_names = Some(Seq()),
      num_members = Some(2)
    )

    val generalTopic = Topic(
      value = Some("Talk about anything!"),
      creator = Some("U0G9QF9C6"),
      last_set = Some(1449709364)
    )

    val generalPurpose = Purpose(
      value = Some("To talk about anything!"),
      creator = Some("U0G9QF9C6"),
      last_set = Some(1449709334)
    )

    val generalChannel = Channel(
      id = Some("C0G9QKBBL"),
      name = Some("general"),
      is_channel = Some(true),
      created = Some(1449709280),
      creator = Some("U0G9QF9C6"),
      is_archived = Some(false),
      is_general = Some(true),
      name_normalized = Some("general"),
      is_shared = Some(false),
      is_org_shared = Some(false),
      is_member = Some(true),
      is_private = Some(false),
      is_mpim = Some(false),
      members = Some(Seq("U0G9QF9C6", "U0G9WFXNZ")),
      topic = Some(generalTopic),
      purpose = Some(generalPurpose),
      previous_names = Some(Seq()),
      num_members = Some(2)
    )

    val res = SlackClient.jsonToClass[Channels](json)

    res.isSuccess shouldBe true
    res.get.response_metadata shouldBe Some(Metadata(next_cursor = Some("dGVhbTpDMUg5UkVTR0w=")))

    res.get.channels.get.length shouldBe 2

    val channels = res.get.channels.get

    channels(0) shouldBe randomChan
    channels(0).topic shouldBe Some(randomTopic)
    channels(0).purpose shouldBe Some(randomPurpose)

    channels(1) shouldBe generalChannel
    channels(1).topic shouldBe Some(generalTopic)
    channels(1).purpose shouldBe Some(generalPurpose)
  }

  it should "send an error" in {
    val json =
      """
        |{
        |    "ok": false,
        |    "error": "invalid_auth"
        |}
      """.stripMargin

    val res = SlackClient.jsonToClass[Channels](json)

    res.isFailure shouldBe true
    res.failed.get.getMessage shouldBe "Error: invalid_auth"
  }

  private def json =
    """
      |{
      |    "ok": true,
      |    "channels": [
      |        {
      |            "id": "C0G9QF9GW",
      |            "name": "random",
      |            "is_channel": true,
      |            "created": 1449709280,
      |            "creator": "U0G9QF9C6",
      |            "is_archived": false,
      |            "is_general": false,
      |            "name_normalized": "random",
      |            "is_shared": false,
      |            "is_org_shared": false,
      |            "is_member": true,
      |            "is_private": false,
      |            "is_mpim": false,
      |            "members": [
      |                "U0G9QF9C6",
      |                "U0G9WFXNZ"
      |            ],
      |            "topic": {
      |                "value": "Other stuff",
      |                "creator": "U0G9QF9C6",
      |                "last_set": 1449709352
      |            },
      |            "purpose": {
      |                "value": "A place for non-work-related flimflam, faffing, hodge-podge or jibber-jabber you'd prefer to keep out of more focused work-related channels.",
      |                "creator": "",
      |                "last_set": 0
      |            },
      |            "previous_names": [],
      |            "num_members": 2
      |        },
      |        {
      |            "id": "C0G9QKBBL",
      |            "name": "general",
      |            "is_channel": true,
      |            "created": 1449709280,
      |            "creator": "U0G9QF9C6",
      |            "is_archived": false,
      |            "is_general": true,
      |            "name_normalized": "general",
      |            "is_shared": false,
      |            "is_org_shared": false,
      |            "is_member": true,
      |            "is_private": false,
      |            "is_mpim": false,
      |            "members": [
      |                "U0G9QF9C6",
      |                "U0G9WFXNZ"
      |            ],
      |            "topic": {
      |                "value": "Talk about anything!",
      |                "creator": "U0G9QF9C6",
      |                "last_set": 1449709364
      |            },
      |            "purpose": {
      |                "value": "To talk about anything!",
      |                "creator": "U0G9QF9C6",
      |                "last_set": 1449709334
      |            },
      |            "previous_names": [],
      |            "num_members": 2
      |        }
      |    ],
      |    "response_metadata": {
      |        "next_cursor": "dGVhbTpDMUg5UkVTR0w="
      |    }
      |}
    """.stripMargin
}
