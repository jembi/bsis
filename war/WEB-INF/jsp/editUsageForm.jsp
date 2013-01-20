<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editUsageFormDivId">editUsageFormDiv-${unique_page_id}</c:set>
<c:set var="editUsageFormBarcodeId">editUsageFormBarcode-${unique_page_id}</c:set>
<c:set var="editUsageFormId">editUsageForm-${unique_page_id}</c:set>
<c:set var="deleteUsageConfirmDialogId">deleteUsageConfirmDialog-${unique_page_id}</c:set>
<c:set var="usageDateInputId">usageDateInput-${unique_page_id}</c:set>
<c:set var="updateUsageButtonId">updateUsageButton-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>
<c:set var="cancelButtonId">cancelButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
					 // let the parent know we are done
				   $("#${editUsageFormDivId}").parent().trigger("editUsageSuccess");
    		}

        function notifyParentCancel() {
					 // let the parent know we are done
				   $("#${editUsageFormDivId}").parent().trigger("editUsageCancel");
	   		}

        $("#${cancelButtonId}").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(
	           function() {
               notifyParentCancel();
        });

        $("#${updateUsageButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingUsage}" == "true")
                updateExistingUsage($("#${editUsageFormId}")[0],
                    								"${editUsageFormDivId}", notifyParentSuccess);
              else
                addNewUsage($("#${editUsageFormId}")[0], "${editUsageFormDivId}", notifyParentSuccess);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${editUsageFormId}").printArea();
        });

        $("#${editUsageFormId}").find(".clearFormButton").button({
          icons : {
            primary : 'ui-icon-grip-solid-horizontal'
          }
        }).click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${model.refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
              			 	 $("#${editUsageFormDivId}").replaceWith(response);
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

        $("#${usageDateInputId}").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c0",
        });

        if ("${model.existingUsage}" !== "true")
        	$("#${editUsageFormDivId}").find('textarea[name="notes"]').html("${model.usageFields.notes.defaultValue}");

        copyMirroredFields("${editUsageFormId}", JSON.parse('${model.usageFields.mirroredFields}'));
      });
</script>

<div id="${editUsageFormDivId}">

	<div class="tipsBox ui-state-highlight">
		<p>
			${model['usage.addusage']}
		</p>
	</div>

	<form:form id="${editUsageFormId}" method="POST" class="formInTabPane"
		commandName="editUsageForm">
		<form:hidden path="id" />
		<c:if test="${model.usageFields.product.hidden != true }">
			<div>
				<form:label path="productNumber">${model.usageFields.product.displayName}</form:label>
				<form:input path="productNumber" value="${model.existingUsage ? '' : model.usageFields.product.defaultValue}" />
				<form:errors class="formError" path="usage.product"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.usageFields.hospital.hidden != true }">
			<div>
				<form:label path="hospital">${model.usageFields.hospital.displayName}</form:label>
				<form:input path="hospital" value="${model.existingUsage ? '' : model.usageFields.hospital.defaultValue}" />
				<form:errors class="formError" path="usage.hospital"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.usageFields.patientName.hidden != true }">
			<div>
				<form:label path="patientName">${model.usageFields.patientName.displayName}</form:label>
				<form:input path="patientName" value="${model.existingUsage ? '' : model.usageFields.patientName.defaultValue}" />
				<form:errors class="formError" path="usage.patientName" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.usageFields.ward.hidden != true }">
			<div>
				<form:label path="ward">${model.usageFields.ward.displayName}</form:label>
				<form:input path="ward" value="${model.existingUsage ? '' : model.usageFields.ward.defaultValue}" />
				<form:errors class="formError" path="usage.ward"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.usageFields.lastName.hidden != true }">
			<div>
				<form:label path="useIndication">${model.usageFields.useIndication.displayName}</form:label>
				<form:input path="useIndication" value="${model.existingUsage ? '' : model.usageFields.useIndication.defaultValue}" />
				<form:errors class="formError" path="usage.useIndication" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.usageFields.usageDate.hidden != true }">
			<div>
				<form:label path="usageDate">${model.usageFields.usageDate.displayName}</form:label>
				<form:input path="usageDate" id="${usageDateInputId}"
										value="${model.existingUsage ? '' : model.usageFields.usageDate.defaultValue}" />
				<form:errors class="formError" path="usage.usageDate" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.usageFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.usageFields.notes.displayName}</form:label>
				<form:textarea path="notes" value="${model.existingUsage ? '' : model.usageFields.notes.defaultValue}" />
				<form:errors class="formError" path="usage.notes"></form:errors>
			</div>
		</c:if>
		<div>
			<label></label>
			<c:if test="${!(model.existingUsage)}">
				<button type="button" id="${updateUsageButtonId}" class="autoWidthButton">
					Save and add another
				</button>
				<button type="button" class="clearFormButton autoWidthButton">
					Clear form
				</button>				
			</c:if>
			<c:if test="${model.existingUsage}">
				<button type="button" id="${updateUsageButtonId}">
					Save
				</button>
				<button type="button" id="${cancelButtonId}">
					Cancel
				</button>
			</c:if>

			<button type="button" id="${printButtonId}">
				Print
			</button>

		</div>
	</form:form>
</div>
