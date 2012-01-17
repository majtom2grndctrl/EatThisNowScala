package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.validation.Constraints._

import java.util.{Date}

import anorm._

import views._
import models._

object Foods extends Controller with Secured {

  val Home = Redirect(routes.Foods.index())

  val foodForm = Form(
    of(Food.apply _, Food.unapply _)(
      "id" -> ignored(NotAssigned),
      "name" -> nonEmptyText,
      "eaten" -> boolean,
      "owner" -> nonEmptyText,
      "expiry" -> date("MM-dd-yyyy")
    )
  )

  def index = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
//      Food.sadacheTest(username)
      Ok(
        html.foods.index(
          Food.findFoodFor(username),
          user
        )
      )
    }.getOrElse(Forbidden)
  }

  def create = IsAuthenticated { username => _ =>
  	User.findByEmail(username).map { user =>
  		Ok(
  		    html.foods.create(
  		        foodForm,
  		        user
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

  def save = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
	    foodForm.bindFromRequest.fold(
	      formWithErrors => BadRequest(html.foods.create(formWithErrors, user)),
	      food => {
	        Food.create(food)
	        Home.flashing("success" -> "Food %s has been created".format(food.name))
	      }
	    )

    }.getOrElse(Forbidden)

  }
}
