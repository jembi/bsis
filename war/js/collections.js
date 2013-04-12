function addNewCollection(form, resultDivId, successCallback) {
  updateCollectionGeneric(form, resultDivId, "addCollection.html", successCallback);
}

function updateExistingCollection(form, resultDivId, successCallback) {
  updateCollectionGeneric(form, resultDivId, "updateCollection.html", successCallback);
}

function updateCollectionGeneric(form, resultDivId, url, successCallback) {
  var collectedSample = $(form).serialize();
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: url,
    data: collectedSample,
    success: function(jsonResponse, data, data1, data2) {
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
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

function addNewCollectionBatch(form, resultDivId, successCallback) {
  updateCollectionBatchGeneric(form, resultDivId, "addCollectionBatch.html", successCallback);  
}

function updateCollectionBatchGeneric(form, resultDivId, url, successCallback) {
  var collectionBatch = $(form).serialize();
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: url,
    data: collectionBatch,
    success: function(jsonResponse, data, data1, data2) {
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function addNewWorksheet(form, resultDivId, successCallback) {
  updateWorksheetGeneric(form, resultDivId, "addWorksheet.html", successCallback);
}

function updateExistingWorksheet(form, resultDivId, successCallback) {
  updateWorksheetGeneric(form, resultDivId, "updateWorksheet.html", successCallback);
}

function updateWorksheetGeneric(form, resultDivId, url, successCallback) {
  var worksheet = $(form).serialize();
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: url,
    data: worksheet,
    success: function(jsonResponse, data, data1, data2) {
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteWorksheet(worksheetId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteWorksheet.html",
    data : {worksheetId: worksheetId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Worksheet Deleted Successfully!");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
