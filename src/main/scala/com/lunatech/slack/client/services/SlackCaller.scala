package com.lunatech.slack.client.services

import akka.stream.ActorMaterializer
import play.api.libs.json.JsValue
import play.api.libs.ws.StandaloneWSResponse
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}

class SlackCaller(token: String) {
  implicit val materializer = ActorMaterializer()

  val wsClient = StandaloneAhcWSClient()

  def makeApiCall(url: String, body: JsValue)(implicit ec: ExecutionContext) : Future[StandaloneWSResponse] = {
    wsClient.url(url)
      .withHttpHeaders(
        "Content-Type" -> "application/json",
        "Authorization" -> s"Bearer $token")
      .post(body)
  }

  def makeGetApiCall(url: String, params: Map[String, String])(implicit ec: ExecutionContext) : Future[StandaloneWSResponse] = {
    wsClient.url(url)
      .withHttpHeaders(
        "Content-Type" -> "application/json",
        "Authorization" -> s"Bearer $token")
      .withQueryStringParameters(params.toSeq: _*)
      .get()
  }

}
