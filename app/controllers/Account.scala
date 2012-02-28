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
      "email" -> email,
	  "password" -> tuple(
	    "main" -> text(minLength = 6),
	    "confirm" -> text
	  ).verifying(
	    "Passwords don't match", passwords => passwords._1 == passwords._2
	  )
	)
  )

  def manage = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      val existingUser: (String, (String, String)) = (user.email, (user.password, user.password))
      Ok(
        html.account.index(
          accountForm.fill(existingUser),
          user
        )
      )
    }.getOrElse(Forbidden)
  }
/*
  def update = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      Form(
        "email" -> nonEmptyText
      ).bindFromRequest.fold(
        errors => BadRequest,
        Ok(html.account.summary(email, user))
      )
    }
  }
*/
}