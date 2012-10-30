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

<script>
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
  });
</script>

<div class="editFormDiv issueProductsDiv">
	<button type="button" id="${goBackButtonId}" style="margin-left: 10px">Go
		Back</button>
	<span style="display: block; margin-top: 20px; font-weight: bold;">
	Request Details</span>

	<div>
		<label>${model.requestNoDisplayName}</label> <input
			value="${model.request.requestNumber}" readonly="readonly" />
	</div>
	<div>
		<label>${model.requestDateDisplayName}</label> <input
			value="${model.request.dateRequested}" readonly="readonly" />
		<label>${model.requiredDateDisplayName}</label> <input
			value="${model.request.dateRequired}" readonly="readonly" />
	</div>
	<div>
		<label>${model.quantityDisplayName}</label> <input
			value="${model.request.quantity}" readonly="readonly" />
		<label>${model.siteDisplayName}</label> <input
			value="${model.request.siteName}" readonly="readonly" />
	</div>
	<div>
		<label>Blood Type</label> <input name="bloodType"
			value="${model.request.bloodType}" readonly="readonly" />
		<label>${model.productTypeDisplayName}</label> <input
			value="${model.request.productType}" readonly="readonly" />
	</div>

	<div class="issueProductSelector">
	<span style="display: block; margin-top: 20px; margin-bottom: 20px; font-weight: bold;">
		Showing available products matching this request below. Please select
		all products you want to issue.
		</span>
		<div style="margin-right: 10px;">
			<jsp:include page="productsTable.jsp"></jsp:include>
		</div>
	</div>

		<div
			style="text-align: right">
			<button type="button" id="${issueProductsButtonId}">Issue
				selected</button>
		</div>

</div>