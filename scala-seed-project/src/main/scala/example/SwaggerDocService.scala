// src/main/scala/com/example/SwaggerDocService.scala
package com.example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses: Set[Class[_]] = Set(classOf[WebServer])
  override val host = "localhost:8080"
  override val info = Info(version = "1.0")
}
