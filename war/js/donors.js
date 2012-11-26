function addNewDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "addDonor.html", successCallback);
}

function updateExistingDonor(form, resultDivId, successCallback) {
  updateDonorGeneric(form, resultDivId, "updateDonor.html", successCallback);
}

function updateDonorGeneric(form, resultDivId, url, successCallback) {
  var donor = $("#" + form.getAttribute("id")).serialize();
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

function deleteDonor(donorId) {
  $.ajax({
    type : "POST",
    url : "deleteDonor.html",
    data : {donorId: donorId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Donor Deleted Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}
