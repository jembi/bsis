function updateTestResult(form, resultDivId, successCallback) {
  var testResult = $(form).serialize();
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: "updateTestResult.html",
    data: testResult,
    success: function(jsonResponse, data, data1, data2) {
               showMessage("Test Result Updated Successfully!");
               successCallback(jsonResponse.refreshUrl);
             },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function addAllTestResults(allTestResultsData, resultDivId, successCallback) {
  showLoadingImage($("#" + resultDivId));
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

function saveTestResultsWithConfirmation(url, bloodTestsData, successCallback, errorCallback) {

  $.ajax({
    url: url,
    data: bloodTestsData,
    type: "POST",
    success: function(response) {
               successCallback(response);
             },
    error: function(response) {
             console.log(response);
             errorCallback(response);
           }
  });

}