function addNewCollectedSample(form, resultDivId, successCallback) {
  updateCollectionGeneric(form, resultDivId, "addCollectedSample.html", successCallback);
}

function updateExistingCollection(form, resultDivId) {
  updateCollectionGeneric(form, resultDivId, "updateCollectedSample.html", successCallback);
}

function updateCollectionGeneric(form, resultDivId, url, successCallback) {
  var collectedSample = $(form).serialize();
  $.ajax({
    type: "POST",
    url: url,
    data: collectedSample,
    success: function(jsonResponse, data, data1, data2) {
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(jsonResponse) {
             $("#" + resultDivId).replaceWith(jsonResponse);
           }
  });
}

function deleteCollectedSample(collectedSampleId) {
  $.ajax({
    type : "POST",
    url : "deleteCollectedSample.html",
    data : {collectedSampleId: collectedSampleId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Collection Deleted Successfully!");
        window.history.back();
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function getLabelForDonor(donor) {
  return donor.firstName + " " + donor.lastName + ": "
         + donor.donorNumber;
}