function addNewDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "addDonor.html", successCallback);
}

function updateExistingDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "updateDonor.html", successCallback);
}

function updateDonorGeneric(form, resultDivId, url, successCallback) {
  var donor = $(form).serialize();
  showLoadingImage(resultDivId);
  $.ajax({
    type: "POST",
    url: url,
    data: donor,
    success: function(jsonResponse, data, data1, data2) {
                showMessage("Donor Updated Successfully!");
                successCallback();
                $("#" + resultDivId).replaceWith(jsonResponse);
              },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteDonor(donorId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteDonor.html",
    data : {donorId: donorId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Donor Deleted Successfully!");
      } else {
        showErrorMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
