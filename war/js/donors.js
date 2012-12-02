function addNewDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "addDonor.html", successCallback);
}

function updateExistingDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "updateDonor.html", successCallback);
}

function updateDonorGeneric(form, resultDivId, url, successCallback) {
  var donor = $("#" + form.getAttribute("id")).serialize();
  showLoadingImage(resultDivId);
  $.ajax({
    type: "POST",
    url: url,
    data: donor,
    success: function(jsonResponse, data, data1, data2) {
                console.log(resultDivId);
                $("#" + resultDivId).replaceWith(jsonResponse);
                console.log(jsonResponse);
                successCallback();
              },
    error: function(jsonResponse) {
             $("#" + resultDivId).replaceWith(jsonResponse);
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
        $.showMessage("Donor Deleted Successfully!");
        successCallback();
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}
