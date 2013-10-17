package com.player.test.app.generator

import play.api.test.Helpers.{ running, inMemoryDatabase, await }
import play.api.test.{ FakeApplication }
import org.fest.assertions.Assertions.assertThat
import org.junit.Test
import com.player.app.generator.Tool
import com.player.app.Setting
import com.player.app.generator.Model

trait Generic {
  def get: Int
  def set( aa: Int ): Unit
}

class Generator {

  val fakeApp = FakeApplication( additionalConfiguration = inMemoryDatabase() )

  @Test
  def create: Unit = {
    val o = Tool.createClass[ Generic ]( """
        case class C(var a: Int) extends com.player.test.app.generator.Generic {
          def get = a;
          def set(aa:Int) = a = aa;
        };
        C(1)""" );
    assertThat ( o.get ) isEqualTo 1
  }

  @Test
  def createModel: Unit = running( fakeApp ){

    Model.generate( "pilot" )

    //    println( Play.current.path.getCanonicalPath() + "/setting" )
    //    val o = Tool.createObject[ Generic ]( """
    //        case class C(var a: Int) extends com.player.test.app.generator.Generic {
    //          def get = a;
    //          def set(aa:Int) = a = aa;
    //        };
    //        C(1)""");
    //    assertThat (o.get) isEqualTo 1

  }

  @Test
  def reflection1: Unit = {
    import scala.reflect.runtime.{ universe ⇒ ru }
    val l = List( 1, 2, 3 )
    def getTypeTag[ T: ru.TypeTag ]( obj: T ) = ru.typeTag[ T ]
    val theType = getTypeTag( l ).tpe

    println( theType )
  }

}


