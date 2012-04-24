$(document).ready(function(){

// Hide JS required warnings
$('.etnsJsRequired').hide(0);

// Show the foodForm
$('#etnsAddFoodContainer').hide(0);
$('#etnsLoadFoodContainer').hide(0);

$('#etnsButtonAddFood').click(function(){
  $(this).fadeOut(100);
  $(this).delay(100).hide(0);
  $('#etnsAddFoodContainer').delay(200).animate({
    "height": "toggle", "opacity": "toggle"
  }, 500);
});

// Load food asynchronously after page load
  $("#etnsLoadFoodContainer").load("loadFood", function() {
    $("#facebookG").hide(250);
    $(this).delay(250).animate({
      "height": "toggle", "opacity": "toggle"
    }, 500);

    $(".etnsFoodCommands").css("display", "none");

    // Show food options when clicked
    $(".etnsFoodButtonMain").click(function() {
      var menuStatus;
      if(menuStatus != "open") {
        var menuStatus = "open";
        $(this).next(".etnsFoodCommands").show(250);
      } else if(menuStatus == "open") {
    	var menuStatus = "closed";
        $(this).next(".etnsFoodCommands").hide(250);
      }
    });

    $(".etnsFoodEatenButton").click(function() {
      var foodId = $(this).attr("food-id");
      var listItemId = "#foodItem" + foodId;
      alert(listItemId);

      $.ajax ({
    	type: 'POST',
    	url: '/nom/' + foodId,
    	success: $(listItemId).hide(250)
      });
    });

  });


// foodForm validation
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

// Hide account menu on page load
  $(".etnsHeaderAccountMenuItem").css({"position":"absolute","left":"-10000px"});
  var etnAccountMenuState = "hidden";

// Show or hide account menu on click
  $(".etnsHeaderAccountMenuTitle").click(function() {
	if (etnAccountMenuState == "hidden") {
      $(".etnsHeaderAccountMenuItem").css({"position":"static"});
      etnAccountMenuState = "shown";
	} else if (etnAccountMenuState == "shown") {
      $(".etnsHeaderAccountMenuItem").css({"position":"absolute"});
      etnAccountMenuState = "hidden";
	}
  });

});
