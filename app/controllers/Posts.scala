package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import eu.henkelmann.actuarius.ActuariusTransformer
import models._
import com.mongodb.casbah.Imports._

object Posts extends Controller {
  
  def editor = TODO
  /* Text Transformer for Markdown Support */
  val transformer = new ActuariusTransformer
  
  /* PostForm */
  val postForm = Form(
    tuple("title" -> text, "content" -> text)
  )
  
  /* Pagination for Posts */
  def list(page: String, postNumber: Int = 10) = Action {
    val query = MongoDBObject()
    val results = PostDAO.find(query).toList
    val start = page.toInt * postNumber
    val end = start + postNumber
    val posts = results.slice(start, end)
    val numberOfPages = page.toInt / postNumber
    Ok(views.html.postsPage(posts, 0 to numberOfPages))
  }
  
  def newest = Action {
    list("0")
  }
  
  /* CRUD Actions for Posts */
  def create = Action { implicit request =>
    val (title, content) = postForm.bindFromRequest.get
    val md = transformer(content)
    val post: Post = Post(title = title, content = md, author = "placeholder")
    val _id = PostDAO.insert(post)
    Redirect("/posts/"+(_id.get.toString))
  }
  
  def read(id: String) = Action { 
    val query = MongoDBObject("_id" -> new ObjectId(id))
    val post = PostDAO.findOne(query)
    post match {
      case Some(p) => Ok(views.html.displayPost(p))
      case None    => Ok("Not Found")
    }
  }
  
  def update(id: String) = Action { implicit request =>
    val (title, content) = postForm.bindFromRequest.get
    val md = transformer(content)
    val post: Post = Post(title = title, content = md, author = "placeholder")
    val _id = PostDAO.insert(post)
    Redirect("/posts/"+(_id.get.toString))
  }
  
  def delete(id: String) = Action {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    PostDAO.remove(query)
    Ok("Post Removed!")
  }
}