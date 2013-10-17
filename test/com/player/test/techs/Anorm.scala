package com.player.test.techs
import play.api.test.Helpers.{running, inMemoryDatabase}
import play.api.test.{ FakeApplication }
import anorm.SqlParser.{ get }
import anorm._
import org.junit.Test

class Anorm {
  val fakeApp = FakeApplication( additionalConfiguration = inMemoryDatabase() )

  @Test
  def mappingMethods: Unit = running( fakeApp ) {
    val createScript =
      """
CREATE TABLE pilot (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(20) DEFAULT NULL,
  code varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
)         
"""
    case class Pilot( id: Pk[ Long ], name: String, code: String )
    play.api.db.DB.withConnection { implicit connection ⇒
      SQL( createScript ).execute
      SQL( "insert into pilot(name, code) values('n', 'c')" ).executeInsert()

      val result1 = SQL( "select * from pilot" ).as(
        get[ Pk[ Long ] ]( "id" ) ~ get[ String ]( "name" ) ~ get[ String ]( "code" )
          map {
            case id ~ name ~ code ⇒ Pilot( id, name, code )
          } * )
      println( result1 )

      val result2 = SQL( "select * from pilot" )().map{ row ⇒
        ( row[ Long ]( "id" ), row[ String ]( "name" ), row[ String ]( "code" ) )
      }
      for ( i ← result2 )
        println( i )
      println( result2 )

      val result3 = SQL( "select name from pilot" )().collect{
        case Row( name: String ) ⇒ Pilot( Id( 1 ), name, "" )
      }
      println( result3 )

    } ( fakeApp )
  }

}