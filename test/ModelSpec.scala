package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class ModelSpec extends Specification {

  import models._

  // -- Date helpers

  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str

  // --

  "Food model" should {
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val pizza = Food.findById(3)

        pizza.name must equalTo("Pizza")
        pizza.eaten must equalTo(false)
        pizza.owner must equalTo("dhiester@example.com")

      }
    }
  }




}