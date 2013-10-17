package com.player.app

import scala.concurrent.Future
import scala.language.dynamics
import scala.reflect.runtime.{ universe ⇒ ru }

import com.player.app.core.Common

package model {

  trait Model extends Dynamic {
    val theType = Common.getTypeTag( this ).tpe

    def selectDynamic( name: String ): Any = {

      // TODO: use scala reflection
      for ( field ← getClass.getDeclaredFields() if ( field.getName == name ) ) {
        field.setAccessible( true )
        return field.get( this )
      }
    }
  }

}

import model.Model

package repository.api {

  trait Component[ M <: Model ] {

    protected[ this ] def repository: Repository

    //    protected[ this ] def getGenerfiedType {
    //      import scala.reflect.runtime.{ universe ⇒ ru }
    //      def getTypeTag[ T: ru.TypeTag ]( obj: T ) = ru.typeTag[ T ]
    //      val l = List( 1, 2, 3 )
    //      val theType = getTypeTag( this ).tpe
    //      for ( i ← theType.members )
    //        println ( i )
    //
    //    }

    protected[ this ] trait Repository {

      def createTable: Unit

      def dropTable: Unit

      def create( model: M ): Future[ Long ]

      def find( id: Long ): Future[ Option[ M ] ]

      def findAll(): Future[ Seq[ M ] ]

      def update( id: Long, model: M ): Future[ Unit ]

      def delete( id: Long ): Future[ Unit ]

      def delete: Future[ Unit ]

      def serialize( model: M ): Map[ String, Any ]

      def deserialize( value: Map[ String, Any ] ): M
    }
  }
}

package service.api {

  trait Component[ M <: Model ] {

    def service: Service

    protected[ this ] trait Service {

      def createTable: Unit

      def dropTable: Unit

      def create( model: M ): Future[ Long ]

      def find( id: Long ): Future[ Option[ M ] ]

      def findAll: Future[ Seq[ M ] ]

      def update( id: Long, model: M ): Future[ Unit ]

      def delete( id: Long ): Future[ Unit ]

      def delete: Future[ Unit ]

      def serialize( model: M ): Map[ String, Any ]

      def deserialize( value: Map[ String, Any ] ): M
    }
  }
}

object Api {

}