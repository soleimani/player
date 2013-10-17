package com.player.test.techs
import scala.util.{ Success, Failure }
import scala.concurrent.ExecutionContext.Implicits.global
import org.junit.Test

class Future {

  @Test
  def run() {
    val value1 = scala.concurrent.Future {
      println( "enter to value1" )
      println( "thread id: " + Thread.currentThread().getId() )
      Thread.sleep( 3000 )
      println( "exit value1" )
      1
    }

    val value2 = scala.concurrent.Future {
      println( "enter to value2" )
      println( "thread id: " + Thread.currentThread().getId() )
      Thread.sleep( 3000 )
      println( "exit value2" )
      throw new Exception( "error value2" )
      1
    }

    val value3 = scala.concurrent.Future {
      println( "enter to value3" )
      println( "thread id: " + Thread.currentThread().getId() )
      Thread.sleep( 3000 )
      println( "exit value3" )
      throw new Exception( "error value3" )
      1
    }    
    println( value1 )
    println( "thread id: " + Thread.currentThread().getId() )

    value1 onSuccess {
      case i => {
        println( "enter success" )
        println( i )
        println( "exit success" )
      }
    }

    value2 onComplete {
      case Failure( i ) => {
        println( "enter failure" )
        println( i )
        println( "exit failure" )
      }
      case Success( i ) => {
        println( "enter success" )
        println( i )
        println( "exit success" )
      }
    }


    println( "exit main" )
    Thread.sleep( 4000 )
  }

}