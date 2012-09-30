function addNewUsage(form) {
  updateUsageGeneric(form, "updateUsage.html");
}

function updateExistingUsage(form) {
  updateUsageGeneric(form, "updateUsage.html");
}

function updateUsageGeneric(form, url) {
  var usage = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type : "POST",
    url : url,
    data : usage,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Usage Updated Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function deleteUsage(productNumber) {
  $.ajax({
    type : "POST",
    url : "deleteUsage.html",
    data : {productNumber: productNumber},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Usage Deleted Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function decorateEditUsageDialog() {
};
