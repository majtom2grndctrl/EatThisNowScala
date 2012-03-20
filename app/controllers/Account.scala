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
	  "firstName" -> text,
	  "lastName" -> text,
	  "password" -> tuple(
	    "password1" -> text(minLength = 6),
	    "password2" -> text
	  ).verifying(
	    "Passwords don't match", passwords => passwords._1 == passwords._2
	  )
	)
  )

  def manage = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      val existingUser: (String, String, String, (String, String)) = (user.email, user.firstName, user.lastName, (user.password, user.password))
      Ok(
        html.account.manage(
          accountForm.fill(existingUser),
          user
        )
      )
    }.getOrElse(Forbidden)
  }

  def update = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      accountForm.bindFromRequest.fold(
        errors => BadRequest(html.account.manage(errors, user)),
        {case(email, firstName, lastName, (password1, password2)) => Ok(html.account.summary(email, firstName, lastName, password1, password2, user))}
      )
    }.getOrElse(Forbidden)
  }



}
