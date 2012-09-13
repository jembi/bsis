function addNewCollection(form) {
  updateCollectionGeneric(form, "updateCollection.html");
}

function updateExistingCollection(form) {
  updateCollectionGeneric(form, "updateCollection.html");
}

function updateCollectionGeneric(form, url) {
  var collection = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type : "POST",
    url : url,
    data : collection,
    success : function(responseStr) {
      var jsonResponse = jQuery.parseJSON(responseStr);
      console.log(jsonResponse);
      if (jsonResponse["success"] === "true") {
        $.showMessage("Collection Updated Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function decorateEditCollectionDialog() {
  $("#updateCollectionBirthDate").datepicker({
    changeMonth: true,
    changeYear: true,
    minDate: -36500,
    maxDate: 0,
    showOn: "button",
    buttonImage: "images/calendar.gif",
    buttonImageOnly: true,
    yearRange: "c-100:c0"
  });
};
