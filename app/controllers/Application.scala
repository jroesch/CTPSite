package controllers

import play.api._
import models._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._

object Application extends Controller {
  private val guest = User(username = "Guest", password = "", salt = "")
  
  def index = Action { implicit request =>
    val user = User.getLoggedInUser(session)
    val lastPost = Post.getLatestPost
    Ok(views.html.index(user))
  }
  
  def editPost(id: String) = Action { request =>
    val user = User.getLoggedInUser(request.session)
    PostDAO.findOne(MongoDBObject("_id" -> new ObjectId(id))) match {
      case Some(post) => {
        if (user.admin)
          Ok(views.html.editPost(user, post))
        else 
          Redirect("/login") //redirect here
      }
      case None => {
        if (user.admin)
          Ok("Post not found.")
        else 
          Redirect("/login") //redirect here
      }
    }
  }
  
  def newPost = Action { request =>
    val user = User.getLoggedInUser(request.session)
    if (user.admin)
      Ok(views.html.createPost(user))
    else 
      Redirect("/login") //redirect here
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
        if (user.password == User.hashPassword(pw, user.salt)) 
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