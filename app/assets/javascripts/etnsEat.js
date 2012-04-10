$(document).ready(function(){

/*
 * Show the foodForm
 */
$('#etnsAddFoodContainer').hide(0);
$('#etnsLoadFoodContainer').hide(0);

$('#etnsButtonAddFood').click(function(){
  $(this).fadeOut(100);
  $(this).delay(100).hide(0);
  $('#etnAddFoodContainer').delay(200).animate({
    "height": "toggle", "opacity": "toggle"
  }, 500);
});

/*
 * Load food asynchronously after page load
 */
  $('#etnsLoadFoodContainer').load('loadFood', function() {
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
  $(".etnsHeaderAccountMenuItem").css({"position":"absolute","left":"-10000px"});
  var etnAccountMenuState = "hidden";
/*
 * Show account menu
 */
  $(".etnsHeaderAccountMenuTitle").click(function() {
	if (etnAccountMenuState == "hidden") {
      $(".etnsHeaderAccountMenuItem").css({"position":"static"});
      etnAccountMenuState = "shown";
	} else if (etnAccountMenuState == "shown") {
      $(".etnsHeaderAccountMenuItem").css({"position":"absolute"});
      etnAccountMenuState = "hidden";
	}
  })

  /*
   * Show food options
   */

  function etnShowFoodOptions(id) {
    
  }
});
