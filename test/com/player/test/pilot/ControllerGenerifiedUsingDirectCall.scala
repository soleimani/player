package com.player.test.pilot

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.test.{ FakeRequest, FakeApplication }
import play.api.test.Helpers.{ OK, NOT_FOUND, status, contentType, contentAsString }
import org.fest.assertions.Assertions.assertThat

import anorm.NotAssigned

import com.player.app.model.{ Pilot ⇒ PilotModel }
import org.junit.Test

import org.mockito.Mockito.{ mock, when }

class ControllerGenerifiedUsingDirectCall {

  object Controller extends com.player.app.controller.GenerifiedPilot
      with com.player.app.service.api.Component[ PilotModel ] {
    val service = mock( classOf[ Service ] )
    val model = PilotModel( anorm.Id( 1 ), "test" )

    when( service.find( 1 ) ).thenReturn( Future{ Some( model ) } )
    when( service.find( 2 ) ).thenReturn( Future{ None } )
  }

  @Test
  def findOk(): Unit = {
    val result = Controller.read( 1 )( FakeRequest() )
    assertThat( status( result ) ) isEqualTo OK
    assertThat( contentType( result ).get ) isEqualTo "application/json"
    assertThat( contentAsString( result ) ) isEqualTo """{"id":1,"name":"test"}"""
  }

  @Test
  def findNotFound(): Unit = {
    val result = Controller.read( 2 )( FakeRequest() )
    assertThat( status( result ) ) isEqualTo NOT_FOUND
    assertThat( contentType( result ) ) isEqualTo None
    assertThat( contentAsString( result ) ) isEqualTo ""

  }

}