function addNewCollectedSample(form, resultDivId, successCallback, successCallbackArguments) {
  updateCollectionGeneric(form, resultDivId, "addCollectedSample.html", successCallback, successCallbackArguments);
}

function updateExistingCollection(form, resultDivId) {
  updateCollectionGeneric(form, resultDivId, "updateCollectedSample.html", successCallback, successCallbackArguments);
}

function updateCollectionGeneric(form, resultDivId, url, successCallback, successCallbackArguments) {
  var collectedSample = $("#" + form.getAttribute("id")).serialize();
  console.log(collectedSample);
  $.ajax({
    type: "POST",
    url: url,
    data: collectedSample,
    success: function(jsonResponse, data, data1, data2) {
               $("#" + resultDivId).replaceWith(jsonResponse);
               successCallback(successCallbackArguments);
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