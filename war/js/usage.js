function addNewUsage(form, resultDivId, successCallback) {
  updateUsageGeneric(form, resultDivId, "addUsage.html", successCallback);
}

function updateExistingUsage(form, resultDivId, successCallback) {
  updateUsageGeneric(form, resultDivId, "updateUsage.html", successCallback);
}

function updateUsageGeneric(form, resultDivId, url, successCallback) {
  var usage = $(form).serialize();
  $.ajax({
    type: "POST",
    url: url,
    data: usage,
    success: function(jsonResponse, data, data1, data2) {
               showMessage("Usage Updated Successfully!");
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteUsage(productId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteUsage.html",
    data : {productId: productId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Usage Deleted Successfully!");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
