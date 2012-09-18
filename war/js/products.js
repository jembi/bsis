function addNewProduct(form) {
  updateTestResultGeneric(form, "updateProduct.html");
}

function updateExistingProduct(form) {
  updateTestResultGeneric(form, "updateProduct.html");
}

function updateProductGeneric(form, url) {
  var testResult = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type : "POST",
    url : url,
    data : testResult,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Product Updated Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function decorateEditProductDialog() {
};
