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
<c:set var="addRequestFormId">addRequestForm-${unique_page_id}</c:set>

<c:set var="addRequestFormBloodAboSelectorId">addRequestFormBloodAboSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormBloodRhSelectorId">addRequestFormBloodRhSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormProductTypeSelectorId">addRequestFormProductTypeSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormRequestTypeSelectorId">addRequestFormRequestTypeSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormRequestSiteSelectorId">addRequestFormRequestSiteSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormPatientGenderSelectorId">addRequestFormPatientGenderSelectorId-${unique_page_id}</c:set>

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

      $("#${mainContentId}").find(".addRequestButton").button({
        icons : {
          primary : 'ui-icon-plusthick'
        }
      }).click(
          function() {
            addNewRequest($("#${addRequestFormId}")[0],
            								 "${tabContentId}", notifyParentSuccess);
          });

      $("#${addRequestFormId}").find(".bloodAbo").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".bloodRh").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".productType").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".requestType").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".requestSites").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".requestDate").datetimepicker({
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 1,
        dateFormat : "dd/mm/yy",
        timeFormat : "hh:mm:ss tt",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          //$("#${addRequestFormId}").find(".requiredDate").datetimepicker("option", "minDate", selectedDate);
        },
      });

      $("#${addRequestFormId}").find(".requiredDate").datepicker({
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 60,
        dateFormat : "dd/mm/yy",
        yearRange : "c-100:c+1",
        onSelect : function(selectedDate) {
          //$("#${addRequestFormId}").find(".requestDate").datetimepicker("option", "maxDate", selectedDate);
        },
      });

      $("#${tabContentId}").find(".clearFormButton").button({
        icons : {
          
        }
      }).click(refetchForm);

      function refetchForm() {
        $.ajax({
          url: "${refreshUrl}",
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

      getGenderSelector().multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      if ("${firstTimeRender}" == "true") {
      	$("#${tabContentId}").find('textarea[name="notes"]').html("${requestFields.notes.defaultValue}");
      	setDefaultValueForSelector(getBloodAboSelector(), "${requestFields.patientBloodAbo.defaultValue}");
      	setDefaultValueForSelector(getBloodRhSelector(), "${requestFields.patientBloodRh.defaultValue}");
      	setDefaultValueForSelector(getProductTypeSelector(), "${requestFields.productType.defaultValue}");
      	setDefaultValueForSelector(getRequestTypeSelector(), "${requestFields.requestType.defaultValue}");
      	setDefaultValueForSelector(getRequestSiteSelector(), "${requestFields.requestSite.defaultValue}");
      	setDefaultValueForSelector(getGenderSelector(), "${requestFields.patientGender.defaultValue}");
      }

      function getGenderSelector() {
        return $("#${mainContentId}").find('select[name="patientGender"]').multiselect();
      }

      function getBloodAboSelector() {
        return $("#${tabContentId}").find('select[name="bloodAbo"]').multiselect();
      }
      
      function getBloodRhSelector() {
        return $("#${tabContentId}").find('select[name="bloodRh"]').multiselect();
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
       $("#${addRequestFormId}").find(".barcodeContainer").barcode(
				  val,
					"code128",
					{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
      }
      updateBarcode("${addRequestForm.request.requestNumber}");

      $("#${addRequestFormId}").find('input[name="requestNumber"]').keyup(function() {
        updateBarcode($(this).val());
      });
      

    });
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
		</c:if>

		<form:form method="POST" commandName="addRequestForm"
			class="formInTabPane" id="${addRequestFormId}">
			<c:if test="${!requestFields.requestNumber.autoGenerate}">		
				<c:if test="${requestFields.requestNumber.hidden != true }">
					<div>
						<form:label path="requestNumber">${requestFields.requestNumber.displayName}</form:label>
						<form:input path="requestNumber" value="${firstTimeRender ?  requestFields.requestNumber.defaultValue : ''}" />
						<form:errors class="formError"
							path="request.requestNumber" delimiter=", "></form:errors>
					</div>
				</c:if>
			</c:if>
			<c:if test="${requestFields.patientBloodAbo.hidden != true }">
				<div>
					<form:label path="patientBloodAbo">${requestFields.patientBloodAbo.displayName}</form:label>
					<form:select path="patientBloodAbo"
											 id="${addRequestFormBloodAboSelectorId}"
											 class="bloodAbo">
						<form:option value="" label="" />
						<form:option value="A" label="A" />
						<form:option value="B" label="B" />
						<form:option value="AB" label="AB" />
						<form:option value="O" label="O" />
					</form:select>
					<form:errors class="formError" path="request.patientBloodAbo" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientBloodRh.hidden != true }">
				<div>
					<form:label path="patientBloodRh">${requestFields.patientBloodRh.displayName}</form:label>
					<form:select path="patientBloodRh"
											 id="${addRequestFormBloodRhSelectorId}"
											 class="bloodRh">
						<form:option value="" label="" />
						<form:option value="+" label="POS" />
						<form:option value="-" label="NEG" />
					</form:select>
					<form:errors class="formError" path="request.patientBloodRh" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientGender.hidden != true}">
				<div>
					<form:label path="patientGender">${requestFields.patientGender.displayName}</form:label>
					<form:select path="patientGender" id="${addRequestFormPatientGenderSelectorId}">
						<form:option value="not_known" label="Not Known" />
						<form:option value="male" label="Male" />
						<form:option value="female" label="Female" />
						<form:option value="not_applicable" label="Not Applicable" />
					</form:select>
					<form:errors class="formError" path="request.patientGender" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestDate.hidden != true }">
				<c:if test="${requestFields.requestDate.isTimeField == false or requestFields.requestDate.useCurrentTime == false}">
					<div>
						<form:label path="requestDate">${requestFields.requestDate.displayName}</form:label>
						<form:input path="requestDate" class="requestDate" value="${firstTimeRender ?  requestFields.requestDate.defaultValue : ''}" />
						<form:errors class="formError" path="request.requestDate"
							delimiter=", "></form:errors>
					</div>
				</c:if>
			</c:if>
			<c:if test="${requestFields.requiredDate.hidden != true }">
				<div>
					<form:label path="requiredDate">${requestFields.requiredDate.displayName}</form:label>
					<form:input path="requiredDate" class="requiredDate" value="${firstTimeRender ?  requestFields.requiredDate.defaultValue : ''}" />
					<form:errors class="formError" path="request.requiredDate"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestType.hidden != true }">
				<div>
					<form:label path="requestType">${requestFields.requestType.displayName}</form:label>
					<form:select path="requestType"
											 id="${addRequestFormRequestTypeSelectorId}"
											 class="requestType">
						<form:option value="">&nbsp;</form:option>
						<c:forEach var="requestType" items="${requestTypes}">
							<form:option value="${requestType.id}">${requestType.requestType}</form:option>
						</c:forEach>
					</form:select>
					<form:errors class="formError" path="requestType"
						delimiter=", "></form:errors>
					<form:errors class="formError" path="request.requestType"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.productType.hidden != true }">
				<div>
					<form:label path="productType">${requestFields.productType.displayName}</form:label>
					<form:select path="productType"
											 id="${addRequestFormProductTypeSelectorId}"
											 class="productType">
						<form:option value="">&nbsp;</form:option>
						<c:forEach var="productType" items="${productTypes}">
							<form:option value="${productType.id}">${productType.productType}</form:option>
						</c:forEach>
					</form:select>
					<form:errors class="formError" path="request.productType"
						delimiter=", "></form:errors>
					<form:errors class="formError" path="productType"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.numUnitsRequested.hidden != true }">
				<div>
					<form:label path="numUnitsRequested">${requestFields.numUnitsRequested.displayName}</form:label>
					<form:input path="numUnitsRequested" value="${firstTimeRender ?  requestFields.numUnitsRequested.defaultValue : ''}" />
					<form:errors class="formError" path="request.numUnitsRequested" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestSite.hidden != true }">
				<div>
					<form:label path="requestSite">${requestFields.requestSite.displayName}</form:label>
					<form:select path="requestSite"
						id="${addRequestFormRequestSiteSelectorId}"
						class="requestSites">
						<form:option value="" selected="selected">&nbsp;</form:option>
						<c:forEach var="site" items="${sites}">
							<form:option value="${site.id}">${site.name}</form:option>
						</c:forEach>
					</form:select>
					<form:errors class="formError" path="request.requestSite" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientNumber.hidden != true }">
				<div>
					<form:label path="patientNumber">${requestFields.patientNumber.displayName}</form:label>
					<form:input path="patientNumber" value="${firstTimeRender ?  requestFields.patientNumber.defaultValue : ''}" />
					<form:errors class="formError" path="request.patientNumber" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientFirstName.hidden != true }">
				<div>
					<form:label path="patientFirstName">${requestFields.patientFirstName.displayName}</form:label>
					<form:input path="patientFirstName" value="${firstTimeRender ?  requestFields.patientFirstName.defaultValue : ''}" />
					<form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientLastName.hidden != true }">
				<div>
					<form:label path="patientLastName">${requestFields.patientLastName.displayName}</form:label>
					<form:input path="patientLastName" value="${firstTimeRender ?  requestFields.patientFirstName.defaultValue : ''}" />
					<form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientBirthDate.hidden != true }">
				<div>
					<form:label path="patientBirthDate">${requestFields.patientBirthDate.displayName}</form:label>
					<form:input path="patientBirthDate" class="patientBirthDate"
											value="${firstTimeRender ?  requestFields.patientBirthDate.defaultValue : ''}" />
					<form:errors class="formError" path="request.patientBirthDate"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientAge.hidden != true }">
				<div>
					<form:label path="patientAge">${requestFields.patientAge.displayName}</form:label>
					<form:input path="patientAge" value="${firstTimeRender ?  requestFields.patientAge.defaultValue : ''}"
										  type="number" min="0" max="120" />
						years
					<form:errors class="formError" path="request.patientAge" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientDiagnosis.hidden != true }">
				<div>
					<form:label path="patientDiagnosis">${requestFields.patientDiagnosis.displayName}</form:label>
					<form:input path="patientDiagnosis" value="${firstTimeRender ?  requestFields.patientDiagnosis.defaultValue : ''}" />
					<form:errors class="formError" path="request.patientDiagnosis" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.ward.hidden != true }">
				<div>
					<form:label path="ward">${requestFields.ward.displayName}</form:label>
					<form:input path="ward" value="${firstTimeRender ?  requestFields.ward.defaultValue : ''}" />
					<form:errors class="formError" path="request.ward" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.department.hidden != true }">
				<div>
					<form:label path="department">${requestFields.department.displayName}</form:label>
					<form:input path="department" value="${firstTimeRender ?  requestFields.department.defaultValue : ''}" />
					<form:errors class="formError" path="request.department" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.hospital.hidden != true }">
				<div>
					<form:label path="hospital">${requestFields.hospital.displayName}</form:label>
					<form:input path="hospital" value="${firstTimeRender ?  requestFields.hospital.defaultValue : ''}" />
					<form:errors class="formError" path="request.hospital" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestedBy.hidden != true }">
				<div>
					<form:label path="requestedBy">${requestFields.requestedBy.displayName}</form:label>
					<form:input path="requestedBy" value="${firstTimeRender ?  requestFields.requestedBy.defaultValue : ''}" />
					<form:errors class="formError" path="request.requestedBy" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.notes.hidden != true }">
				<div>
					<form:label path="notes" class="labelForTextArea">${requestFields.notes.displayName}</form:label>
					<form:textarea path="notes" />
					<form:errors class="formError" path="request.notes"
						delimiter=", "></form:errors>
				</div>
			</c:if>
		</form:form>

		<div style="margin-left: 200px;">
			<label></label>
			<button type="button" class="addRequestButton autoWidthButton">
				Add Request
			</button>
			<button type="button" class="clearFormButton autoWidthButton">
				Clear form
			</button>				
		</div>
	</div>
</div>
