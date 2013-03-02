function addNewProduct(form, resultDivId, successCallback) {
  updateProductGeneric(form, resultDivId, "addProduct.html", successCallback);
}

function updateExistingProduct(form, resultDivId, successCallback) {
  updateProductGeneric(form, resultDivId, "updateProduct.html", successCallback);
}

function updateProductGeneric(form, resultDivId, url, successCallback) {
  var product = $(form).serialize();
  showLoadingImage($("#" + resultDivId));
  $.ajax({
    type: "POST",
    url: url,
    data: product,
    success: function(jsonResponse, data, data1, data2) {
               showMessage("Product Updated Successfully!");
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
             showErrorMessage("Something went wrong. Please fix the errors noted.");
             $("#" + resultDivId).replaceWith(response.responseText);
           }
  });
}

function deleteProduct(productId, successCallback) {
  $.ajax({
    type : "POST",
    url : "deleteProduct.html",
    data : {productId: productId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Product Deleted Successfully");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}

function discardProduct(productId, successCallback) {
  $.ajax({
    type : "POST",
    url : "discardProduct.html",
    data : {productId: productId},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Product Discarded");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
