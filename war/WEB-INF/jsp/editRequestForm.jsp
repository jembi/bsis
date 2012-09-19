<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>
<c:set var="formId"><%=getCurrentTime()%></c:set>


<script>
  $(".addRequestButton").button();
  function updateRequest() {
    addNewRequest($("#editRequestForm-" + '<c:out value="${formId}"/>')[0]);
    $("#editRequestForm-" + '<c:out value="${formId}"/>')[0].reset();
  }

  $("#dateRequested-" + '<c:out value="${formId}"/>').datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
  });

  $("#dateRequired-" + '<c:out value="${formId}"/>').datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
  });

  $("#editRequestFormSites-" + '<c:out value="${formId}"/>').multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#editRequestFormTypes-" + '<c:out value="${formId}"/>').multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  function updateRequest() {
    addNewRequest($("#editRequestForm-" + '<c:out value="${formId}"/>')[0]);
    $("#editRequestForm-" + '<c:out value="${formId}"/>')[0].reset();
  }
</script>


<form:form method="POST" commandName="editRequestForm"
	id="editRequestForm-${formId}">
	<table>
		<thead>
			<tr>
				<td><b>Add a New Request</b></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="requestNumber">${model.requestNoDisplayName}</form:label></td>
				<td><form:input path="requestNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="dateRequested">${model.requestDateDisplayName}</form:label></td>
				<td><form:input path="dateRequested"
						id="dateRequested-${formId}" /></td>
			</tr>
			<tr>
				<td><form:label path="dateRequired">${model.requiredDateDisplayName}</form:label></td>
				<td><form:input path="dateRequired" id="dateRequired-${formId}" /></td>
			</tr>
			<tr>
				<td><form:label path="quantity">${model.quantityDisplayName}</form:label></td>
				<td><form:input path="quantity"></form:input></td>
			</tr>
			<tr>
				<td><form:label path="abo">${model.aboDisplayName}</form:label></td>
				<td><form:radiobutton path="abo" value="A" label="A"
						class="radioWithToggle" /> <form:radiobutton path="abo" value="B"
						label="B" class="radioWithToggle" /> <form:radiobutton path="abo"
						value="AB" label="AB" class="radioWithToggle" /> <form:radiobutton
						path="abo" value="O" label="O" class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="rhd">${model.rhdDisplayName}</form:label></td>
				<td><form:radiobutton path="rhd" value="negative"
						label="negative" class="radioWithToggle" /> <form:radiobutton
						path="rhd" value="positive" label="positive"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="status">${model.statusDisplayName}</form:label></td>
				<td><form:radiobutton path="status" value="partiallyFulfilled"
						label="Partially Fulfilled" /> <form:radiobutton path="status"
						value="pending" label="Pending" /> <form:radiobutton
						path="status" value="fulfilled" label="Fulfilled" /></td>
			</tr>
			<tr>
				<td><form:label path="sites">${model.siteDisplayName}</form:label></td>
				<td style="padding-left: 10px;"><form:select path="sites"
						id="editRequestFormSites-${formId}" class="editRequestFormSites">
						<c:forEach var="site" items="${model.sites}">
							<form:option value="${site}" label="${site}"
								selected="${site == model.selectedSite ? 'selected' : ''}" />
						</c:forEach>
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="productType">${model.productTypeDisplayName}</form:label></td>
				<td style="padding-left: 10px;"><form:select path="productType"
						id="editRequestFormTypes-${formId}" class="editRequestFormTypes">
						<form:option value="wholeBlood" label="Whole Blood"
							selected="${model.selectedType == 'wholeBlood' ? 'selected' : ''}" />
						<form:option value="rcc" label="RCC"
							selected="${model.selectedType == 'rcc' ? 'selected' : ''}" />
						<form:option value="ffp" label="ffp"
							selected="${model.selectedType == 'ffp' ? 'selected' : ''}" />
						<form:option value="platelets" label="Platelets"
							selected="${model.selectedType == 'platelets' ? 'selected' : ''}" />
						<form:option value="partialPlatelets" label="Partial Platelets"
							selected="${model.selectedType == 'partialPlatelets' ? 'selected' : ''}" />
					</form:select></td>
			</tr>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td />
					<td><input type="button" value="Add Request"
						class="addRequestButton" onclick="updateRequest();" /></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form:form>
