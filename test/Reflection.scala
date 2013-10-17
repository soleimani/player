object Reflection {

  def main( args: Array[ String ] ): Unit = {
    
        import scala.reflect.runtime._
    import scala.tools.reflect.ToolBox
    val cm = universe.runtimeMirror( getClass.getClassLoader )
    val tb = cm.mkToolBox()

    
    val str1 = """
case class MyClass(var name : String);

trait RepositoryComponent {
  object Repository {
    def instance = MyClass("c")
  }
}

object RepositoryRegister extends RepositoryComponent {
  val repository = Repository
}
RepositoryRegister
"""
      
    val str2 = """
case class MyClass(var name : String);

object RepositoryComponent {
  object Repository {
    def instance = MyClass("c")
  }
}

RepositoryComponent
"""
      
    val str3 = """
case class MyClass(var name : String);

trait RepositoryComponent {
  object Repository {
    def instance = new MyClass("c")
  }
}

object RepositoryRegister extends RepositoryComponent {
  val repository = Repository
}
RepositoryRegister
"""      
    val parsed = tb.parse( str3 )

    val c1 = tb.compile( parsed )

    c1()
  }

}