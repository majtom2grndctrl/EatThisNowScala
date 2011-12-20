package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import java.util.{Date}

import anorm._

import views._
import models._

object Foods extends Controller with Secured {

  val eatThisForm = Form(
    of(Food.apply _,Food.unapply _)(
      "foodId" -> of[Pk[Long]],
      "foodName" -> of[String],
      "foodOwner" -> of[String],
      "foodIsEaten" -> of[Boolean],
      "foodExpiry" -> of [Option[Date]]
    )
  )

  def index = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
//      Food.sadacheTest(username)
      Ok(
        html.foods.index(
          Food.findFoodFor(username),
          user,
          eatThisForm
        )
      )
    }.getOrElse(Forbidden)
  }

  def markAsEaten(food: Long) = IsOwnerOf(food) { _ => implicit request =>
  	println("markAsEaten " + food)
    Food.markAsEaten(food, true: Boolean)
    Ok
    Redirect(routes.Foods.index)
  }

}
