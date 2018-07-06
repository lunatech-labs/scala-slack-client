package com.lunatech.slack.client.api

import com.lunatech.slack.client.models._
import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import play.api.libs.json._


class ActionFieldTest extends FlatSpec {

  it should "parse a button to json correctly" in {
    val button: ActionField = Button("Default button", "button").withId("d_button").withValue("v")
    val primaryButton: ActionField = Button("Primary button", "p_button").asPrimaryButton
    val dangerButton: ActionField = Button("Danger button", "d_button").asDangerButton.withConfirmation("Are you sure ?")

    val buttonJson = Json.obj(
      "name" -> "Default button",
      "text" -> "button",
      "type" -> "button",
      "value" -> "v",
      "id" -> "d_button"
    )

    val primaryButtonJson = Json.obj(
      "name" -> "Primary button",
      "text" -> "p_button",
      "type" -> "button",
      "style" -> "primary"
    )

    val dangerButtonJson = Json.obj(
      "name" -> "Danger button",
      "text" -> "d_button",
      "type" -> "button",
      "style" -> "danger",
      "confirm" -> ConfirmField("Are you sure ?")
    )

    Json.toJson(button) shouldBe buttonJson
    Json.toJson(primaryButton) shouldBe primaryButtonJson
    Json.toJson(dangerButton) shouldBe dangerButtonJson
  }

  it should "convert Json to button correctly" in {

    val buttonJson = Json.obj(
      "name" -> "Default button",
      "text" -> "button",
      "type" -> "button",
      "value" -> "v",
      "id" -> "d_button"
    )

    val primaryButtonJson = Json.obj(
      "name" -> "Primary button",
      "text" -> "p_button",
      "type" -> "button",
      "style" -> "primary"
    )

    val dangerButtonJson = Json.obj(
      "name" -> "Danger button",
      "text" -> "d_button",
      "type" -> "button",
      "style" -> "danger",
      "confirm" -> ConfirmField("Are you sure ?")
    )

    val button: JsResult[ActionField] = Json.fromJson[ActionField](buttonJson)
    val buttonExpected: ActionField = Button("Default button", "button").withId("d_button").withValue("v")

    button match {
      case JsSuccess(s: Button, _) =>
        s shouldBe buttonExpected
      case _ => fail
    }

    val primaryButton: JsResult[ActionField] = Json.fromJson[ActionField](primaryButtonJson)
    val primaryButtonExpected: ActionField = Button("Primary button", "p_button").asPrimaryButton

    primaryButton match {
      case JsSuccess(s: Button, _) =>
        s shouldBe primaryButtonExpected
      case _ => fail
    }

    val dangerButton: JsResult[ActionField] = Json.fromJson[ActionField](dangerButtonJson)
    val dangerButtonExpected: ActionField = Button("Danger button", "d_button").asDangerButton.withConfirmation("Are you sure ?")

    dangerButton match {
      case JsSuccess(s: Button, _) =>
        s shouldBe dangerButtonExpected
        s.confirm shouldBe Some(ConfirmField("Are you sure ?"))
      case _ => fail
    }

  }

  it should "build a menu correctly into json" in {
    val menu: ActionField = StaticMenu("games_list", "Pick a game...")
      .addOption("Hearts", "hearts", select = true)
      .addOption("Bridge", "bridge")
      .addOption("Checkers", "checkers")

    val jsonExpected = staticMenuJson

    Json.toJson(menu) shouldBe jsonExpected
  }

  it should "convert a json to static menu" in {
    val json = staticMenuJson

    val menuExpected: ActionField = StaticMenu("games_list", "Pick a game...")
      .addOption("Hearts", "hearts", select = true)
      .addOption("Bridge", "bridge")
      .addOption("Checkers", "checkers")

    val menu = Json.fromJson[Menu](json)

    menu match {
      case JsSuccess(s: StaticMenu, _) =>
        s shouldBe menuExpected
        s.selected_options shouldBe Some(Seq(BasicField("Hearts", "hearts")))
      case _ => fail
    }
  }

  it should "convert a group menu to json" in {
    val firstGroup = OptionGroupsField("Doggone bot antics")
      .addOption("Unexpected sentience", "AI-2323")
      .addOption("Bot biased toward other bots", "SUPPORT-42")
      .addOption("Bot broke my toaster", "IOT-75")

    val secondGroup = OptionGroupsField("Human error")
      .addOption("Not Penny's boat", "LOST-7172")
      .addOption("We built our own CMS", "OOPS-1")

    val groupMenu : ActionField = StaticGroupMenu("not_a_robot", "Errors...")
      .addOptionGroup(firstGroup)
      .addOptionGroup(secondGroup)

    Json.toJson(groupMenu) shouldBe staticGroupMenuJson
  }

  it should "convert json to a group menu" in {
    val firstGroup = OptionGroupsField("Doggone bot antics")
      .addOption("Unexpected sentience", "AI-2323")
      .addOption("Bot biased toward other bots", "SUPPORT-42")
      .addOption("Bot broke my toaster", "IOT-75")

    val secondGroup = OptionGroupsField("Human error")
      .addOption("Not Penny's boat", "LOST-7172")
      .addOption("We built our own CMS", "OOPS-1")

    val groupMenuExpected: ActionField = StaticGroupMenu("not_a_robot", "Errors...")
      .addOptionGroup(firstGroup)
      .addOptionGroup(secondGroup)

    val menu = Json.fromJson[Menu](staticGroupMenuJson)

    menu match {
      case JsSuccess(s: StaticGroupMenu, _) =>
        s shouldBe groupMenuExpected
      case _ => fail
    }
  }

  it should "convert a dynamic menu to json" in {
    val menu: Menu = DynamicMenu("bugs_list", "Which random bug do you want to resolve?", DataSource.external)
      .withMinimalQueryLength(3)

    val jsonExpected = Json.parse(
      """
        |                {
        |                    "name": "bugs_list",
        |                    "text": "Which random bug do you want to resolve?",
        |                    "type": "select",
        |                    "data_source": "external",
        |                    "min_query_length": 3
        |                }
      """.stripMargin)

    Json.toJson(menu) shouldBe jsonExpected
  }

  it should "convert json to dynamic menu" in {

    val json = Json.parse(
      """
        |                {
        |                    "name": "bugs_list",
        |                    "text": "Which random bug do you want to resolve?",
        |                    "type": "select",
        |                    "data_source": "external",
        |                    "min_query_length": 3
        |                }
      """.stripMargin)

    val menuExpected = DynamicMenu("bugs_list", "Which random bug do you want to resolve?", DataSource.external)
      .withMinimalQueryLength(3)

    Json.fromJson[Menu](json) match {
      case JsSuccess(s, _) => s shouldBe menuExpected
      case _ => fail
    }
  }

  private def staticMenuJson: JsValue = Json.parse(
    """
      |{
      |   "name": "games_list",
      |   "text": "Pick a game...",
      |   "type": "select",
      |   "options": [
      |      {
      |           "text": "Hearts",
      |           "value": "hearts"
      |       },
      |       {
      |           "text": "Bridge",
      |           "value": "bridge"
      |       },
      |       {
      |           "text": "Checkers",
      |           "value": "checkers"
      |       }
      |   ],
      |   "selected_options": [
      |       {
      |           "text": "Hearts",
      |           "value": "hearts"
      |       }
      |   ]
      |}
    """.stripMargin)

  private def staticGroupMenuJson: JsValue = Json.parse(
    """
      |{
      |    "name": "not_a_robot",
      |    "text": "Errors...",
      |    "type": "select",
      |    "option_groups": [
      |        {
      |            "text": "Doggone bot antics",
      |            "options": [
      |                    {
      |                        "text": "Unexpected sentience",
      |                        "value": "AI-2323"
      |                    },
      |                    {
      |                        "text": "Bot biased toward other bots",
      |                        "value": "SUPPORT-42"
      |                    },
      |                    {
      |                        "text": "Bot broke my toaster",
      |                        "value": "IOT-75"
      |                    }
      |            ]
      |        },
      |        {
      |            "text": "Human error",
      |            "options": [
      |                {
      |                    "text": "Not Penny's boat",
      |                    "value": "LOST-7172"
      |                },
      |                {
      |                    "text": "We built our own CMS",
      |                    "value": "OOPS-1"
      |                }
      |            ]
      |        }
      |    ]
      |}
    """.stripMargin)
}
