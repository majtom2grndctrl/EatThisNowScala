package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import java.util.{Date}

class ModelSpec extends Specification {

  import models._

  // -- Date helper
  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy/MM/dd").format(date) == str
  
  "Food model" should {
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val pizza = Food.findById(1)

        pizza.name must equalTo("Mashed Potatoes")
        pizza.eaten must equalTo(false)
        pizza.owner must equalTo("test@example.com")
      }
    }
/*
//This might be a weekend programming session kind of thing.
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