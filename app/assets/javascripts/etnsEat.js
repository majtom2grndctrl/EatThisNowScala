function eatThisNow(foodId) {
  jquery.ajax({
    type: "POST",
    url: "/nom/" + {foodId: foodId}
  })

  alert("Food ID is " + foodId);
}
