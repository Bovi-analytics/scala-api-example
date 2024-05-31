// src/main/scala/com/example/WebServer.scala
package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.concurrent.Future

import scala.io.StdIn

object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("webserver-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      path("file") {
        get {
          val fileUrl = "https://raw.githubusercontent.com/username/repo/branch/filename.txt"
          val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = fileUrl))

          onComplete(responseFuture) {
            case Success(response) => complete(response)
            case Failure(_)        => complete(StatusCodes.InternalServerError)
          }
        }
      }



    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
