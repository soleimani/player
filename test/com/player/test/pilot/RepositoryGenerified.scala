package com.player.test.pilot

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.test.FakeApplication
import play.api.test.Helpers.{ running }
import org.fest.assertions.Assertions.assertThat
import org.junit.Test
import com.player.app.model.Model

class RepositoryGenerified {

  val fakeApp = FakeApplication( additionalConfiguration = Config.inMemoryDatabase )

  object Repository extends com.player.app.repository.GenerifiedPilotComponent {
    val repository = GenerifiedPilotRepository
  }

  @Test
  def test: Unit = running( fakeApp ) {

    Repository.repository.dropTable
    Repository.repository.createTable

    Await.result( Repository.repository.create( Repository.repository.deserialize( Map( "name" -> "test" ) ) ), Duration.Inf )
    val f = Await.result( Repository.repository.find( 1 ), Duration.Inf )
    assertThat( f.get.name ) isEqualTo "test"

    Repository.repository.dropTable

  }

}