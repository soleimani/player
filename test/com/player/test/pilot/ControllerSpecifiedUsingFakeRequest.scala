package com.player.test.pilot

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.test.Helpers.{ OK, NOT_FOUND, status, contentType, charset, contentAsString, running, POST, PUT, await }
import play.api.test.{ FakeApplication, FakeHeaders, FakeRequest }
import play.api.http.HeaderNames
import play.api.libs.json.{ Json, JsString }

import org.junit.Test
import org.fest.assertions.Assertions.assertThat
import anorm.NotAssigned

class ControllerSpecifiedUsingFakeRequest {

  val fakeApp = FakeApplication( additionalConfiguration = Config.inMemoryDatabase )

  object Service extends com.player.app.service.expose.PilotComponent {
    val service = PilotService
  }

  @Test
  def createThenGet(): Unit = running( fakeApp ) {
    Service.service.createTable
    val createResult = Await.result( Service.service.create( new com.player.app.model.Pilot( NotAssigned, "test" ) ), Duration.Inf )
    val result = com.player.app.controller.expose.Pilot.read( createResult )( FakeRequest() )

    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ).get ) isEqualTo "application/json"
    assertThat( contentAsString( result ) ) isEqualTo """{"id":1,"name":"test"}"""
    Service.service.dropTable
  }

  @Test
  def createThenDelete(): Unit = running( fakeApp ) {
    Service.service.createTable
    val createResult = Await.result( Service.service.create( new com.player.app.model.Pilot( NotAssigned, "test" ) ), Duration.Inf )
    val result = com.player.app.controller.expose.Pilot.delete( createResult )( FakeRequest() )

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
      uri = com.player.app.controller.expose.routes.Pilot.create.url,
      headers = FakeHeaders(
        Seq( "Content-type" -> Seq( "application/json" ) ) ),
      body = json )
    val result = com.player.app.controller.expose.Pilot.create()( request )

    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ) ) isEqualTo Some( "text/plain" )
    assertThat( charset( result ) ) isEqualTo Some( "utf-8" )
    assertThat( contentAsString( result ) ) contains "created id : 1"
    Service.service.dropTable
  }

  //  @Test
  def testCreateThenUpdate(): Unit = running( fakeApp ) {

    Service.service.createTable
    val createResult = Await.result( Service.service.create( new com.player.app.model.Pilot( NotAssigned, "test" ) ), Duration.Inf )
    var f = FakeRequest( PUT, "" ).withJsonBody( Json.toJson( Map( "name" -> "TEST" ) ) )

    val result = com.player.app.controller.expose.Pilot.update( createResult )( f )
    println( result )
    //    assertThat(status(result)) isEqualTo OK
    //    assertThat(contentType(result).get) isEqualTo "application/json"
    //    assertThat(contentAsString(result)) isEqualTo """{"id":1,"name":"test"}"""
    //    com.player.app.service.Test.dropTable

  }
}


/*
"POST createGroup with JSON" should {
  "create a group and return a message" in {
    implicit val app = FakeApplication()
    running(app) {
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.ApplicationController.createGroup().url, FakeHeaders(), """ {"name": "New Group", "collabs": ["foo", "asdf"]} """)

      val result = controllers.ApplicationController.createGroup()(fakeRequest).result.value.get

      status(result) must equalTo(OK)
      contentType(result) must beSome(AcceptExtractors.Accepts.Json.mimeType)

      val message = Region.parseJson(contentAsString(result))

      // test the message response
    }
  }
}
"respond to the register Action" in {
    val requestNode = Json.toJson(Map("name" -> "Testname"))
    val request = FakeRequest().copy(body = requestNode)
        .withHeaders(HeaderNames.CONTENT_TYPE -> "application/json");
    val result = controllers.Users.register()(request)

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    charset(result) must beSome("utf-8")

    val responseNode = Json.parse(contentAsString(result))
    (responseNode \ "success").as[Boolean] must equalTo(true)
  }
*/
