package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import http._
import sitemap._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index"
    )

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    LiftRules.statelessDispatch.append(heartbeat.Memory)
    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

  }
}
