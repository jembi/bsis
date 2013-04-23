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
<c:set var="addUsageForProductFormId">addUsageForm-${unique_page_id}</c:set>
<c:set var="usageDateInputId">usageDateInput-${unique_page_id}</c:set>

<c:set var="addUsageFormProductTypesId">addUsageFormProductTypes-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
					 // let the parent know we are done
				   $("#${tabContentId}").parent().trigger("editUsageSuccess");
    		}

        function notifyParentCancel() {
					 // let the parent know we are done
				   $("#${tabContentId}").parent().trigger("editUsageCancel");
	   		}

        $("#${mainContentId}").find(".addUsageForProductForm")
        											.bind("addUsageForProduct",
	        											function() {
	                							  addNewUsageForProduct($("#${addUsageForProductFormId}")[0], "${tabContentId}", notifyParentSuccess);
	                              });

        $("#${usageDateInputId}").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 1,
          dateFormat : "mm/dd/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        if ("${firstTimeRender}" == "true")
        	$("#${tabContentId}").find('textarea[name="notes"]').html("${usageFields.notes.defaultValue}");

        $("#${addUsageForProductFormId}").find(".productType").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        
      });
</script>

<div id="${tabContentId}">

	<c:if test="${!empty success && !success}">
		<jsp:include page="../common/errorBox.jsp">
			<jsp:param name="errorMessage" value="${errorMessage}" />
		</jsp:include>
	</c:if>


	<div id="${mainContentId}">
		<form:form id="${addUsageForProductFormId}" class="addUsageForProductForm formInTabPane"
			commandName="addUsageForProductForm">
			<form:hidden path="id" />
			<form:hidden path="productId" value="${addUsageForProductForm.product.id}" />
			<form:errors class="formError" path="usage.product" delimiter=", " />
			<c:if test="${usageFields.collectionNumber.hidden != true }">
				<div>
					<label>${usageFields.collectionNumber.displayName}</label>
					<label>${addUsageForProductForm.product.collectionNumber}</label>
				</div>
			</c:if>
			<c:if test="${usageFields.productType.hidden != true }">
				<div>
					<label>${usageFields.productType.displayName}</label>
					<label style="width: auto;">${addUsageForProductForm.product.productType}</label>
				</div>
			</c:if>
			<c:if test="${usageFields.usageDate.hidden != true }">
				<div>
					<form:label path="usageDate">${usageFields.usageDate.displayName}</form:label>
					<form:input path="usageDate" id="${usageDateInputId}" class="usageDate"
											value="${firstTimeRender ? usageFields.usageDate.defaultValue : ''}" />
					<form:errors class="formError" path="usage.usageDate" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${usageFields.hospital.hidden != true }">
				<div>
					<form:label path="hospital">${usageFields.hospital.displayName}</form:label>
					<form:input path="hospital" value="${firstTimeRender ? usageFields.hospital.defaultValue : ''}" />
					<form:errors class="formError" path="usage.hospital"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${usageFields.patientName.hidden != true }">
				<div>
					<form:label path="patientName">${usageFields.patientName.displayName}</form:label>
					<form:input path="patientName" value="${firstTimeRender ? usageFields.patientName.defaultValue : ''}" />
					<form:errors class="formError" path="usage.patientName" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${usageFields.ward.hidden != true }">
				<div>
					<form:label path="ward">${usageFields.ward.displayName}</form:label>
					<form:input path="ward" value="${firstTimeRender ? usageFields.ward.defaultValue : ''}" />
					<form:errors class="formError" path="usage.ward"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${usageFields.lastName.hidden != true }">
				<div>
					<form:label path="useIndication">${usageFields.useIndication.displayName}</form:label>
					<form:input path="useIndication" value="${firstTimeRender ? usageFields.useIndication.defaultValue : ''}" />
					<form:errors class="formError" path="usage.useIndication" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${usageFields.notes.hidden != true }">
				<div>
					<form:label path="notes" class="labelForTextArea">${usageFields.notes.displayName}</form:label>
					<form:textarea path="notes" value="${firstTimeRender ? usageFields.notes.defaultValue : ''}" />
					<form:errors class="formError" path="usage.notes"></form:errors>
				</div>
			</c:if>
		</form:form>

	</div>

</div>
