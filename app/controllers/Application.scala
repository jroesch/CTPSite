package controllers

import play.api._
import models._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._

object Application extends Controller {
  
  def index = Action { request =>
    val uid = request.session.get("userid").get
    val query = MongoDBObject("_id" -> new ObjectId(uid))
    val user = UserDAO.findOne(query)
    Ok(views.html.index(user.get))
  }
  
  def newPost = Action {
    Ok(views.html.createPost())
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
        val (user, pw) = loginForm.bindFromRequest.get
        Ok(user + pw)
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