package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import eu.henkelmann.actuarius.ActuariusTransformer
import models._
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

object Posts extends Controller {
  private val guest = User(username = "Guest", password = "", salt = "")

  def editor = TODO
  /* Text Transformer for Markdown Support */
  val transformer = new ActuariusTransformer
  
  /* PostForm */
  val postForm = Form(
    tuple("author" -> text, "title" -> text, "content" -> text)
  )
  
  /* Pagination for Posts */
  def list(page: String, postNumber: Int = 10) = Action { implicit request =>
    val query = MongoDBObject()
    val user = User.getLoggedInUser(session)
    val results = PostDAO.find(query).toList
    val start = page.toInt * postNumber
    val end = start + postNumber
    val posts = results.slice(start, end)
    val numberOfPages = page.toInt / postNumber
    Ok(views.html.postsPage(user, posts, 0 to numberOfPages))
  }
  
  def newest = Action {
    list("0")
  }
  
  /* CRUD Actions for Posts */
  def create = Action { implicit request =>
    val (author, title, content) = postForm.bindFromRequest.get
    val md = transformer(content)
    val post: Post = Post(title = title, content = md, author = author)
    val _id = PostDAO.insert(post)
    Redirect("/posts/"+(_id.get.toString))
  }
  
  def read(id: String) = Action { implicit request =>
    val query = MongoDBObject("_id" -> new ObjectId(id))
    val user = User.getLoggedInUser(session)
    val post = PostDAO.findOne(query)
    post match {
      case Some(p) => Ok(views.html.displayPost(user, p))
      case None    => Ok("Not Found")
    }
  }
  
  def update(id: String) = Action { implicit request =>
    println("updating!")
    val (author, title, content) = postForm.bindFromRequest.get
    val md = transformer(content)
    val updatedPost = Post(title = title, content = md, author = author)
    val oldPost = PostDAO.findOne(MongoDBObject("_id" -> new ObjectId(id))).get
    PostDAO.update(grater[Post].asDBObject(updatedPost), grater[Post].asDBObject(oldPost))
    Redirect("/posts/"+id)
  }
  
  def delete(id: String) = Action {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    PostDAO.remove(query)
    Ok("Post Removed!")
  }
}