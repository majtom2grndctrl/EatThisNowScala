package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import anorm._

import java.util.{Date}

class ModelSpec extends Specification {

  import models._

  // -- Date helper
  def date(str: String) = new java.text.SimpleDateFormat("yyyy/MM/dd").parse(str)

  "Food model" should {
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(mashedPotatoes) = Food.findById(1000)

        mashedPotatoes.name must equalTo("Mashed Potatoes")
        mashedPotatoes.eaten must equalTo(false)
        mashedPotatoes.expiry must equalTo(date("2012/05/21"))
        mashedPotatoes.id must equalTo(Id(1000))
        mashedPotatoes.owner must equalTo(Id(1))
      }
    }

/*    "User model" should {
      "be retrieved by id" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

          val Some(testUser) = User.findById(1)

          testUser.firstName must equalTo("Test")
          testUser.lastName must equalTo("User")

        }
      }
    }
*/
/*
    "Add new food to a user's list" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val badResult = controllers.Foods.save(FakeRequest())

        status(badResult) must equalTo(BAD_REQUEST)

        val badDateFormat = controllers.Foods.save(
          FakeRequest().withFormUrlEncodedBody(
            "id" -> "0",
            "name" -> "badname",
            "eaten" -> "wrong",
            "owner" -> "userwithoutdomain",
            "expiry" -> "baddate"
          )
        )

        status(badDateFormat) must equalTo(BAD_REQUEST)

      }
    }
*/

  }




}