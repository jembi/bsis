function addNewCollectedSample(form, resultDivId) {
  updateCollectionGeneric(form, resultDivId, "addCollectedSample.html");
}

function updateExistingCollection(form, resultDivId) {
  updateCollectionGeneric(form, resultDivId, "updateCollectedSample.html");
}

function updateCollectionGeneric(form, resultDivId, url) {
  var collectedSample = $("#" + form.getAttribute("id")).serialize();
  console.log(collectedSample);
  $.ajax({
    type: "POST",
    url: url,
    data: collectedSample,
    success: function(jsonResponse, data, data1, data2) {
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
