<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${mainContentId}").find(".findIssuedProductsButton")
                        .button({icons: {primary: 'ui-icon-search'}})
                        .click(findIssuedProductsForRequest);

  function findIssuedProductsForRequest() {
    var requestNumber = $("#${mainContentId}").find('input[name="requestNumber"]').val();
    $.ajax({
      url: "findIssuedProductsForRequest.html",
      type: "GET",
      data: {requestNumber : requestNumber},
      success: function(response) {
                 $("#${mainContentId}").find(".findIssuedProductsResults")
                                        .html(response);
               },
      error: function(response) {
               $("#${mainContentId}").find(".findIssuedProductsResults")
                                      .html(response.responseText);
             }
    });
  }

  $("#${mainContentId}").find(".clearFormButton")
                        .button()
                        .click(refetchForm);

  function refetchForm() {
    $.ajax({
      url: "${refreshUrl}",
      data: {},
      type: "GET",
      success: function (response) {
                  $("#${tabContentId}").replaceWith(response);
               },
      error:   function (response) {
                 showErrorMessage("Something went wrong. Please try again.");
               }
      
    });
  }

});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">

    <div class="formFormatClass">
      <div>
        <label>Request number</label>
        <input name="requestNumber" />
      </div>
      <div>
        <button class="findIssuedProductsButton autoWidthButton">
          Find issued products
        </button>
        <button class="clearFormButton">
          Clear form
        </button> 
      </div>
    </div>


    <div class="findIssuedProductsResults">
    </div>

  </div>

</div>
