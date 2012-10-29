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
        $('#' + form.getAttribute("id")).each(function() {
          this.reset();
        });
        window.history.back();
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function deleteRequest(requestNumber) {
  $.ajax({
    type : "POST",
    url : "deleteRequest.html",
    data : {requestNumber: requestNumber},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Request Deleted Successfully!");
        window.history.back();
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
