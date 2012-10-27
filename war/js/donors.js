function addNewDonor(form) {
  updateDonorGeneric(form, "updateDonor.html");
}

function updateExistingDonor(form) {
  updateDonorGeneric(form, "updateDonor.html");
}

function updateDonorGeneric(form, url) {
  var donor = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type : "POST",
    url : url,
    data : donor,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Donor Updated Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
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