package com.player.app

import play.api.Play.current
import com.player.app.service.GenericComponent
import com.player.app.model.Model

object Setting {

  val modelPackage = "com.player.app.model"

  private val setting_schema_path = "player.setting.schema.path"

  lazy val applicationPath = current.path.getCanonicalPath() + "/"

  lazy val schemaPath = current.path.getCanonicalPath() + "/" +
    current.configuration.getString( setting_schema_path ).get + "/"

  private[ this ] var services = new scala.collection.mutable.HashMap[ String, GenericComponent[ Model ] ]()

  def service( name: String ) = services( name )
  def addService( model: String, service: GenericComponent[ Model ] ) {
    services += model -> service

  }
}