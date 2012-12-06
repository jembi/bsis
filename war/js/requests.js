function addNewRequest(form, resultDivId, successCallback) {
  updateRequestGeneric(form, resultDivId, "addRequest.html", successCallback);
}

function updateExistingProduct(form, resultDivId, successCallback) {
  updateRequestGeneric(form, resultDivId, "updateRequest.html", successCallback);
}

function updateRequestGeneric(form, resultDivId, url, successCallback) {
  var request = $(form).serialize();
  $.ajax({
    type: "POST",
    url: url,
    data: request,
    success: function(jsonResponse, data, data1, data2) {
               showMessage("Request Updated Successfully!");
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteRequest(productId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteRequest.html",
    data : {requestId: requestId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Request Deleted Successfully!");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
