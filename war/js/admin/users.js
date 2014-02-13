function addNewUser(form, resultDivId, successCallback) {
  updateUserGeneric(form, resultDivId, "addUser.html", successCallback);
}

function updateExistingUser(form, resultDivId, successCallback) {
  updateUserGeneric(form, resultDivId, "updateUser.html", successCallback);
}

function updateUserGeneric(form, resultDivId, url, successCallback) {
  var user = $(form).serialize();
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: url,
    data: user,
    success: function(jsonResponse, data, data1, data2) {
                showMessage("User Updated Successfully!");
                successCallback();
                $("#" + resultDivId).replaceWith(jsonResponse);
              },
    error: function(response) {
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteUser(userId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteUser.html",
    data : {userId: userId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("User Deleted Successfully!");
      } else {
        showErrorMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
