package com.player.app.generator

import play.api.libs.json.{ JsObject, Json, JsValue, JsString }
import com.player.app.Setting

object Model {

  private def parse( name: String ) = Json.parse( Tool.readSchema( name ) );

  private def getFieldAndType( js: JsValue ): Seq[ ( String, String ) ] =
    for ( i ← ( js \ "properties" ).asInstanceOf[ JsObject ].fields )
      yield ( i._1,
      ( i._2.asInstanceOf[ JsObject ] \ "type" ).asInstanceOf[ JsString ].value )

  def generate( name: String ) {

    val schema = parse( name )
    val className = ( schema \ "name" ).asInstanceOf[ JsString ].value
    val classStr =
      "import java.util.Date; " +
        "class " +
        //        Setting.modelPackage + "." +
        className +
        "(" +
        ( for ( field ← getFieldAndType( schema ) )
          yield ( s"var ${field._1} : ${field._2}" ) ).mkString( ", " ) +
        "); new " + className + "().getClass()"

    println( classStr )
    Tool.createClass[ Any ]( classStr )

  }

}