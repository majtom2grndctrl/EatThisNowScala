$(document).ready(function(){

// Hide JS required warnings
$('.etnsJsRequired').hide(0);
/*
 * Show the foodForm
 */
$('#etnsAddFoodContainer').hide(0);
$('#etnsLoadFoodContainer').hide(0);

$('#etnsButtonAddFood').click(function(){
  $(this).fadeOut(100);
  $(this).delay(100).hide(0);
  $('#etnsAddFoodContainer').delay(200).animate({
    "height": "toggle", "opacity": "toggle"
  }, 500);
});

/*
 * Load food asynchronously after page load
 */
  $('#etnsLoadFoodContainer').load('loadFood', function() {
    $('#facebookG').hide(250);
    $(this).delay(250).animate({
      "height": "toggle", "opacity": "toggle"
    }, 500);
    /*
     * Show food options
     */
    $(".etnsFoodButtonMain").click(function() {
      alert("The click event has fired.");
      $(this).children(".etnsFoodCommands").show(0);
    });
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
        date: "That isn't a valid date format! Please follow the pattern of mm/dd/yyyy!"
      }
    }
  });

/*
 * Hide account menu
 */
  $(".etnsHeaderAccountMenuItem").css({"position":"absolute","left":"-10000px"});
  var etnAccountMenuState = "hidden";

/*
 * Show account menu
 */
  $(".etnsHeaderAccountMenuTitle").click(function() {
	    alert("The click event has fired.");
	if (etnAccountMenuState == "hidden") {
      $(".etnsHeaderAccountMenuItem").css({"position":"static"});
      etnAccountMenuState = "shown";
	} else if (etnAccountMenuState == "shown") {
      $(".etnsHeaderAccountMenuItem").css({"position":"absolute"});
      etnAccountMenuState = "hidden";
	}
  });

});
