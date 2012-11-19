function addNewCollectedSample(form) {
  updateCollectionGeneric(form, "addCollectedSample.html");
}

function updateExistingCollection(form) {
  updateCollectionGeneric(form, "updateCollectedSample.html");
}

function updateCollectionGeneric(form, url) {
  var collectedSample = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type: "POST",
    url: url,
    data: donor,
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
