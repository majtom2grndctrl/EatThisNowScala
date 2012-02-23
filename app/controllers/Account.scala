package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import anorm._

import views._
import models._

object Account extends Controller with Secured {

  val accountForm = Form(
    tuple(
      "password1" -> text,
      "password2" -> text
    )
  )

  def manage = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      Ok(
        html.account.index(
          accountForm,
          user
        )
      )
    }.getOrElse(Forbidden)
  }

}