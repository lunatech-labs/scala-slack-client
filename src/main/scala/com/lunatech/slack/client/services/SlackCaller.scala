package com.lunatech.slack.client.services

import akka.stream.Materializer
import com.lunatech.slack.client.api.SlackClient
import play.api.libs.json.{JsValue, Reads}
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class SlackCaller(implicit materializer: Materializer) {

  val wsClient = StandaloneAhcWSClient()

  def makeApiCall[T](token: String, url: String, body: JsValue)(implicit ec: ExecutionContext, reads: Reads[T]): Future[T] = {
    wsClient.url(url)
      .withHttpHeaders(
        "Content-Type" -> "application/json",
        "Authorization" -> s"Bearer $token")
      .post(body)
      .flatMap(x => SlackClient.jsonToClass[T](x.body) match {
        case Success(c) => Future.successful(c)
        case Failure(error) => Future.failed(error)
      })
  }

  def makeGetApiCall[T](token: String, url: String, params: Map[String, String])(implicit ec: ExecutionContext, reads: Reads[T]): Future[T] = {
    wsClient.url(url)
      .withHttpHeaders(
        "Content-Type" -> "application/json",
        "Authorization" -> s"Bearer $token")
      .withQueryStringParameters(params.toSeq: _*)
      .get()
      .flatMap(x => SlackClient.jsonToClass[T](x.body) match {
        case Success(c) => Future.successful(c)
        case Failure(error) => Future.failed(error)
      })
  }
}