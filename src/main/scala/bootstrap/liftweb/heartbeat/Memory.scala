package bootstrap.liftweb.heartbeat

import net.liftweb.http.rest.RestHelper
import net.liftweb.http._

import net.liftweb.json._
import net.liftweb.json.JsonDSL._

import net.liftweb.common._

import scala.collection.JavaConverters._
import sun.management.ManagementFactory
import java.lang.management._

object Memory extends RestHelper with Loggable {

  protected implicit def memoryUsage2Json(m: MemoryUsage): JObject = (
    ("commited" -> m.getCommitted) ~
    ("max" -> m.getMax) ~
    ("used" -> m.getUsed)
  )

  serve {
    case Req("memory" :: Nil, _, _) => {
      val m = ManagementFactory.getMemoryMXBean
      JsonResponse(
        ("heap" -> m.getHeapMemoryUsage) ~
        ("nonheap" -> m.getNonHeapMemoryUsage)
      )
    }
    case Req("memory" :: "pool" :: Nil, _, _) => {
      val pools = for {
        p <- ManagementFactory.getMemoryPoolMXBeans.asScala.toList
        usage <- Option(p.getCollectionUsage)
        pName = p.getName.stripPrefix("PS ").toLowerCase.replaceAll(" ", "_")
      } yield JField(pName, usage)

      JsonResponse(pools)
    }

  }

}



