function addNewCollection(form, resultDivId, successCallback) {
  updateCollectionGeneric(form, resultDivId, "addCollectedSample.html", successCallback);
}

function updateExistingCollection(form, resultDivId, successCallback) {
  updateCollectionGeneric(form, resultDivId, "updateCollectedSample.html", successCallback);
}

function updateCollectionGeneric(form, resultDivId, url, successCallback) {
  var collectedSample = $(form).serialize();
  $.ajax({
    type: "POST",
    url: url,
    data: collectedSample,
    success: function(jsonResponse, data, data1, data2) {
               showMessage("Collection Updated Successfully!");
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteCollection(collectedSampleId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteCollectedSample.html",
    data : {collectedSampleId: collectedSampleId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Collection Deleted Successfully!");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}

function getLabelForDonor(donor) {
  return donor.firstName + " " + donor.lastName + ": "
         + donor.donorNumber;
}