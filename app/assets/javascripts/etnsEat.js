$(document).ready(function(){

/*
 * Show the foodForm
 */
$('#etnAddFoodContainer').hide(0);
$('#etnLoadFoodContainer').hide(0);

$('#etnButtonAddFood').click(function(){
  $(this).fadeOut(100);
  $(this).delay(100).hide(0);
  $('#etnAddFoodContainer').delay(200).animate({
    "height": "toggle", "opacity": "toggle"
  }, 500);
});

/*
 * Load food asynchronously after page load
 */
  $('#etnLoadFoodContainer').load('loadFood', function() {
//Delay directives below simulate network traffic
    $('#facebookG').hide(250);
    $(this).delay(250).animate({
      "height": "toggle", "opacity": "toggle"
    }, 500);
  });


/*
 * foodForm validation
 */
  $("#foodForm").validate({
    rules: {
      name: "required",
      expiry: {
        required: true,
        date: true
      }
    },
    messages: {
      name: "Hey! What do you call this food?",
      date: {
        required: "When do you think this food will go bad?",
        date: "That isn't a valid date format! Please use something like 02/02/2012!"
      }
    }
  })

/*
 * Hide account menu
 */
  $(".etnHeaderAccountMenuItem").css({"position":"absolute","left":"-10000px"});
  var etnAccountMenuState = "hidden";
/*
 * Show account menu
 */
  $(".etnHeaderAccountMenuTitle").click(function() {
	if (etnAccountMenuState == "hidden") {
      $(".etnHeaderAccountMenuItem").css({"position":"static"});
      etnAccountMenuState = "shown";
	} else if (etnAccountMenuState == "shown") {
      $(".etnHeaderAccountMenuItem").css({"position":"absolute"});
      etnAccountMenuState = "hidden";
	}
  })
});