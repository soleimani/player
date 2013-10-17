package com.player.app.repository

import play.api.db._
import play.api.Play.current
import anorm._

import com.player.app.model.{ Pilot ⇒ PilotModel }

trait GenerifiedPilotComponent extends com.player.app.repository.GenericComponent[ PilotModel ] {

  protected[ this ] object GenerifiedPilotRepository extends GenericRepository {
    val createScript =
      """
CREATE TABLE pilot (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
)         
"""
    val dropScript = "drop table if exists pilot"
    val insertScript = "insert into pilot(name) values ({name})"
    val selectScript = "select * from pilot where id = {id}"
    val selectAllScript = "select * from pilot"
    val updateScript = "update pilot set name = {name} where id = {id}"
    val deleteScript = "delete from pilot where id = {id}"
    val deleteAllScript = "delete from pilot"

    override def getInsertParam( model: PilotModel ) = Array( 'name -> model.name )

    override def getUpdateParameter( model: PilotModel ) = Array( 'id -> model.id, 'name -> model.name )

    override def rowMapper: SqlRow ⇒ PilotModel = { row: SqlRow ⇒
      PilotModel( Id( row[ Long ]( "id" ) ), row[ String ]( "name" ) )
    }

    override def serialize( model: PilotModel ): Map[ String, Any ] = {
      Map( "id" -> model.id.get, "name" -> model.name )
    }

    override def deserialize( value: Map[ String, Any ] ): PilotModel =
      PilotModel( Id( value.getOrElse( "id", 0L ).asInstanceOf[ Long ] ),
        value.getOrElse( "name", "" ).asInstanceOf[ String ] )
  }
}