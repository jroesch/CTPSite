package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._

object Comments extends Controller {
  
  val postForm = Form(
    tuple("author" -> text, "content" -> text)
  )
  
  /* CRUD for Comments */ 
  def create(postId: String) = TODO
  
  def read(id: String) = TODO
  
  def update(id: String) = TODO
  
  def delete(id: String) = TODO
}