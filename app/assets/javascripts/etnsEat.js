function eatThisNow(foodId) {
  $.ajax({
    type: "POST",
    url: "/nom",
    data: "foodId=1"
  }).done(alert("Save is done"));

  alert("Food ID is " + foodId);
}
