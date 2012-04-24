package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import java.util.{Date}

import anorm._

import views._
import models._

object Foods extends Controller with Secured {

  val Home = Redirect(routes.Foods.index())

  def foodForm(user: User) = Form(
    mapping(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "name" -> nonEmptyText,
      "status" -> ignored("edible": String),
      "owner" -> ignored(user.id),
      "expiry" -> date("MM/dd/yyyy")
    )(Food.apply)(Food.unapply)
  )

  def index = AuthenticatedUser { user => implicit request =>
    Ok(
      html.foods.index(
        user,
        Food.findEdibleFoodFor(user.id),
        foodForm(user)
      )
    )
  }

  def loadFood = AuthenticatedUser { user => implicit request =>
    Ok(
      html.foods.loadListItems(
        Food.findEdibleFoodFor(user.id)
      )
    )
  }

  def createFood = AuthenticatedUser { user => implicit request =>
    Ok(
      html.foods.create(
        foodForm(user),
        user
      )
    )
  }

  def markAsEaten(foodId: Long) = IsOwnerOf(foodId) { _ => implicit request =>
    Food.updateStatus(foodId, "eaten")
    Ok
    Redirect(routes.Foods.index)
  }

  def markAsEatenAsync(foodId: Long) = IsOwnerOf(foodId) { _ => implicit request =>
    Food.updateStatus(foodId, "eaten")
    Ok
  }

  def saveFood = AuthenticatedUser { user => implicit request =>
    foodForm(user).bindFromRequest.fold(
      errors => BadRequest(html.foods.create(errors, user)),
      food => Food.create(food)
    )
    Redirect(routes.Foods.index)
  }

}
