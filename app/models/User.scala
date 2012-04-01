package models

import models._
import com.novus.salat._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import java.security.MessageDigest
import scala.math.random

case class User(
         _id: ObjectId = new ObjectId, 
         username: String, 
         password: String,
         admin: Boolean) /* {
  def hashPassword(s: String) = sha(salt(s))
  
  def salt(password: String) = password + randomString(10)
  
  def sha(s: String) = {
    val md = MessageDigest.getInstance("SHA")
    md.update(s)
    md.digest().toString
  }
  
  def randomString(length: Int) = {
    (1 to length).map(x => (scala.math.random * 127).toInt.toChar.toString)
                 .fold("")(_ + _)
  } 
} */

object UserDAO extends SalatDAO[User, ObjectId](collection=Database("users"))
