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
  def list(page: String, postNumber: Int = 10) = TODO
  
  /* CRUD Actions for Posts */
  def create = Action { implicit request =>
    val (title, content) = postForm.bindFromRequest.get
    val md = transformer(content)
    val post: Post = Post(title = title, content = md, author = "placeholder")
    val _id = PostDAO.insert(post)
    Redirect("/blog/posts/"+(_id.get.toString))
  }
  
  def read(id: String) = Action { 
    val query = MongoDBObject("_id" -> new ObjectId(id))
    val post = PostDAO.findOne(query)
    post match {
      case Some(Post(i, a, t, c)) => Ok("hello")
      case None => Ok("Not Found")
    }
  }
  
  def update(id: String) = TODO
  
  def delete(id: String) = Action {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    PostDAO.remove(query)
    Ok("Post Removed!")
  }
}