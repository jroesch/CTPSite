package models

import models._
import com.novus.salat._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._


case class Comment(_id: ObjectId = new ObjectId, post_id: ObjectId, author: String, content: String) 

object CommentDAO extends SalatDAO[Comment, ObjectId](collection=Database("posts"))