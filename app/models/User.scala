package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(id: Pk[Long], email: String, firstName: String, lastName: String, password: String)

object User {

  val simple = {
    get[Pk[Long]]("user.id") ~
    get[String]("user.email") ~
    get[String]("user.firstName") ~
    get[String]("user.lastName") ~
    get[String]("user.password") map {
      case id~email~firstName~lastName~password => User(id, email, firstName, lastName, password)
    }
  }

def findById(userId: Long): Option[User] = {
    DB.withConnection { implicit csonnection =>
      SQL("select * from user where id = {userId}").on(
        'id -> userId
      ).as(User.simple.singleOpt)
    }
  }

def findByEmail(userEmail: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where email = {userEmail}").on(
        'email -> userEmail
      ).as(User.simple.singleOpt)
    }
  }

  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(User.simple *)
    }
  }

  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select * from user where 
         email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(User.simple.singleOpt)
    }
  }

  def create(user: User): User = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user values (
          {id},{email}, {firstName}, {lastName}, {password}
          )
        """
      ).on(
        'id -> user.id,
        'email -> user.email,
        'firstName -> user.firstName,
        'lastName -> user.lastName,
        'password -> user.password
      ).executeUpdate()      
      user
    }
  }

}
