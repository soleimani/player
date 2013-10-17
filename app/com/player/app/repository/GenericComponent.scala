package com.player.app.repository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser.{ get }

import com.player.app.model.Model

trait GenericComponent[ M <: Model ] extends com.player.app.repository.api.Component[ M ] {

  protected[ this ] trait GenericRepository extends Repository {
    def createScript: String

    def dropScript: String
    def insertScript: String
    def selectScript: String
    def selectAllScript: String
    def updateScript: String
    def deleteScript: String
    def deleteAllScript: String

    def rowMapper: SqlRow ⇒ M

    def getInsertParam( model: M ): Array[ ( Any, anorm.ParameterValue[ _ ] ) ]

    def getUpdateParameter( model: M ): Array[ ( Any, anorm.ParameterValue[ _ ] ) ]

    def createTable: Unit = {
      DB.withConnection { implicit connection ⇒
        SQL( createScript ).on().executeUpdate()
      }
    }

    def dropTable: Unit = {
      DB.withConnection { implicit connection ⇒
        SQL( dropScript ).on().executeUpdate()
      }
    }

    def create( model: M ): Future[ Long ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( insertScript ).on( getInsertParam( model ): _* ).executeInsert().getOrElse( -1 )
      }
    }

    def find( id: Long ): Future[ Option[ M ] ] = Future {
      DB.withConnection { implicit connection ⇒
        val result = SQL( selectScript ).on( 'id -> id )().map( rowMapper )
        if ( result.length == 1 )
          Some( result( 0 ) )
        else
          None
      }
    }

    def findAll(): Future[ Seq[ M ] ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( selectAllScript )().map( rowMapper )
      }
    }

    def update( id: Long, model: M ): Future[ Unit ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( updateScript ).on( getUpdateParameter( model ): _* ).executeUpdate
      }
    }

    def delete( id: Long ): Future[ Unit ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( deleteScript ).on( 'id -> id ).executeUpdate()
      }
    }

    def delete: Future[ Unit ] = Future {
      DB.withConnection { implicit connection ⇒
        SQL( deleteAllScript ).on().executeUpdate()
      }
    }

  }
}