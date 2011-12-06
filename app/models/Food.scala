package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Food (id: Pk[Long], name: String, eaten: Boolean, owner: String, expiry: Option[Date])

object Food {
  val simple = {
    get[Pk[Long]]("food.id") ~/
    get[String]("food.name") ~/
    get[Boolean]("food.eaten") ~/
    get[String]("food.owner") ~/
    get[Option[Date]]("food.expiry") ^^ {
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
				"""
			).on(
				'email -> user
			).as(Food.simple *)
		}
	}

	def sadacheTest(user: String) = {
		val result = DB.withConnection { implicit connection =>
					SQL(
						"""
						  select * from food
						  where food.eaten = false and food.owner = {email}
						"""
					).on(
						'email -> user
					)()
		}
		println(result.toList)
	}

  	def create(food: Food): Food = {
	  DB.withConnection { implicit connection =>
	    val id: Long = food.id.getOrElse {
	      SQL("select next value for food_seq").as(scalar[Long])
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


}


