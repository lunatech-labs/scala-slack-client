package com.lunatech.slack.client.models

import play.api.libs.json._


sealed trait SlackResponse[T]

case class SlackSuccess[T](response: T) extends SlackResponse[T]

case class SlackError[T](error: String) extends SlackResponse[T]

object SlackResponse {

  implicit def readSuccess[T](implicit reader: Reads[T]): Reads[SlackSuccess[T]] =
    json => reader.reads(json).map(SlackSuccess(_))

  implicit def readError[T]: Reads[SlackError[T]] = Json.format[SlackError[T]]

  implicit def read[T](implicit reader: Reads[T]): Reads[SlackResponse[T]] = json => (json \ "ok").validate[Boolean] flatMap {
    case true => readSuccess[T].reads(json)
    case false => readError[T].reads(json)
  }
}