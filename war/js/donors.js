function addNewDonor(form, resultDivId) {
  updateDonorGeneric(form, resultDivId, "addDonor.html");
}

function updateExistingDonor(form, resultDivId) {
  updateDonorGeneric(form, resultDivId, "updateDonor.html");
}

function updateDonorGeneric(form, resultDivId, url) {
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
        window.history.back();
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}
