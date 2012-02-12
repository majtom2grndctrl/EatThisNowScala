/*function eatThisNow(foodId) {
  jquery.ajax({
    type: "POST",
    url: "/nom/" + {foodId: foodId}
  })

  alert("Food ID is " + foodId);
}*/

$('#etnAddFoodContainer').hide(0);
$('#circleG').hide(0);

$('#etnButtonAddFood').click(function(){
  $(this).fadeOut(100);
  $('#circleG').delay(100).show(0);
// The delay directives below are meant to simulate network lag, to test the loading animation
  $('#etnAddFoodContainer').delay(2000).load('createAsync', function() {
    $('#circleG').delay(2000).hide(0);
    $(this).animate({
      "height": "toggle", "opacity": "toggle"
    }, 500);
  });
});
