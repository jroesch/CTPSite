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
         salt: String,
         admin: Boolean = false) 
         
object User {
  def hashPassword(pw: String, salt: String) = sha(pw + salt)
  
  def salt = randomString(20)
  
  def sha(s: String) = {
    val md = MessageDigest.getInstance("SHA")
    md.update(s.getBytes)
    val bytes = md.digest()
    val sb = new StringBuilder();
    bytes.foreach { x =>
      sb.append(Integer.toString((x & 0xff) + 0x100, 16).substring(1))
    }
    sb.toString
  }
  
  private def randomString(length: Int) = {
    (1 to length).map(x => (scala.math.random * 127).toInt.toChar.toString)
                 .fold("")(_ + _)
  } 
} 

object UserDAO extends SalatDAO[User, ObjectId](collection=Database("users"))
