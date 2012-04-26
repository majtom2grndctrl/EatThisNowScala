package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Food (id: Pk[Long], name: String, status: String, owner: Pk[Long], expiry: Date)

object Food {
  val simple = {
    get[Pk[Long]]("food.id") ~
    get[String]("food.name") ~
    get[String]("food.status") ~
    get[Pk[Long]]("food.owner") ~
    get[Date]("food.expiry") map {
      case id~name~eaten~owner~expiry => Food(
        id, name, eaten, owner, expiry
      )
    }
  }

	def findEdibleFoodFor(id: Pk[Long]): Seq[Food] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				  select * from food
				  where food.status = 'edible' and food.owner = {id}
				  order by 5
				"""
			).on(
				'id -> id
			).as(Food.simple *)
		}
	}

  	def create(food: Food) = {
      DB.withConnection { implicit connection =>
        SQL(
          """
            insert into food values (
              (select next value for food_seq),
              {name}, {status}, {owner}, {expiry}
            )
          """
        ).on(
          'name -> food.name,
          'status -> food.status,
          'owner -> food.owner,
          'expiry -> food.expiry
        ).executeUpdate()
	  }
	}

  	def updateStatus(foodId: Long, newStatus: String) {
  	  DB.withConnection { implicit connection =>
  	    SQL("update food set status = {status} where id = {id}").on(
  	      'id -> foodId,
          'status -> newStatus
  	    ).executeUpdate()
  	  }
  	}


  	def isOwner(foodId: Long, userId: Pk[Long]): Boolean = {
  	  DB.withConnection { implicit connection =>
  	    SQL(
  	      """
  	        select count(food.id) = 1 from food
  	        where food.owner = {userId} and food.id = {food}
  	      """
  	    ).on(
  	      'food -> foodId,
  	      'userId -> userId
  	    ).as(scalar[Boolean].single)
  	  }
  	}



//Used for Specs testing
  	def findById(foodId: Long): Option[Food] = {
  	  DB.withConnection { implicit connection =>
  	    SQL("select * from food where food.id = {foodId}").on(
  	      'foodId-> foodId
  	    ).as(Food.simple.singleOpt)
  	  }
  	}


}


