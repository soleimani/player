package com.player.test.pilot

import play.api.http.{ ContentTypes }
import play.api.test.{ FakeApplication, FakeRequest, FakeHeaders }
import play.api.test.Helpers.{
  POST,
  GET,
  OK,
  status,
  contentType,
  charset,
  contentAsString,
  route,
  running,
  await
}
import play.mvc.Result
import org.junit.Test
import org.fest.assertions.Assertions.assertThat
import play.api.libs.json.JsValue
import scala.reflect.io.File
import play.api.test.FakeHeaders
import play.api.libs.json.{ Json, JsString }

class ControllerSimpleUsingRoute {

  val fakeApp = FakeApplication( additionalConfiguration = Config.inMemoryDatabase )

  object Repository extends com.player.app.repository.PilotComponent {
    val repository = PilotRepository
  }

  @Test
  def routeSimple(): Unit = running( fakeApp ) {
    val result = route( FakeRequest( GET, "/simple" ), "" ).get
    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ) ) isEqualTo Some( "text/html" )
    assertThat( charset( result ) ) isEqualTo Some( "utf-8" )
    assertThat( contentAsString( result ) ) contains "<title>simple</title>"
  }

  @Test
  def routeSimpleName(): Unit = running( fakeApp ) {
    val result = route( FakeRequest( GET, "/simple/Name" ), "" ).get
    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ) ) isEqualTo Some( "text/html" )
    assertThat( charset( result ) ) isEqualTo Some( "utf-8" )
    assertThat( contentAsString( result ) ) contains "<title>Name</title>"
  }

  @Test
  def routeNotFound(): Unit = running( fakeApp ) {
    val result = route( FakeRequest( GET, "/NotFound" ), "" )
    assertThat( result ) isEqualTo None
  }

  @Test
  def createTest(): Unit = running( fakeApp ) {
    Repository.repository.createTable
    val json = Json.obj( "name" -> "A" )

    val request = FakeRequest(
      method = "POST",
      uri = "/pilot",
      headers = FakeHeaders(
        Seq( "Content-type" -> Seq( "application/json" ) ) ),
      body = json )

    val result = route( request ).get
    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ) ) isEqualTo Some( "text/plain" )
    assertThat( charset( result ) ) isEqualTo Some( "utf-8" )
    assertThat( contentAsString( result ) ) contains "created id : 1"
    Repository.repository.dropTable
  }

}

