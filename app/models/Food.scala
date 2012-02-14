package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Food (id: Pk[Long], name: String, eaten: Boolean, owner: String, expiry: Date)

object Food {
  val simple = {
    get[Pk[Long]]("food.id") ~
    get[String]("food.name") ~
    get[Boolean]("food.eaten") ~
    get[String]("food.owner") ~
    get[Date]("food.expiry") map {
      case id~name~eaten~owner~expiry => Food(
        id, name, eaten, owner, expiry
      )
    }
  }

	def findFoodFor(user: String): Seq[(Food)] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				  select * from food
				  where food.eaten = false and food.owner = {email}
				  order by 5
				"""
			).on(
				'email -> user
			).as(Food.simple *)
		}
	}

  	def create(food: Food): Food = {
	  DB.withConnection { implicit connection =>
	    val id: Long = food.id.getOrElse {
	      SQL("select next value for food_seq").as(scalar[Long].single)
	    }
	  SQL(
	      """
			  insert into food values (
	      {id}, {name}, {eaten}, {owner}, {expiry}
			  )
	      """
	  ).on(
	      'id -> id,
	      'name -> food.name,
	      'eaten -> food.eaten,
	      'owner -> food.owner,
	      'expiry -> food.expiry
	  ).executeUpdate()
	  
	  food.copy(id = Id(id))
	
	  }
	}

  	def markAsEaten(foodId: Long, eaten: Boolean) {
  	  DB.withConnection { implicit connection =>
  	    SQL("update food set eaten = {eaten} where id = {id}").on(
  	      'id -> foodId,
  	      'eaten -> eaten
  	    ).executeUpdate()
  	  }
  	}


  	def isOwner(food: Long, user: String): Boolean = {
  	  DB.withConnection { implicit connection =>
  	    SQL(
  	      """
  	        select count(food.id) = 1 from food
  	        where food.owner = {email} and food.id = {food}
  	      """
  	    ).on(
  	      'food -> food,
  	      'email -> user
  	    ).as(scalar[Boolean].single)
  	  }
  	}



//Used for Specs testing
  	def findById(foodId: Long): Food = {
  	  DB.withConnection { implicit connection =>
  	    SQL("select * from food where food.id = {foodId}").on(
  	      'foodId-> foodId
  	    ).as(Food.simple.single)
  	  }
  	}


}


