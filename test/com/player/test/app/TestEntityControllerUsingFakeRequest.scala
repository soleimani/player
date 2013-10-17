package com.player.test.app

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import play.api.test.Helpers.{ OK, NOT_FOUND, status, contentType, charset, contentAsString, inMemoryDatabase, running, POST, PUT, await }
import play.api.test.{ FakeApplication, FakeHeaders, FakeRequest }
import play.api.http.HeaderNames
import play.api.libs.json.{ Json, JsString }

import org.junit.Test
import org.fest.assertions.Assertions.assertThat
import anorm.NotAssigned

class TestEntityControllerUsingFakeRequest {

  val fakeApp = FakeApplication( additionalConfiguration = inMemoryDatabase() )

  object Service extends com.player.app.service.expose.GenerifiedPilotComponent {
    val service = GenerifiedPilotService
  }

  @Test
  def createThenGet(): Unit = running( fakeApp ) {
    Service.service.createTable
    val createResult = Await.result( Service.service.create( null ), Duration.Inf )
    val result = com.player.app.controller.expose.Dispatcher.read( "", createResult )( FakeRequest() )

    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ).get ) isEqualTo "application/json"
    assertThat( contentAsString( result ) ) isEqualTo """{"id":1,"name":"test"}"""
    Service.service.dropTable
  }

  @Test
  def createThenDelete(): Unit = running( fakeApp ) {
    Service.service.createTable
    val createResult = Await.result( Service.service.create( null ), Duration.Inf )
    val result = com.player.app.controller.expose.Dispatcher.delete( "", createResult )( FakeRequest() )

    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentAsString( result ) ) isEqualTo "deleted " + createResult
    Service.service.dropTable
  }

  @Test
  def create: Unit = running( fakeApp ) {
    Service.service.createTable
    val json = Json.obj( "name" -> "Testname" )
    val request = FakeRequest(
      method = POST,
      uri = com.player.app.controller.expose.routes.Dispatcher.create( "process" ).url,
      headers = FakeHeaders(
        Seq( "Content-type" -> Seq( "application/json" ) ) ),
      body = json )
    val result = com.player.app.controller.expose.Dispatcher.create( "" )( request )

    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ) ) isEqualTo Some( "text/plain" )
    assertThat( charset( result ) ) isEqualTo Some( "utf-8" )
    assertThat( contentAsString( result ) ) contains "created id : 1"
    Service.service.dropTable
  }

}

