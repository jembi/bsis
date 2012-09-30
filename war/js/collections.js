function addNewCollection(form) {
  updateCollectionGeneric(form, "updateCollection.html");
}

function updateExistingCollection(form) {
  updateCollectionGeneric(form, "updateCollection.html");
}

function updateCollectionGeneric(form, url) {
  var collection = $("#" + form.getAttribute("id")).serialize();
  $.ajax({
    type : "POST",
    url : url,
    data : collection,
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Collection Updated Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function deleteCollection(collectionNumber) {
  $.ajax({
    type : "POST",
    url : "deleteCollection.html",
    data : {collectionNumber: collectionNumber},
    success : function(jsonResponse) {
      if (jsonResponse["success"] === true) {
        $.showMessage("Collection Deleted Successfully!");
      } else {
        $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
          backgroundColor : 'red'
        });
      }
    }
  });
}

function decorateEditCollectionDialog() {
};
