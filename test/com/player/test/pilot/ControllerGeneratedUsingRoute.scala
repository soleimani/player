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
  await,
  inMemoryDatabase
}
import play.mvc.Result
import org.junit.Test
import org.fest.assertions.Assertions.assertThat
import play.api.libs.json.JsValue
import scala.reflect.io.File
import play.api.test.FakeHeaders
import play.api.libs.json.{ Json, JsString }
import com.player.app.Setting

class ControllerGeneratedUsingRoute {

  val fakeApp = FakeApplication( additionalConfiguration = inMemoryDatabase() )

  @Test
  def create(): Unit = running( fakeApp ) {
    Setting.service( "pilot" ).service.createTable;
    val json = Json.obj( "name" -> "A" )

    val created = route( FakeRequest(
      method = "POST",
      uri = "/entity/pilot",
      headers = FakeHeaders(
        Seq( "Content-type" -> Seq( "application/json" ) ) ),
      body = json ) ).get
    assertThat( status( created ) ) isEqualTo OK
    assertThat( contentType( created ) ) isEqualTo Some( "text/plain" )
    assertThat( charset( created ) ) isEqualTo Some( "utf-8" )
    assertThat( contentAsString( created ) ) contains "created id : 1"

    val selected = route( FakeRequest(
      method = "GET",
      uri = "/entity/pilot/1",
      headers = FakeHeaders(),
      body = "" ) ).get
    assertThat( status( selected ) ) isEqualTo OK
    assertThat( contentType( selected ) ) isEqualTo Some( "application/json" )
    assertThat( contentAsString( selected ) ) contains """{"id":1,"name":"A"}"""

    Setting.service( "pilot" ).service.dropTable
  }

}

