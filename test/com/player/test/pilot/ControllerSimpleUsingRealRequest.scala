package com.player.test.pilot

import play.api.test.Helpers.{ OK, NOT_FOUND, running, HTMLUNIT, status, contentType, charset, contentAsString, await }
import play.api.test.{ FakeRequest, FakeApplication, TestServer }
import play.api.libs.ws.WS
import scala.concurrent.ExecutionContext.Implicits.global

import org.junit.Test
import org.fest.assertions.Assertions.assertThat

class ControllerSimpleUsingRealRequest {

  lazy val server = TestServer( 3333, FakeApplication( additionalConfiguration = Config.inMemoryDatabase ) )

  @Test
  def index(): Unit = running( server ) {
    val response = await( WS.url( "http://localhost:3333" ).get )
    assertThat( response.status ) isEqualTo OK
  }

  @Test
  def simple(): Unit = running( server ) {
    val response = await( WS.url( "http://localhost:3333/simple" ).get )
    assertThat( response.status ) isEqualTo OK
    assertThat( response.body ) contains "<title>simple</title>"
  }

  @Test
  def simpleWithName(): Unit = running( server ) {
    val response = await( WS.url( "http://localhost:3333/simple/TEST" ).get )
    assertThat( response.status ) isEqualTo OK
    assertThat( response.body ) contains "<title>TEST</title>"
  }

  @Test
  def notFound(): Unit = running( server ) {
    val response = await( WS.url( "http://localhost:3333/not_found" ).get )
    assertThat( response.status ) isEqualTo NOT_FOUND
  }

  //  @Test
  def runInBrowser() {

    running( TestServer( 3333 ), HTMLUNIT ) { browser =>

      browser.goTo( "http://localhost:3333" )
      assertThat( browser.$( "#title" ).getTexts().get( 0 ) ).isEqualTo( "Hello Guest" )

      browser.$( "a" ).click()

      assertThat( browser.url ).isEqualTo( "http://localhost:3333/Coco" )
      assertThat( browser.$( "#title" ).getTexts().get( 0 ) ).isEqualTo( "Hello Coco" )

    }
  }

}