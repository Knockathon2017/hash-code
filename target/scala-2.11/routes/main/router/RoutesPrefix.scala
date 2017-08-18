
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/amans/fipple/conf/routes
// @DATE:Sat Aug 19 01:01:51 IST 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
