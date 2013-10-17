package com.player.test.pilot

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.test.FakeApplication
import play.api.test.Helpers.{ running }
import org.fest.assertions.Assertions.assertThat
import anorm.Pk
import com.player.app.model.{ Pilot ⇒ PilotModel }
import org.junit.Test
import org.mockito.Mockito.{ mock, when }
import org.fusesource.scalate._

import com.player.app.core.Common
import com.player.app.generator.Tool
import com.player.app.Setting
import com.player.app.model.Model

trait ServiceComponent[ M <: Model ] extends com.player.app.service.GenericComponent[ M ] {
  this: com.player.app.repository.api.Component[ M ] ⇒
  def service: Service
}

class ServiceGenerated {

  val fakeApp = FakeApplication( additionalConfiguration = Config.inMemoryDatabase )

  @Test
  def test(): Unit = running( fakeApp ) {
    val engine = new TemplateEngine
    val template = engine.layout( Setting.applicationPath + "app/templates/Service.scala.mustache",
      Map( "modelName" -> "Pilot", "tableName" -> "pilot", "genericPackageName" -> "com.player.test.pilot.ServiceComponent" ) )

    val Service = Tool.createClass[ ServiceComponent[ Model ] ]( template )

    Service.service.createTable
    Service.service.create( Service.service.deserialize( Map( "name" -> "TEST" ) ) )
    val model = Await.result( Service.service.find( 1 ), Duration.Inf )

    assertThat( model.get.name ) isEqualTo "TEST"
  }

}