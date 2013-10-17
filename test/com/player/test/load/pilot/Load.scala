package com.player.test.load.pilot
import play.api.test.Helpers.{running, await}
import play.api.test.TestServer
import play.api.libs.ws.WS
import play.api.libs.json.Json
import scala.concurrent.Future
import org.junit.Test
import play.api.libs.json.Json.toJsFieldJsValueWrapper

class Load {

  lazy val server = TestServer( 3333 )
  
  val requestCount = 1000

  @Test
  def get(): Unit = running( server ) {

    val req = collection.mutable.Set[ Future[ play.api.libs.ws.Response ] ]()
    for ( i ← 1 to requestCount )
      req += WS.url( s"http://localhost:3333/pilot/$i" ).get

    for ( r ← req ) {
      val response = await( r )
      println( response.body )
    }
  }

  @Test
  def post(): Unit = running( server ) {

    val req = collection.mutable.Set[ Future[ play.api.libs.ws.Response ] ]()
    for ( i ← 1 to requestCount )
      req += WS.url( "http://localhost:3333/pilot" ).post( Json.obj( "name" -> i.toString ) )

    for ( r ← req ) {
      val response = await( r )
      println( response.body )
    }
  }
}