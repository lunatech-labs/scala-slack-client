package com.lunatech.slack.client.api

import com.lunatech.slack.client.models._
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class UserLookupByEmail extends FlatSpec {

  it should "parse json correctly to User" in {
    val res = SlackClient.jsonToClass[UserInfo](json)

    val profile = Profile(
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

    val user = User(
      id = "W012A3CDE",
      team_id = Some("T012AB3C4"),
      name = Some("spengler"),
      deleted = Some(false),
      color = Some("9f69e7"),
      real_name = Some("Egon Spengler"),
      tz = Some("America/Los_Angeles"),
      tz_label = Some("Pacific Daylight Time"),
      tz_offset = Some(-25200),
      profile = Some(profile),
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

    res.isSuccess shouldBe true
    res.get.user shouldBe user
    res.get.user.profile.get shouldBe profile
  }

  it should "send an error" in {
    val json =
      """{
      "ok": false,
      "error": "users_not_found"
    }""".stripMargin

    val res = SlackClient.jsonToClass[UserInfo](json)

    res.isFailure shouldBe true
    res.failed.get.getMessage shouldBe "Error: users_not_found"
  }

  // Response from server
  private def json =
    """
      |{
      |    "ok": true,
      |    "user": {
      |        "id": "W012A3CDE",
      |        "team_id": "T012AB3C4",
      |        "name": "spengler",
      |        "deleted": false,
      |        "color": "9f69e7",
      |        "real_name": "Egon Spengler",
      |        "tz": "America/Los_Angeles",
      |        "tz_label": "Pacific Daylight Time",
      |        "tz_offset": -25200,
      |        "profile": {
      |            "avatar_hash": "ge3b51ca72de",
      |            "status_text": "Print is dead",
      |            "status_emoji": ":books:",
      |            "real_name": "Egon Spengler",
      |            "display_name": "spengler",
      |            "real_name_normalized": "Egon Spengler",
      |            "display_name_normalized": "spengler",
      |            "email": "spengler@ghostbusters.example.com",
      |            "image_24": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |            "image_32": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |            "image_48": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |            "image_72": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |            "image_192": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |            "image_512": "https://.../avatar/e3b51ca72dee4ef87916ae2b9240df50.jpg",
      |            "team": "T012AB3C4"
      |        },
      |        "is_admin": true,
      |        "is_owner": false,
      |        "is_primary_owner": false,
      |        "is_restricted": false,
      |        "is_ultra_restricted": false,
      |        "is_bot": false,
      |        "updated": 1502138686,
      |        "is_app_user": false,
      |        "has_2fa": false
      |    }
      |}
      |
    """.stripMargin

}
