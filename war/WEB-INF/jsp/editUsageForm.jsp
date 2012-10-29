<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editUsageFormId">editUsageForm-${unique_page_id}</c:set>
<c:set var="deleteUsageConfirmDialogId">deleteUsageConfirmDialog-${unique_page_id}</c:set>
<c:set var="editUsageFormUsageDateId">editUsageFormUsageDateId-${unique_page_id}</c:set>
<c:set var="updateUsageButtonId">updateUsageButton-${unique_page_id}</c:set>
<c:set var="deleteUsageButtonId">deleteUsageButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {
        $("#${updateUsageButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(function() {
          updateExistingUsage($("#${editUsageFormId}")[0]);
        });

        $("#${deleteUsageButtonId}").button({
          icons : {
            primary : 'ui-icon-minusthick'
          }
        }).click(
            function() {
              $("#${deleteUsageConfirmDialogId}").dialog(
                  {
                    modal : true,
                    title : "Confirm Delete",
                    buttons : {
                      "Delete" : function() {
                        var productNumber = $("#${editUsageFormId}").find(
                            "[name='productNumber']").val();
                        deleteUsage(productNumber);
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

        $("#${editUsageFormUsageDateId}").datepicker({
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
	<form:form method="POST" commandName="editUsageForm"
		id="${editUsageFormId}">
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
					<td><form:input path="dateUsed"
							id="${editUsageFormUsageDateId}" /></td>
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
						<td><button type="button" id="${updateUsageButtonId}"
								style="margin-left: 10px">Save changes</button>
							<button type="button" id="${deleteUsageButtonId}"
								style="margin-left: 10px">Delete</button>
							<button type="button" id="${goBackButtonId}"
								style="margin-left: 10px">Go Back</button></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</form:form>
</div>

<div id="${deleteUsageConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Usage?</div>
