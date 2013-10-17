package com.player.app.controller

import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.mvc.Action.apply

import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._

import com.player.app.model.Model

import anorm.NotAssigned

trait Generic[ M <: Model ] extends Controller {
  this: com.player.app.service.api.Component[ M ] ⇒

  def deserialize( i: JsValue ): M
  def serialize( o: M ): JsValue
  def create = Action( parse.json ) { request ⇒
    var id = service.create( deserialize( request.body ) )
    Async {
      id map { value ⇒
        Ok( "created id : " + value )
      }
    }
  }

  def readAll = Action {
    val p = service.findAll
    Async {
      p map { item ⇒
        Ok( serialize( item( 0 ) ) ).as( "application/json" )
      }
    }
  }

  def read( id: Long ) = Action {
    val p = service.find( id )
    Async {
      p map { item ⇒
        item.map{
          a ⇒ Ok( serialize( a ) ).as( "application/json" )
        }.getOrElse {
          NotFound
        }
      }
    }
  }

  def update( id: Long ) = Action( parse.json ) { request ⇒
    ( request.body \ "name" ).asOpt[ String ].map { p ⇒
      service.update( id, deserialize( request.body ) )
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