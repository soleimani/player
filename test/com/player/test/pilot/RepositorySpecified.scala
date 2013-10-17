package com.player.test.pilot

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.test.FakeApplication
import play.api.test.Helpers.{ running }
import org.fest.assertions.Assertions.assertThat

import com.player.app.model.{ Pilot ⇒ PilotModel }
import org.junit.Test

class RepositorySpecified {

  val fakeAppDefaultDB = FakeApplication( additionalConfiguration = Config.fakeDB )
  val fakeAppInMemoryDB = FakeApplication( additionalConfiguration = Config.inMemoryDatabase )
  val fakeAppConfigDB = FakeApplication( additionalConfiguration =
    Map( "db.default.driver" -> "com.mysql.jdbc.Driver",
      "db.default.url" -> "jdbc:mysql://localhost/player",
      "db.default.user" -> "root",
      "db.default.password" -> "1111" ) ++ Config.fakeDB )

  object Repository extends com.player.app.repository.PilotComponent {
    val repository = PilotRepository
  }

  def test( fakeApp: FakeApplication ): Unit = running( fakeApp ) {

    Repository.repository.dropTable
    Repository.repository.createTable

    var m = new PilotModel( null, "TEST1" )
    Await.result( Repository.repository.create( m ), Duration.Inf )
    val f = Await.result( Repository.repository.find( 1 ), Duration.Inf )
    assertThat( f.get.name ) isEqualTo "TEST1"

    Repository.repository.dropTable

  }

  @Test
  def testDefaultDB() {
    test( fakeAppDefaultDB )
  }

  @Test
  def testConfigDB() {
    test( fakeAppConfigDB )
  }

  @Test
  def testFakeDB(): Unit = running( fakeAppInMemoryDB ) {

    Repository.repository.createTable

    var m = new PilotModel( null, "TEST1" )
    Await.result( Repository.repository.create( m ), Duration.Inf )

    var f = Await.result( Repository.repository.find( 1 ), Duration.Inf )
    assertThat( f.get.name ) isEqualTo "TEST1"

    m = new PilotModel( null, "TEST2" )
    Await.result( Repository.repository.create( m ), Duration.Inf )

    f = Await.result( Repository.repository.find( 2 ), Duration.Inf )
    assertThat( f.get.name ) isEqualTo "TEST2"

  }
}