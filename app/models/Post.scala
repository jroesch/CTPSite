package models

import models._
import com.novus.salat._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._


case class Post(_id: ObjectId = new ObjectId, author: String, title: String, content: String) 

object PostDAO extends SalatDAO[Post, ObjectId](collection=Database("posts"))