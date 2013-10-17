package com.player.app.repository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.db._
import play.api.Play.current
//import anorm.{ Pk, SQL }
import anorm._

import anorm.SqlParser.{ get }

import com.player.app.model.{ Pilot ⇒ PilotModel }

trait PilotComponent extends com.player.app.repository.api.Component[ PilotModel ] {

  protected[ this ] object PilotRepository extends Repository {
    val schema =
      """
CREATE TABLE pilot (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
)         
"""

    def rowMapper: SqlRow ⇒ PilotModel = { row: SqlRow ⇒
      PilotModel( Id( row[ Long ]( "id" ) ), row[ String ]( "name" ) )
    }

    def createTable: Unit = {
      DB.withConnection { implicit connection ⇒
        SQL( schema ).on().executeUpdate()
      }
    }

    def dropTable: Unit = {
      DB.withConnection { implicit connection ⇒
        SQL( "drop table if exists pilot" ).on().executeUpdate()
      }
    }

    def create( person: PilotModel ): Future[ Long ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( "insert into pilot(name) values ({name})" ).on(
          'name -> person.name ).executeInsert().getOrElse( -1 )
      }
    }

    def find( id: Long ): Future[ Option[ PilotModel ] ] = Future {
      DB.withConnection { implicit connection ⇒
        val result = SQL( "select * from pilot where id = {id}" ).on( 'id -> id )().map( rowMapper )
        if ( result.length == 1 )
          Some( result( 0 ) )
        else
          None
      }
    }

    def findAll(): Future[ Seq[ PilotModel ] ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( "select * from pilot" )().map( rowMapper )
      }
    }

    def update( id: Long, person: PilotModel ): Future[ Unit ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( "update pilot set name = {name} where id = {id}" ).on(
          'id -> id, 'name -> person.name ).executeUpdate()
      }
    }

    def delete( id: Long ): Future[ Unit ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( "delete from pilot where id = {id}" ).on( 'id -> id ).executeUpdate()
      }
    }

    def delete: Future[ Unit ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( "delete from pilot" ).on().executeUpdate()
      }
    }

    def serialize( model: PilotModel ): Map[ String, Any ] = ???

    def deserialize( value: Map[ String, Any ] ): PilotModel = ???
  }
}