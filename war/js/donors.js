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
                console.log(jsonResponse);
                $("#" + resultDivId).replaceWith(jsonResponse);
              },
    error: function(jsonResponse) {
             console.log("here");
             console.log(jsonResponse);
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

function decorateEditDonorDialog() {
  $("#updateDonorBirthDate").datepicker({
    changeMonth: true,
    changeYear: true,
    minDate: -36500,
    maxDate: 0,
    showOn: "button",
    buttonImage: "images/calendar.gif",
    buttonImageOnly: true,
    yearRange: "c-100:c0"
  });
};

function decorateEditDonorForm() {
  $("#updateDonorBirthDate").datepicker({
    changeMonth: true,
    changeYear: true,
    minDate: -36500,
    maxDate: 0,
    showOn: "button",
    buttonImage: "images/calendar.gif",
    buttonImageOnly: true,
    yearRange: "c-100:c0"
  });
};
