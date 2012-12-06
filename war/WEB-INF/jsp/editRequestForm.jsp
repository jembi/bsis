<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editRequestFormDivId">editRequestFormDiv-${unique_page_id}</c:set>
<c:set var="editRequestFormId">editRequestForm-${unique_page_id}</c:set>
<c:set var="editRequestFormBarcodeId">editRequestFormBarcode-${unique_page_id}</c:set>
<c:set var="editRequestFormDonorId">editRequestFormDonor-${unique_page_id}</c:set>
<c:set var="editRequestFormDonorHiddenId">editRequestFormDonorHidden-${unique_page_id}</c:set>
<c:set var="editRequestFormCentersId">editRequestFormCenters-${unique_page_id}</c:set>
<c:set var="editRequestFormSitesId">editRequestFormSites-${unique_page_id}</c:set>
<c:set var="editRequestFormProductTypeId">editRequestFormProductType-${unique_page_id}</c:set>
<c:set var="editRequestFormDonorTypeId">editRequestFormDonorType-${unique_page_id}</c:set>
<c:set var="updateRequestButtonId">updateProductButton-${unique_page_id}</c:set>
<c:set var="deleteProductButtonId">deleteProductButton-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>
<c:set var="cancelButtonId">cancelButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
						// let the parent know we are done
						$("#${editRequestFormDivId}").parent().trigger("editRequestSuccess");
				}

        function notifyParentCancel() {
					// let the parent know we are done
					$("#${editRequestFormDivId}").parent().trigger("editRequestCancel");
				}

        $("#${cancelButtonId}").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(
	           function() {
               notifyParentCancel();
        });

        $("#${updateRequestButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingRequest}" == "true")
                updateExistingRequest($("#${editRequestFormId}")[0],
                  														"${editRequestFormDivId}",
                  														notifyParentSuccess);
              else
                addNewRequest($("#${editRequestFormId}")[0],
                    									"${editRequestFormDivId}", notifyParentSuccess);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${editRequestFormId}").printArea();
        });

        $("#${editRequestFormId}").find(".bloodGroup").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editRequestFormId}").find(".productType").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editRequestFormId}").find(".requestSites").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editRequestFormId}").find(".requestDate").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c0",
          onSelect : function(selectedDate) {
            $("#${editRequestFormId}").find(".requiredDate").datepicker("option", "minDate", selectedDate);
          },
        });

        $("#${editRequestFormId}").find(".requiredDate").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 60,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c+1",
          onSelect : function(selectedDate) {
            $("#${editRequestFormId}").find(".requestDate").datepicker("option", "maxDate", selectedDate);
          },
        });

        // second condition required for the case where the form is returned with errors
        var requestedOnDatePicker = $("#${editRequestFormId}").find(".requestDate");
        if ("${model.existingRequest}" == "false" && requestedOnDatePicker.val() == "") {
          requestedOnDatePicker.datepicker('setDate', new Date());
        }

        // set the checkboxes if required
        var isAvailableCheckbox = $("#${editRequestFormId}").find(".isAvailable");
        if ("${model.existingRequest}" == "false") {
          isAvailableCheckbox.prop("checked", true);
        }

        var isQuarantinedCheckbox = $("#${editRequestFormId}").find(".isQuarantined");
        if ("${model.existingRequest}" == "false") {
          isQuarantinedCheckbox.prop("checked", true);
        }

        $("#${editRequestFormId}").find(".clearFormButton").button({
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
              			 	 $("#${editRequestFormDivId}").replaceWith(response);
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

        $("#${editRequestFormBarcodeId}").barcode(
					  "${editRequestForm.request.requestNumber}",
						"code128",
						{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});

        copyMirroredFields("${editRequestFormId}", JSON.parse('${model.requestFields.mirroredFields}'));

      });
</script>

<div id="${editRequestFormDivId}">

	<form:form method="POST" commandName="editRequestForm"
		class="formInTabPane" id="${editRequestFormId}">
		<c:if test="${model.existingRequest}">
			<div id="${editRequestFormBarcodeId}"></div>
		</c:if>
		<form:hidden path="id" />
		<c:if test="${model.requestFields.requestNumber.hidden != true }">
			<div>
				<form:label path="requestNumber">${model.requestFields.requestNumber.displayName}</form:label>
				<form:input path="requestNumber" />
				<form:errors class="formError"
					path="request.requestNumber" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.bloodGroup.hidden != true }">
			<div>
				<form:label path="bloodGroup">${model.requestFields.bloodGroup.displayName}</form:label>
				<form:select path="bloodGroup" class="bloodGroup">
					<form:option value="Unknown" label="Unknown" />
					<form:option value="A+" label="A+" />
					<form:option value="A-" label="A-" />
					<form:option value="B+" label="B+" />
					<form:option value="B-" label="B-" />
					<form:option value="AB+" label="AB+" />
					<form:option value="AB-" label="AB-" />
					<form:option value="O+" label="O+" />
					<form:option value="O-" label="O-" />
				</form:select>
				<form:errors class="formError" path="bloodGroup" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestDate.hidden != true }">
			<div>
				<form:label path="requestDate">${model.requestFields.requestDate.displayName}</form:label>
				<form:input path="requestDate" class="requestDate" />
				<form:errors class="formError" path="request.requestDate"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requiredDate.hidden != true }">
			<div>
				<form:label path="requiredDate">${model.requestFields.requiredDate.displayName}</form:label>
				<form:input path="requiredDate" class="requiredDate" />
				<form:errors class="formError" path="request.requiredDate"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.productType.hidden != true }">
			<div>
				<form:label path="productType">${model.requestFields.productType.displayName}</form:label>
				<form:select path="productType" class="productType">
					<form:option value="">&nbsp;</form:option>
					<c:forEach var="productType" items="${model.productTypes}">
						<form:option value="${productType}">${productType}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="request.productType"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestedQuantity.hidden != true }">
			<div>
				<form:label path="requestedQuantity">${model.requestFields.requestedQuantity.displayName}</form:label>
				<form:input path="requestedQuantity" value="${model.existingRequest ? '' : model.requestFields.requestedQuantity.defaultValue}" />
				<form:errors class="formError" path="request.requestedQuantity" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestSite.hidden != true }">
			<div>
				<form:label path="requestSite">${model.requestFields.requestSite.displayName}</form:label>
				<form:select path="requestSite"
					class="requestSites">
					<form:option value="" selected="selected">&nbsp;</form:option>
					<c:forEach var="site" items="${model.sites}">
						<form:option value="${site.id}">${site.name}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="request.requestSite" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.patientName.hidden != true }">
			<div>
				<form:label path="patientName">${model.requestFields.patientName.displayName}</form:label>
				<form:input path="patientName" value="${model.existingRequest ? '' : model.requestFields.patientName.defaultValue}" />
				<form:errors class="formError" path="request.patientName" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.requestFields.notes.displayName}</form:label>
				<form:textarea path="notes" maxlength="255" />
				<form:errors class="formError" path="request.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<div>
			<label></label>
			<c:if test="${!(model.existingRequest)}">
				<button type="button" id="${updateRequestButtonId}">
					Save and add another
				</button>
				<button type="button" class="clearFormButton">
					Clear form
				</button>
			</c:if>
			<c:if test="${model.existingRequest}">
				<button type="button" id="${updateRequestButtonId}"
								class="autoWidthButton">Save</button>
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
