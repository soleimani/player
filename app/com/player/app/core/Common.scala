package com.player.app.core

import scala.reflect.runtime.{ universe ⇒ ru }
import scala.reflect.runtime.{universe => ru}

object Common {

  def getTypeTag[ T: ru.TypeTag ]( obj: T ) = ru.typeTag[ T ]

  def readFromFile( fileName: String ) = {
    val file = scala.io.Source.fromFile( fileName )
    val result = file.mkString
    file.close
    result
  }
}