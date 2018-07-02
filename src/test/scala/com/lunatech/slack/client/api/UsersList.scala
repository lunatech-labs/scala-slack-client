package com.lunatech.slack.client.api

import com.lunatech.slack.client.models._
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class UsersList extends FlatSpec {

  it should "parse json correctly to UserList" in {
    val spenglerProfile = Profile(
      avatar_hash = Some("ge3b51ca72de"),
      status_text = Some("Print is dead"),
      status_emoji = Some(":books:"),
      real_name = Some("Egon Spengler"),
      display_name = Some("spengler"),
      real_name_normalized = Some("Egon Spengler"),
      display_name_normalized = Some("spengler"),
      email = Some("spengler@ghostbusters.example.com"),
      team = Some("T012AB3C4")
    )

    val spengler = User(
      id = "W012A3CDE",
      team_id = Some("T012AB3C4"),
      name = Some("spengler"),
      deleted = Some(false),
      color = Some("9f69e7"),
      real_name = Some("spengler"),
      tz = Some("America/Los_Angeles"),
      tz_label = Some("Pacific Daylight Time"),
      tz_offset = Some(-25200),
      profile = Some(spenglerProfile),
      is_admin = Some(true),
      is_owner = Some(false),
      is_primary_owner = Some(false),
      is_app_user = Some(false),
      is_bot = Some(false),
      is_restricted = Some(false),
      is_ultra_restricted = Some(false),
      updated = Some(1502138686),
      has_2fa = Some(false)
    )

    val glindaProfile = Profile(
      avatar_hash = Some("8fbdd10b41c6"),
      first_name = Some("Glinda"),
      last_name = Some("Southgood"),
      title = Some("Glinda the Good"),
      phone = Some(""),
      skype = Some(""),
      real_name = Some("Glinda Southgood"),
      real_name_normalized = Some("Glinda Southgood"),
      display_name = Some("Glinda the Fairly Good"),
      display_name_normalized = Some("Glinda the Fairly Good"),
      email = Some("glenda@south.oz.coven")
    )

    val glinda = User(
      id = "W07QCRPA4",
      team_id = Some("T0G9PQBBK"),
      name = Some("glinda"),
      deleted = Some(false),
      color = Some("9f69e7"),
      real_name = Some("Glinda Southgood"),
      tz = Some("America/Los_Angeles"),
      tz_label = Some("Pacific Daylight Time"),
      tz_offset = Some(-25200),
      profile = Some(glindaProfile),
      is_admin = Some(true),
      is_owner = Some(false),
      is_primary_owner = Some(false),
      is_bot = Some(false),
      is_restricted = Some(false),
      is_ultra_restricted = Some(false),
      updated = Some(1480527098),
      has_2fa = Some(false)
    )

    val res = SlackClient.jsonToClass[com.lunatech.slack.client.models.UsersList](json)

    res.isSuccess shouldBe true
    res.get.cache_ts shouldBe Some(1498777272)
    res.get.response_metadata shouldBe Some(Metadata(next_cursor = Some("dXNlcjpVMEc5V0ZYTlo=")))
    res.get.members.get.length shouldBe 2
    res.get.members.get.head shouldBe spengler
    res.get.members.get.head.profile shouldBe Some(spenglerProfile)
    res.get.members.get(1) shouldBe glinda
    res.get.members.get(1).profile shouldBe Some(glindaProfile)
  }

  it should "send an error" in {
    val json =
      """
        |{
        |    "ok": false,
        |    "error": "invalid_cursor"
        |}
      """.stripMargin

    val res = SlackClient.jsonToClass[com.lunatech.slack.client.models.UsersList](json)

    res.isFailure shouldBe true
    res.failed.get.getMessage shouldBe "Error: invalid_cursor"
  }

  private def json =
    """
      |{
      |    "ok": true,
      |    "members": [
      |        {
      |            "id": "W012A3CDE",
      |            "team_id": "T012AB3C4",
      |            "name": "spengler",
      |            "deleted": false,
      |            "color": "9f69e7",
      |            "real_name": "spengler",
      |            "tz": "America/Los_Angeles",
      |            "tz_label": "Pacific Daylight Time",
      |            "tz_offset": -25200,
      |            "profile": {
      |                "avatar_hash": "ge3b51ca72de",
      |                "status_text": "Print is dead",
      |                "status_emoji": ":books:",
      |                "real_name": "Egon Spengler",
      |                "display_name": "spengler",
      |                "real_name_normalized": "Egon Spengler",
      |                "display_name_normalized": "spengler",
      |                "email": "spengler@ghostbusters.example.com",
      |                "image_24": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |                "image_32": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |                "image_48": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |                "image_72": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |                "image_192": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |                "image_512": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |                "team": "T012AB3C4"
      |            },
      |            "is_admin": true,
      |            "is_owner": false,
      |            "is_primary_owner": false,
      |            "is_restricted": false,
      |            "is_ultra_restricted": false,
      |            "is_bot": false,
      |            "updated": 1502138686,
      |            "is_app_user": false,
      |            "has_2fa": false
      |        },
      |        {
      |            "id": "W07QCRPA4",
      |            "team_id": "T0G9PQBBK",
      |            "name": "glinda",
      |            "deleted": false,
      |            "color": "9f69e7",
      |            "real_name": "Glinda Southgood",
      |            "tz": "America/Los_Angeles",
      |            "tz_label": "Pacific Daylight Time",
      |            "tz_offset": -25200,
      |            "profile": {
      |                "avatar_hash": "8fbdd10b41c6",
      |                "image_24": "https://a.slack-edge.com...png",
      |                "image_32": "https://a.slack-edge.com...png",
      |                "image_48": "https://a.slack-edge.com...png",
      |                "image_72": "https://a.slack-edge.com...png",
      |                "image_192": "https://a.slack-edge.com...png",
      |                "image_512": "https://a.slack-edge.com...png",
      |                "image_1024": "https://a.slack-edge.com...png",
      |                "image_original": "https://a.slack-edge.com...png",
      |                "first_name": "Glinda",
      |                "last_name": "Southgood",
      |                "title": "Glinda the Good",
      |                "phone": "",
      |                "skype": "",
      |                "real_name": "Glinda Southgood",
      |                "real_name_normalized": "Glinda Southgood",
      |                "display_name": "Glinda the Fairly Good",
      |                "display_name_normalized": "Glinda the Fairly Good",
      |                "email": "glenda@south.oz.coven"
      |            },
      |            "is_admin": true,
      |            "is_owner": false,
      |            "is_primary_owner": false,
      |            "is_restricted": false,
      |            "is_ultra_restricted": false,
      |            "is_bot": false,
      |            "updated": 1480527098,
      |            "has_2fa": false
      |        }
      |    ],
      |    "cache_ts": 1498777272,
      |    "response_metadata": {
      |        "next_cursor": "dXNlcjpVMEc5V0ZYTlo="
      |    }
      |}
    """.stripMargin

}
