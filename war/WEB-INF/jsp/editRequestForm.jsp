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
						$("#${tabContentId}").parent().trigger("editRequestSuccess");
				}

        function notifyParentCancel() {
					// let the parent know we are done
					$("#${tabContentId}").parent().trigger("editRequestCancel");
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
                  														"${tabContentId}",
                  														notifyParentSuccess);
              else
                addNewRequest($("#${editRequestFormId}")[0],
                    									"${tabContentId}", notifyParentSuccess);
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

        $("#${editRequestFormId}").find(".requestType").multiselect({
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

        $("#${tabContentId}").find(".clearFormButton").button({
          icons : {
            
          }
        }).click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${model.refreshUrl}",
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

        if ("${model.existingRequest}" !== "true" && "${model.hasErrors}" !== "true") {
        	$("#${tabContentId}").find('textarea[name="notes"]').html("${model.requestFields.notes.defaultValue}");
        	setDefaultValueForSelector(getBloodGroupSelector(), "${model.requestFields.bloodGroup.defaultValue}");
        	setDefaultValueForSelector(getProductTypeSelector(), "${model.requestFields.productType.defaultValue}");
        	setDefaultValueForSelector(getRequestTypeSelector(), "${model.requestFields.requestType.defaultValue}");
        	setDefaultValueForSelector(getRequestSiteSelector(), "${model.requestFields.requestSite.defaultValue}");
          copyMirroredFields("${tabContentId}", JSON.parse('${model.requestFields.mirroredFields}'));
        }

        function getBloodGroupSelector() {
          return $("#${tabContentId}").find('select[name="bloodGroup"]').multiselect();
        }
        
        function getProductTypeSelector() {
          return $("#${tabContentId}").find('select[name="productType"]').multiselect();
        }

        function getRequestTypeSelector() {
          return $("#${tabContentId}").find('select[name="requestType"]').multiselect();
        }

        function getRequestSiteSelector() {
          return $("#${tabContentId}").find('select[name="requestSite"]').multiselect();
        }
        
        function updateBarcode(val) {
          if (val === null || val === undefined || val === "")
            val = "-";
	        $("#${editRequestFormId}").find(".barcodeContainer").barcode(
						  val,
							"code128",
							{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
        }
        updateBarcode("${editRequestForm.request.requestNumber}");

        $("#${editRequestFormId}").find('input[name="requestNumber"]').keyup(function() {
          updateBarcode($(this).val());
        });
        

      });
</script>

<div id="${tabContentId}">

	<form:form method="POST" commandName="editRequestForm"
		class="formInTabPane" id="${editRequestFormId}">
		<div class="barcodeContainer"></div>
		<form:hidden path="id" />
		<c:if test="${model.requestFields.requestNumber.hidden != true }">
			<div>
				<form:label path="requestNumber">${model.requestFields.requestNumber.displayName}</form:label>
				<form:input path="requestNumber" value="${model.existingRequest ? '' : model.requestFields.requestNumber.defaultValue}" />
				<form:errors class="formError"
					path="request.requestNumber" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.bloodGroup.hidden != true }">
			<div>
				<form:label path="bloodGroup">${model.requestFields.bloodGroup.displayName}</form:label>
				<form:select path="bloodGroup" class="bloodGroup">
					<form:option value="" label="" />
					<form:option value="A+" label="A+" />
					<form:option value="A-" label="A-" />
					<form:option value="B+" label="B+" />
					<form:option value="B-" label="B-" />
					<form:option value="AB+" label="AB+" />
					<form:option value="AB-" label="AB-" />
					<form:option value="O+" label="O+" />
					<form:option value="O-" label="O-" />
				</form:select>
				<form:errors class="formError" path="request.bloodGroup" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestDate.hidden != true }">
			<div>
				<form:label path="requestDate">${model.requestFields.requestDate.displayName}</form:label>
				<form:input path="requestDate" class="requestDate" value="${model.existingRequest ? '' : model.requestFields.requestDate.defaultValue}" />
				<form:errors class="formError" path="request.requestDate"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requiredDate.hidden != true }">
			<div>
				<form:label path="requiredDate">${model.requestFields.requiredDate.displayName}</form:label>
				<form:input path="requiredDate" class="requiredDate" value="${model.existingRequest ? '' : model.requestFields.requiredDate.defaultValue}" />
				<form:errors class="formError" path="request.requiredDate"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestType.hidden != true }">
			<div>
				<form:label path="requestType">${model.requestFields.requestType.displayName}</form:label>
				<form:select path="requestType" class="requestType">
					<form:option value="">&nbsp;</form:option>
					<c:forEach var="requestType" items="${model.requestTypes}">
						<form:option value="${requestType.id}">${requestType.requestType}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="request.requestType"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.productType.hidden != true }">
			<div>
				<form:label path="productType">${model.requestFields.productType.displayName}</form:label>
				<form:select path="productType" class="productType">
					<form:option value="">&nbsp;</form:option>
					<c:forEach var="productType" items="${model.productTypes}">
						<form:option value="${productType.id}">${productType.productType}</form:option>
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
		<c:if test="${model.requestFields.patientFirstName.hidden != true }">
			<div>
				<form:label path="patientFirstName">${model.requestFields.patientFirstName.displayName}</form:label>
				<form:input path="patientFirstName" value="${model.existingRequest ? '' : model.requestFields.patientFirstName.defaultValue}" />
				<form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.patientLastName.hidden != true }">
			<div>
				<form:label path="patientLastName">${model.requestFields.patientLastName.displayName}</form:label>
				<form:input path="patientLastName" value="${model.existingRequest ? '' : model.requestFields.patientFirstName.defaultValue}" />
				<form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.patientBirthDate.hidden != true }">
			<div>
				<form:label path="patientBirthDate">${model.requestFields.patientBirthDate.displayName}</form:label>
				<form:input path="patientBirthDate" class="patientBirthDate"
										value="${model.existingRequest ? '' : model.requestFields.patientBirthDate.defaultValue}" />
				<form:errors class="formError" path="request.patientBirthDate"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.patientAge.hidden != true }">
			<div>
				<form:label path="patientAge">${model.requestFields.patientAge.displayName}</form:label>
				<form:input path="patientAge" value="${model.existingRequest ? '' : model.requestFields.patientAge.defaultValue}"
									  type="number" min="0" max="120" />
					years
				<form:errors class="formError" path="request.patientAge" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.patientDiagnosis.hidden != true }">
			<div>
				<form:label path="patientDiagnosis">${model.requestFields.patientDiagnosis.displayName}</form:label>
				<form:input path="patientDiagnosis" value="${model.existingRequest ? '' : model.requestFields.patientDiagnosis.defaultValue}" />
				<form:errors class="formError" path="request.patientDiagnosis" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.ward.hidden != true }">
			<div>
				<form:label path="ward">${model.requestFields.ward.displayName}</form:label>
				<form:input path="ward" value="${model.existingRequest ? '' : model.requestFields.ward.defaultValue}" />
				<form:errors class="formError" path="request.ward" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.department.hidden != true }">
			<div>
				<form:label path="department">${model.requestFields.department.displayName}</form:label>
				<form:input path="department" value="${model.existingRequest ? '' : model.requestFields.department.defaultValue}" />
				<form:errors class="formError" path="request.department" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.hospital.hidden != true }">
			<div>
				<form:label path="hospital">${model.requestFields.hospital.displayName}</form:label>
				<form:input path="hospital" value="${model.existingRequest ? '' : model.requestFields.hospital.defaultValue}" />
				<form:errors class="formError" path="request.hospital" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestedBy.hidden != true }">
			<div>
				<form:label path="requestedBy">${model.requestFields.requestedBy.displayName}</form:label>
				<form:input path="requestedBy" value="${model.existingRequest ? '' : model.requestFields.requestedBy.defaultValue}" />
				<form:errors class="formError" path="request.requestedBy" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.requestFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.requestFields.notes.displayName}</form:label>
				<form:textarea path="notes" />
				<form:errors class="formError" path="request.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>
	</form:form>

	<div style="margin-left: 200px;">
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

</div>
