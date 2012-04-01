package models

import com.mongodb.casbah.Imports._
import scala.sys._

private object MongoURIParser {
  def config(uri: String) = parseMongoURI(uri)

  def parseMongoURI(uri: String) = {
    if (uri.startsWith("mongodb://")) {
      val (_, rest) = uri.span(_ != '/')
      val (username, rest2) = rest.drop(2).span(_ != ':')
      val (password, rest3) = rest2.drop(1).span(_ != '@')
      val (hosts, rest4)    = rest3.drop(1)span(_ != ':')
      val (port, rest5)     = rest4.drop(1)span(_ != '/')
      val db                = rest5.drop(1)
      val values = List(username, password, hosts, port, db)
      val keys   = List("username", "password", "hosts", "port", "db")
      keys.zip(values).toMap
    } else {
      throw new Exception("Must begin \"mongodb://\"")
    }
  }
}

object Database {
  val _database = configMongo()

  def apply(collection: String) = _database(collection)

  def configMongo() = {
    val envVar = env.filterKeys(_ == "MONGOHQ_URL").nonEmpty
    var uriString = "mongodb://:@127.0.0.1:27017/test"
    if (envVar) {
      uriString = env("MONGOHQ_URL")
    } 
    val config = MongoURIParser.config(uriString)
    val mongo = MongoConnection(config("hosts"), config("port").toInt)
    val database = mongo(config("db"))
    if (config("username").nonEmpty && config("password").nonEmpty) {
      val auth = database.authenticate(config("username"), config("password"))
      if (!auth) throw new Exception("Could not Authenticate")
    }
    database
  }
}