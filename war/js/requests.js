function addNewRequest(form) {
  updateRequestGeneric(form, "updateRequest.html");
}

function updateExistingRequest(form) {
  updateRequestGeneric(form, "updateRequest.html");
}

function updateRequestGeneric(form, url) {
  var request = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type : "POST",
    url : url,
    data : request,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Request Updated Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function decorateEditRequestDialog() {
};
