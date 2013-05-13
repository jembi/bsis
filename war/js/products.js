function addNewProduct(form, resultDivId, successCallback) {
  updateProductGeneric(form, resultDivId, "addProduct.html", successCallback);
}

function addNewProductCombination(form, resultDivId, successCallback) {
  updateProductGeneric(form, resultDivId, "addProductCombination.html", successCallback);
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
               successCallback();
               $("#" + resultDivId).replaceWith(jsonResponse);
             },
    error: function(response) {
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

function discardProduct(form, successCallback) {
  var discardProductFormData = $(form).serialize();
  $.ajax({
    type : "POST",
    url : "discardProduct.html",
    data : discardProductFormData,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Product discarded");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}

function returnProduct(form, successCallback) {
  var returnProductFormData = $(form).serialize();
  $.ajax({
    type : "POST",
    url : "returnProduct.html",
    data : returnProductFormData,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Product returned");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}

function splitProduct(form, successCallback) {
  var splitProductFormData = $(form).serialize();
  $.ajax({
    type : "POST",
    url : "splitProduct.html",
    data : splitProductFormData,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        successCallback();
        showMessage("Product split");
      } else {
        showMessage("Something went wrong." + jsonResponse["errMsg"]);
      }
    }
  });
}
