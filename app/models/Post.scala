package models

import models._
import com.novus.salat._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._


case class Post(_id: ObjectId = new ObjectId, author: String, title: String, content: String) 

object Post {
  def getLatestPost: Post = {
    val all = PostDAO.find(MongoDBObject()).toList
    if (all.isEmpty) return Post(author="", title="No Posts To Show", content="")
    val postsAndTimes = all.zip(all.map(_._id.getTime))
    val max = postsAndTimes.tail.fold(postsAndTimes.head)(maxTuple(_,_))
    max._1
  }
  
  def maxTuple(tupleOne: (Post, Long), tupleTwo: (Post, Long)) = {
    if (tupleOne._2 > tupleTwo._2)
      tupleOne
    else
      tupleTwo
  }
}

object PostDAO extends SalatDAO[Post, ObjectId](collection=Database("posts")) 