/*function eatThisNow(foodId) {
  jquery.ajax({
    type: "POST",
    url: "/nom/" + {foodId: foodId}
  })

  alert("Food ID is " + foodId);
}*/

$('#etnAddFoodContainer').hide(0);
$('#etnLoadFoodContainer').hide(0);

$('#etnButtonAddFood').click(function(){
  $(this).fadeOut(100);
  $(this).delay(100).hide(0);
  $('#etnAddFoodContainer').delay(200).animate({
    "height": "toggle", "opacity": "toggle"
  }, 500);
});

$(document).ready(function(){
  $('#etnLoadFoodContainer').load('loadFood', function() {
//Delay directives below simulate network traffic
    $('#circleG').delay(2000).hide(0);
    $(this).delay(2000).animate({
      "height": "toggle", "opacity": "toggle"
    }, 500);
  });
});
