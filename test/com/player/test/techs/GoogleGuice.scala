package com.player.test.techs

import com.google.inject.{ Inject, Module, Binder, Guice }

object GoogleGuice {

  def main( args: Array[ String ] ): Unit = {

    // =======================
    // service interfaces
    trait OnOffDevice {
      def on: Unit
      def off: Unit
    }
    trait SensorDevice {
      def isCoffeePresent: Boolean
    }
    trait IWarmer {
      def trigger
    }

    // =======================
    // service implementations
    class Heater extends OnOffDevice {
      def on = println( "heater.on" )
      def off = println( "heater.off" )
    }
    class PotSensor extends SensorDevice {
      def isCoffeePresent = true
    }
    class Warmer @Inject() (
      val potSensor: SensorDevice,
      val heater: OnOffDevice )
        extends IWarmer {

      def trigger = {
        if ( potSensor.isCoffeePresent ) heater.on
        else heater.off
      }
    }

    // =======================
    // Guice's configuration class that is defining the 
    // interface-implementation bindings 
    class DependencyModule extends Module {
      def configure( binder: Binder ) = {
        binder.bind( classOf[ OnOffDevice ] ).to( classOf[ Heater ] )
        binder.bind( classOf[ SensorDevice ] ).to( classOf[ PotSensor ] )
        binder.bind( classOf[ IWarmer ] ).to( classOf[ Warmer ] )
      }
    }

    // helper companion object 
    object ServiceInjector {
      val injector = Guice.createInjector( new DependencyModule )
    }

    // =======================
    // mix-in the ServiceInjector trait to perform injection
    // upon instantiation
    val warmer = ServiceInjector.injector.getInstance( classOf[ IWarmer ] )

    warmer.trigger
  }

}