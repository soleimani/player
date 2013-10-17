package com.player.app.controller.expose

import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.json._
import com.player.app.Setting

object Dispatcher extends Controller {

  def transformFromJson( input: scala.collection.Map[ String, JsValue ] ): Map[ String, Any ] = {
    if ( !input.isInstanceOf[ scala.collection.immutable.Map[ String, JsValue ] ] )
      throw new Exception( "should be scala.collection.immutable.Map[ String, JsValue ] ]" )
    for ( ( key, value ) ← input.asInstanceOf[ scala.collection.immutable.Map[ String, JsValue ] ] )
      yield key -> ( if ( value.isInstanceOf[ JsString ] ) value.asInstanceOf[ JsString ].toString )
  }

  def transformToJson( input: Map[ String, Any ] ): Array[ ( String, Json.JsValueWrapper ) ] = {
    var a = input.map( ( entry ) ⇒ ( entry._1,
      if ( entry._2.isInstanceOf[ String ] )
        Json.toJsFieldJsValueWrapper( entry._2.asInstanceOf[ String ].drop( 1 ).dropRight( 1 ) )
      else if ( entry._2.isInstanceOf[ Long ] )
        Json.toJsFieldJsValueWrapper( entry._2.asInstanceOf[ Long ] )
      else
        Json.toJsFieldJsValueWrapper( "ERROR" ) ) )
    a.toArray
  }

  def create( entity: String ) = Action( parse.json ) { request ⇒
    val service = Setting.service( entity )
    val body = request.body.asInstanceOf[ JsObject ].value
    val model = service.service.deserialize( transformFromJson( body ) )
    val id = service.service.create( model )
    Async {
      id map { value ⇒
        Ok( "created id : " + value )
      }
    }
  }

  def readAll( entity: String ) = Action {
    Ok( "" ).as( "application/json" )
  }

  def read( entity: String, id: Long ) = Action {
    val service = Setting.service( entity )
    val model = service.service.find( id )
    Async {
      model map { item ⇒
        item.map{
          m ⇒ Ok( Json.obj( transformToJson( service.service.serialize( m ) ): _* ) ).as( "application/json" )
        }.getOrElse {
          NotFound
        }
      }
    }
  }

  def update( entity: String, id: Long ) = Action( parse.json ) { request ⇒
    BadRequest( "Missing parameter [name]" )
  }

  def delete( entity: String, id: Long ) = Action {
    Ok( "deleted " + id )
  }

}