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
  $(".addUsageButton").button();
  function updateUsage() {
    addNewUsage($("#editUsageForm-" + '<c:out value="${formId}"/>')[0]);
    $("#editUsageForm-" + '<c:out value="${formId}"/>')[0].reset();
  }

  $("#dateUsed-" + '<c:out value="${formId}"/>').datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
  });

  $("#editUsageFormUseIndications-" + '<c:out value="${formId}"/>')
      .multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

  function updateUsage() {
    addNewUsage($("#editUsageForm-" + '<c:out value="${formId}"/>')[0]);
    $("#editUsageForm-" + '<c:out value="${formId}"/>')[0].reset();
  }
</script>


<form:form method="POST" commandName="editUsageForm"
	id="editUsageForm-${formId}">
	<table>
		<thead>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td><b>Add New Usage</b></td>
				</tr>
			</c:if>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="productNumber">${model.productNoDisplayName}</form:label></td>
				<td><form:input path="productNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="dateUsed">${model.dateUsedDisplayName}</form:label></td>
				<td><form:input path="dateUsed" id="dateUsed-${formId}" /></td>
			</tr>
			<tr>
				<td><form:label path="hospital">${model.hospitalDisplayName}</form:label></td>
				<td><form:input path="hospital" /></td>
			</tr>
			<tr>
				<td><form:label path="ward">${model.wardDisplayName}</form:label></td>
				<td><form:input path="ward" /></td>
			</tr>
			<tr>
				<td><form:label path="useIndication">${model.useIndicationDisplayName}</form:label></td>
				<td><form:radiobutton path="useIndication" value="used"
						label="Used" /> <form:radiobutton path="useIndication"
						value="discarded" label="Discarded" /> <form:radiobutton
						path="useIndication" value="other" label="Other" /></td>
			</tr>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td />
					<td><input type="button" value="Add Usage"
						class="addUsageButton" onclick="updateUsage();" /></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form:form>
