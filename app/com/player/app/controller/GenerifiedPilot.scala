package com.player.app.controller

import play.api.libs.json._

import com.player.app.model.{ Pilot ⇒ PilotModel }
trait GenerifiedPilot extends Generic[ PilotModel ] {
  this: com.player.app.service.api.Component[ PilotModel ] ⇒
  
  def deserialize( i: JsValue ): PilotModel = {
    var name = ""
    ( i \ "name" ).asOpt[ String ].map { n ⇒
      name = n
    }
    PilotModel( anorm.NotAssigned, name )
  }

  def serialize( o: PilotModel ): JsValue = {
    Json.obj( "id" -> o.id.get, "name" -> o.name )
  }
}
