function addNewRole(permissiondata,form, resultDivId, successCallback) {
  updateRoleGeneric(permissiondata,form, resultDivId, "addRole.html", successCallback);
}

function updateExistingRole(permissiondata,form, resultDivId, successCallback) {
  updateRoleGeneric(permissiondata,form, resultDivId, "updateRole.html", successCallback);
}

function updateRoleGeneric(permissiondata,form, resultDivId, url, successCallback) {
  var role = $(form).serialize();
  role += "&permissiondata=" + permissiondata;
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: url,
    data: role,
    success: function(jsonResponse, data, data1, data2) {
    	showMessage(jsonResponse["message"]);
    	
        successCallback();
        if (jsonResponse["message"] === "Role Successfully Added") {
        	refetchContent(jsonResponse['refreshUrl'], $("#"+resultDivId));
        }else{
             $("#" + resultDivId).replaceWith(jsonResponse);
        }
      },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteRole(roleId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteRole.html",
    data : {userId: userId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Role Deleted Successfully!");
      } else {
        showErrorMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
