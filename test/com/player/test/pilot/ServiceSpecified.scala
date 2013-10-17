package com.player.test.pilot

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.test.FakeApplication
import play.api.test.Helpers.{ running }
import org.fest.assertions.Assertions.assertThat

import anorm.Pk

import com.player.app.model.{ Pilot ⇒ PilotModel }
import org.junit.Test

import org.mockito.Mockito.{ mock, when }

class ServiceSpecified {

  object Service extends com.player.app.service.PilotComponent
      with com.player.app.repository.api.Component[ PilotModel ] {
    val repository = mock( classOf[ Repository ] )
    val service = PilotService

    val model = PilotModel( anorm.Id( 1 ), "test" )
    when( repository.find( 1 ) ).thenReturn( Future( Some( model ) ) )
  }

  @Test
  def find() {
    val result = Await.result( Service.service.find( 1 ), Duration.Inf )
    assertThat ( result.get.id.get ) isEqualTo 1
    assertThat ( result.get.name ) isEqualTo "test"
  }

}