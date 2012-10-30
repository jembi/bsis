<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="issueProductsButtonId">issueProductsButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>
<c:set var="issueProductSelectorId">issueProductSelectorId-${unique_page_id}</c:set>

<script>
  $(document).ready(function() {
    var selected_products = {};
    $("#${goBackButtonId}").button({
      icons : {
        primary : 'ui-icon-circle-arrow-w'
      }
    }).click(function() {
      window.history.back();
      return false;
    });

    $("#${issueProductsButtonId}").button({
      icons : {
        primary : 'ui-icon-transferthick-e-w'
      }
    }).click(function() {
      // issue the products in the selected_products object
      $.ajax({
        url : "issueProductsForRequest.html",
        type : "POST",
        data : {requestNumber: "${model.request.requestNumber}",
          			products: JSON.stringify(selected_products)
          		 },
        success : function(jsonResponse) {
					          if (jsonResponse["success"] === true) {
					            $.showMessage("Products Issued Successfully!");
					            window.history.back();
					          } else {
					            $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
					              backgroundColor : 'red'
					            });
					          }
        					}
      });
    });

    $("#${issueProductSelectorId} table tbody").click(function(event) {
      console.log(event);
      var rowClicked = $(event.target.parentNode);
      console.log(rowClicked.hasClass("DTTT_selected"));
      if (rowClicked.hasClass("DTTT_selected")) {
        var productNumber = rowClicked.children()[0].innerHTML;
        console.log(productNumber);
        selected_products[productNumber] = productNumber;
        console.log(selected_products);
      } else {
        var productNumber = rowClicked.children()[0].innerHTML;
        delete selected_products[productNumber];
        console.log(selected_products);
      }
    });
  });
</script>

<div class="editFormDiv issueProductsDiv">
	<button type="button" id="${goBackButtonId}" style="margin-left: 10px">Go
		Back</button>
	<span style="display: block; margin-top: 20px; font-weight: bold;">
		Request Details</span>

	<div style="margin: 10px 0 10px 0;">
		<label>${model.requestNoDisplayName}</label>
		<span>${model.request.requestNumber}</span>
	</div>
	<div style="margin: 10px 0 10px 0;">
		<label>${model.requestDateDisplayName}</label>
		<span>${model.request.dateRequested}</span>
	</div>
	<div>
		<label>${model.requiredDateDisplayName}</label>
		<span>${model.request.dateRequired}</span>
	</div>
	<div style="margin: 10px 0 10px 0;">
		<label>${model.quantityDisplayName}</label>
		<span>${model.request.quantity}</span>
	</div>
	<div style="margin: 10px 0 10px 0;">
		<label>${model.siteDisplayName}</label>
		<span>${model.request.siteName}</span>
	</div>
	<div style="margin: 10px 0 10px 0;">
		<label>Blood Type</label>
		<span>${model.request.bloodType}</span>
	</div>
	<div style="margin: 10px 0 10px 0;">
		<label>${model.productTypeDisplayName}</label>
		<span>${model.request.productType}</span>
	</div>

	<div id="${issueProductSelectorId}" class="issueProductSelector">
		<span
			style="display: block; margin-top: 20px; margin-bottom: 20px; font-weight: bold;">
			Showing available products matching this request below. Please select
			all products you want to issue. </span>
		<div style="margin-right: 10px;">
			<jsp:include page="productsTable.jsp"></jsp:include>
		</div>
	</div>

	<div style="text-align: right; margin-top: 10px; margin-right: 10px;">
		<button type="button" id="${issueProductsButtonId}">Issue
			selected</button>
	</div>

</div>