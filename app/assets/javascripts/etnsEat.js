eatThis = new {};

eatThis.now = function(foodId) {
  $.ajax({
    type: "POST",
    url: "/foods/nom",
    data: "foodId=1"
  })
}
