function addNewDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "addDonor.html", successCallback);
}

function findAndAddNewDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "findDonor.html", successCallback);
}

function updateExistingDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "updateDonor.html", successCallback);
}

function updateDonorGeneric(form, resultDivId, url, successCallback) {
  var donor = $(form).serialize();
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: url,
    data: donor,
    success: function(jsonResponse) {
                successCallback();
                $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
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

function deferDonor(form, successCallback) {
  var donorDeferralFormData = $(form).serialize();
  $.ajax({
    type: "POST",
    url:  "deferDonor.html",
    data: donorDeferralFormData,
    success: function(jsonResponse) {
               successCallback();
               showMessage("Donor deferred");
             },
    error:   function(jsonResponse) {
               showErrorMessage("Something went wrong when trying to defer donor.");
             }
  });
}