function addNewTestResult(form, resultDivId, successCallback) {
  updateTestResultGeneric(form, resultDivId, "addTestResult.html", successCallback);
}

function updateExistingTestResult(form, resultDivId, successCallback) {
  updateTestResultGeneric(form, resultDivId, "updateTestResult.html", successCallback);
}

function updateTestResultGeneric(form, resultDivId, url, successCallback) {
  var testResult = $(form).serialize();
  $.ajax({
    type: "POST",
    url: url,
    data: testResult,
    success: function(jsonResponse, data, data1, data2) {
               showMessage("Test Result Updated Successfully!");
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function addAllTestResults(allTestResultsData, resultDivId, successCallback) {
  $.ajax({
    type: "POST",
    url: "addAllTestResults.html",
    data: allTestResultsData,
    success: function(jsonResponse, data, data1, data2) {
               showMessage("Test Results Updated Successfully!");
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteTestResult(testResultId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteTestResult.html",
    data : {testResultId: testResultId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Test Result Deleted Successfully!");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
