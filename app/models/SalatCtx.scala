package models

import com.mongodb.casbah.commons.Imports._
import com.novus.salat._
import play.api.Play
import play.api.Play.current
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.MongoURI
import scala.sys._

package object models {
  implicit val ctx = {
    val context = new Context {
      val name = "play-context"
    }

    context.registerClassLoader(Play.classloader)

    context
  }
}
