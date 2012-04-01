package controllers

import play.api._
import models._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._

object Application extends Controller {
  private val guest = User(username = "Guest", password = "", salt = "")
  
  def index = Action { request =>
    val uid = request.session.get("userid")
    uid match {
      case None => Ok(views.html.index(guest))
      case Some(id) => {
        val query = MongoDBObject("_id" -> new ObjectId(id))
        val user = UserDAO.findOne(query)
        Ok(views.html.index(user.get))
      }
    }
  }
  
  def newPost = Action { request =>
    val uid = request.session.get("userid").get
    val query = MongoDBObject("_id" -> new ObjectId(uid))
    val user = UserDAO.findOne(query)
    if (user.get.admin)
      Ok(views.html.createPost())
    else 
      Redirect("/") //redirect here
  }
  
  def login = Action { implicit request =>
    val loginForm = Form(
      tuple("username" -> text, "password" -> text)
    )
    
    request.method match {
      case "GET" => {
        Ok(views.html.login())
      }
  
      case "POST" => {
        val (username, pw) = loginForm.bindFromRequest.get
        val query = MongoDBObject("username" -> username)
        val user = UserDAO.findOne(query).get
        if (user.password == pw) 
          Redirect("/").withSession {
            "userid" -> user._id.toString
          }
        else 
          Redirect("/login")
      }
    }
  }
  
  def register = Action { implicit request =>
    val loginForm = Form(
      tuple("username" -> text, "password" -> text, "confirm-password" -> text)
    )
    
    request.method match {
      case "GET" => {
        Ok(views.html.register())
      }
  
      case "POST" => {
        val (un, pw, pwConf) = loginForm.bindFromRequest.get
        if (pw != pwConf) {
          Ok("Passwords don't match.")
        } else {
          val salt = User.salt
          val hashpw = User.hashPassword(pw, salt)
          val user = User(username = un, password = hashpw, salt = salt)
          UserDAO.insert(user)
          Redirect("/").withSession {
            "userid" -> user._id.toString
          }
        }
      }
    }
  }
  
  def logout = Action {
    Redirect("/").withNewSession
  }
}