function addNewTestResult(form) {
  updateTestResultGeneric(form, "updateTestResult.html");
}

function updateExistingTestResult(form) {
  updateTestResultGeneric(form, "updateTestResult.html");
}

function updateTestResultGeneric(form, url) {
  var testResult = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type : "POST",
    url : url,
    data : testResult,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Test Result Updated Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function decorateEditTestResultDialog() {
};
