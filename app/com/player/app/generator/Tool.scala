package com.player.app.generator

import scala.reflect.runtime._
import scala.tools.reflect.ToolBox
import com.player.app.Setting

object Tool {

  val cm = universe.runtimeMirror( getClass.getClassLoader )
  val tb = cm.mkToolBox()

  def createClass[ T ]( string: String ): T = {

    val parsed = tb.parse( string )

    val c1 = tb.compile( parsed )

    c1().asInstanceOf[ T ]
  }

  def readSchema( name: String ): String = {
    val source = scala.io.Source.fromFile( Setting.schemaPath + name + ".json" )
    val result = source.mkString
    source.close ()
    result
  }

  def listSchema: Array[ String ] = {
    new java.io.File( Setting.schemaPath ).list()
  }
}