<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editTestResultFormId">editTestResultForm-${unique_page_id}</c:set>
<c:set var="deleteTestResultConfirmDialogId">deleteTestResultConfirmDialog-${unique_page_id}</c:set>
<c:set var="dateTestedId">dateTested-${unique_page_id}</c:set>
<c:set var="updateTestResultButtonId">updateTestResultButton-${unique_page_id}</c:set>
<c:set var="deleteTestResultButtonId">deleteTestResultButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {
        $("#${updateTestResultButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(function() {
          updateExistingTestResult($("#${editTestResultFormId}")[0]);
        });

        $("#${deleteTestResultButtonId}").button({
          icons : {
            primary : 'ui-icon-minusthick'
          }
        }).click(
            function() {
              $("#${deleteTestResultConfirmDialogId}").dialog(
                  {
                    modal : true,
                    title : "Confirm Delete",
                    buttons : {
                      "Delete" : function() {
                        var collectionNumber = $("#${editTestResultFormId}")
                            .find("[name='collectionNumber']").val();
                        deleteTestResult(collectionNumber,
                            $("#${editTestResultFormId}"));
                        $(this).dialog("close");
                      },
                      "Cancel" : function() {
                        $(this).dialog("close");
                      }
                    }
                  });
            });

        $("#${goBackButtonId}").button({
          icons : {
            primary : 'ui-icon-circle-arrow-w'
          }
        }).click(function() {
          window.history.back();
          return false;
        });

        $("#${dateTestedId}").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c0",
        });
      });
</script>

<div class="editFormDiv">
<form:form method="POST" commandName="editTestResultForm"
	id="${editTestResultFormId}">
	<table>
		<thead>
				<tr>
					<td><b>Test Result</b></td>
				</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="collectionNumber">${model.collectionNoDisplayName}</form:label></td>
				<td><form:input path="collectionNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="dateTested">${model.dateTestedDisplayName}</form:label></td>
				<td><form:input path="dateTested" id="${dateTestedId}" /></td>
			</tr>
			<tr>
				<td><form:label path="hiv">${model.hivDisplayName}</form:label></td>
				<td><form:radiobutton path="hiv" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="hiv" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="hbv">${model.hbvDisplayName}</form:label></td>
				<td><form:radiobutton path="hbv" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="hbv" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="hcv">${model.hcvDisplayName}</form:label></td>
				<td><form:radiobutton path="hcv" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="hcv" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="syphilis">${model.syphilisDisplayName}</form:label></td>
				<td><form:radiobutton path="syphilis" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="syphilis" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td />
					<td><button type="button" id="${updateTestResultButtonId}"
							style="margin-left: 10px">Save</button>
						<button type="button" id="${deleteTestResultButtonId}"
							style="margin-left: 10px">Delete</button>
						<button type="button" id="${goBackButtonId}"
							style="margin-left: 10px">Go Back</button></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form:form>
</div>

<div id="${deleteTestResultConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Test Result?</div>
