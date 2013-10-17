package com.player.app.service

import com.player.app.model.Model

trait GenericComponent[ M <: Model ] extends com.player.app.service.api.Component[ M ] {
  this: com.player.app.repository.api.Component[ M ] ⇒

  trait GenericService extends Service {

    def createTable = repository.createTable

    def dropTable = repository.dropTable

    def create( model: M ) = repository.create( model )

    def find( id: Long ) = repository.find( id )

    def findAll = repository.findAll

    def update( id: Long, model: M ) = repository.update( id, model )

    def delete( id: Long ) = repository.delete( id )

    def delete = repository.delete

    def serialize( model: M ): Map[ String, Any ] = repository.serialize( model )

    def deserialize( value: Map[ String, Any ] ): M = repository.deserialize( value )

  }
}