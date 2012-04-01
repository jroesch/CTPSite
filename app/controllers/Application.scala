package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
    //Posts.list("0", 10)
  }
  
  def newPost = Action {
    Ok(views.html.createPost())
  }
}