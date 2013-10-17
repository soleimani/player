package com.player.app.service

import com.player.app.model.{ Pilot ⇒ PilotModel }

trait PilotComponent extends com.player.app.service.api.Component[ PilotModel ] {
  this: com.player.app.repository.api.Component[ PilotModel ] ⇒

  object PilotService extends Service {

    def createTable = repository.createTable

    def dropTable = repository.dropTable

    def create( model: PilotModel ) = repository.create( model )

    def find( id: Long ) = repository.find( id )

    def findAll = repository.findAll

    def update( id: Long, model: PilotModel ) = repository.update( id, model )

    def delete( id: Long ) = repository.delete( id )

    def delete = repository.delete

    def deserialize( value: Map[ String, Any ] ): PilotModel = repository.deserialize( value )
   
    def serialize( model: PilotModel ): Map[ String, Any ] = repository.serialize( model )
    
  }
}