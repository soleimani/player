import play.api.{ Application, GlobalSettings }
import com.player.app.Setting
import org.fusesource.scalate._
import com.player.app.generator.Tool
import com.player.app.service.GenericComponent
import com.player.app.model.Model

object Global extends GlobalSettings {

  override def onStart( app: Application ): Unit = {
    if ( app.configuration.getBoolean( "player.generate" ).getOrElse( false ) ) {
      val engine = new TemplateEngine
      val f = new java.io.File( Setting.schemaPath )
      for ( model <- f.list() ) {
        val template = engine.layout( Setting.applicationPath + "app/templates/Service.scala.mustache",
          Map( "modelName" -> "Pilot", "tableName" -> "pilot",
            "genericPackageName" -> "com.player.app.service.GenericComponent" ) )
        Setting.addService( "pilot", Tool.createClass[ GenericComponent[ Model ] ]( template ) )
      }
    }
  }
}