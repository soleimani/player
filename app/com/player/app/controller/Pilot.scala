package com.player.app.controller

import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.mvc.Action.apply

import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._

import com.player.app.model.{ Pilot ⇒ PilotModel }
import anorm.NotAssigned

trait Pilot extends Controller { this: com.player.app.service.api.Component[ PilotModel ] ⇒

  def create = Action( parse.json ) { request ⇒
    ( request.body \ "name" ).asOpt[ String ].map { name ⇒
      var id = service.create( PilotModel( NotAssigned, name ) )
      Async {
        id map { value ⇒
          Ok( "created id : " + value )
        }
      }
    }.getOrElse {
      BadRequest( "Missing parameter [name]" )
    }
  }

  def readAll = Action {
    val p = service.findAll
    Async {
      p map { item ⇒
        Ok( Json.obj( "id" -> item( 0 ).id.get, "name" -> item( 0 ).name ) ).as( "application/json" )
      }
    }
  }

  def read( id: Long ) = Action {
    val p = service.find( id )
    Async {
      p map { item ⇒
        item.map {
          a ⇒ Ok( Json.obj( "id" -> a.id.get, "name" -> a.name ) ).as( "application/json" )
        }.getOrElse{
          NotFound
        }
      }
    }
  }

  def update( id: Long ) = Action( parse.json ) { request ⇒
    ( request.body \ "name" ).asOpt[ String ].map { p ⇒
      service.update( id, PilotModel( NotAssigned, p ) )
      Ok( "updated" )
    }.getOrElse {
      BadRequest( "Missing parameter [name]" )
    }
  }

  def delete( id: Long ) = Action {
    service.delete( id )
    Ok( "deleted " + id )
  }
}