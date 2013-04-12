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
<c:set var="editRequestFormId">editRequestForm-${unique_page_id}</c:set>

<c:set var="editRequestFormBloodAboSelectorId">editRequestFormBloodAboSelectorId-${unique_page_id}</c:set>
<c:set var="editRequestFormBloodRhSelectorId">editRequestFormBloodRhSelectorId-${unique_page_id}</c:set>
<c:set var="editRequestFormProductTypeSelectorId">editRequestFormProductTypeSelectorId-${unique_page_id}</c:set>
<c:set var="editRequestFormRequestTypeSelectorId">editRequestFormRequestTypeSelectorId-${unique_page_id}</c:set>
<c:set var="editRequestFormRequestSiteSelectorId">editRequestFormRequestSiteSelectorId-${unique_page_id}</c:set>
<c:set var="editRequestFormPatientGenderSelectorId">editRequestFormPatientGenderSelectorId-${unique_page_id}</c:set>

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

      $("#${mainContentId}").find(".cancelButton").button({
        icons : {
          primary : 'ui-icon-closethick'
        }
      }).click(
          function() {
             notifyParentCancel();
      });

      $("#${mainContentId}").find(".saveRequestButton").button({
        icons : {
          primary : 'ui-icon-plusthick'
        }
      }).click(
          function() {
            updateExistingRequest($("#${editRequestFormId}")[0],
            								 "${tabContentId}", notifyParentSuccess);
          });

      $("#${editRequestFormId}").find(".bloodAbo").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${editRequestFormId}").find(".bloodRh").multiselect({
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

      $("#${editRequestFormId}").find(".requestDate").datetimepicker({
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 1,
        dateFormat : "mm/dd/yy",
        timeFormat : "hh:mm:ss tt",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          //$("#${editRequestFormId}").find(".requiredDate").datetimepicker("option", "minDate", selectedDate);
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
          //$("#${editRequestFormId}").find(".requestDate").datetimepicker("option", "maxDate", selectedDate);
        },
      });

      $("#${tabContentId}").find(".clearFormButton")
      										 .button()
      										 .click(refetchForm);

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

	<div id="${mainContentId}">

		<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
		</c:if>

		<form:form method="POST" commandName="editRequestForm"
			class="formInTabPane" id="${editRequestFormId}">
			<form:hidden path="id" />
			<c:if test="${requestFields.requestNumber.hidden != true }">
				<div class="barcodeContainer"></div>
				<div>
					<form:label path="requestNumber">${requestFields.requestNumber.displayName}</form:label>
					<form:input path="requestNumber" />
					<form:errors class="formError"
						path="request.requestNumber" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientBloodAbo.hidden != true }">
				<div>
					<form:label path="patientBloodAbo">${requestFields.patientBloodAbo.displayName}</form:label>
					<form:select path="patientBloodAbo"
											 id="${editRequestFormBloodAboSelectorId}"
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
											 id="${editRequestFormBloodRhSelectorId}"
											 class="bloodRh">
						<form:option value="" label="" />
						<form:option value="+" label="+" />
						<form:option value="-" label="-" />
					</form:select>
					<form:errors class="formError" path="request.patientBloodRh" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientGender.hidden != true}">
				<div>
					<form:label path="patientGender">${requestFields.patientGender.displayName}</form:label>
					<form:select path="patientGender" id="${editRequestFormPatientGenderSelectorId}">
						<form:option value="not_known" label="Not Known" />
						<form:option value="male" label="Male" />
						<form:option value="female" label="Female" />
						<form:option value="not_applicable" label="Not Applicable" />
					</form:select>
					<form:errors class="formError" path="request.patientGender" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestDate.hidden != true }">
				<div>
					<form:label path="requestDate">${requestFields.requestDate.displayName}</form:label>
					<form:input path="requestDate" class="requestDate" />
					<form:errors class="formError" path="request.requestDate"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requiredDate.hidden != true }">
				<div>
					<form:label path="requiredDate">${requestFields.requiredDate.displayName}</form:label>
					<form:input path="requiredDate" class="requiredDate" />
					<form:errors class="formError" path="request.requiredDate"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestType.hidden != true }">
				<div>
					<form:label path="requestType">${requestFields.requestType.displayName}</form:label>
					<form:select path="requestType"
											 id="${editRequestFormRequestTypeSelectorId}"
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
											 id="${editRequestFormProductTypeSelectorId}"
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
					<form:input path="numUnitsRequested" />
					<form:errors class="formError" path="request.numUnitsRequested" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestSite.hidden != true }">
				<div>
					<form:label path="requestSite">${requestFields.requestSite.displayName}</form:label>
					<form:select path="requestSite"
						id="${editRequestFormRequestSiteSelectorId}"
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
					<form:input path="patientNumber" />
					<form:errors class="formError" path="request.patientNumber" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientFirstName.hidden != true }">
				<div>
					<form:label path="patientFirstName">${requestFields.patientFirstName.displayName}</form:label>
					<form:input path="patientFirstName" />
					<form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientLastName.hidden != true }">
				<div>
					<form:label path="patientLastName">${requestFields.patientLastName.displayName}</form:label>
					<form:input path="patientLastName" />
					<form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientBirthDate.hidden != true }">
				<div>
					<form:label path="patientBirthDate">${requestFields.patientBirthDate.displayName}</form:label>
					<form:input path="patientBirthDate" class="patientBirthDate" />
					<form:errors class="formError" path="request.patientBirthDate"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientAge.hidden != true }">
				<div>
					<form:label path="patientAge">${requestFields.patientAge.displayName}</form:label>
					<form:input path="patientAge"
										  type="number" min="0" max="120" />
						years
					<form:errors class="formError" path="request.patientAge" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.patientDiagnosis.hidden != true }">
				<div>
					<form:label path="patientDiagnosis">${requestFields.patientDiagnosis.displayName}</form:label>
					<form:input path="patientDiagnosis" />
					<form:errors class="formError" path="request.patientDiagnosis" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.ward.hidden != true }">
				<div>
					<form:label path="ward">${requestFields.ward.displayName}</form:label>
					<form:input path="ward" />
					<form:errors class="formError" path="request.ward" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.department.hidden != true }">
				<div>
					<form:label path="department">${requestFields.department.displayName}</form:label>
					<form:input path="department" />
					<form:errors class="formError" path="request.department" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.hospital.hidden != true }">
				<div>
					<form:label path="hospital">${requestFields.hospital.displayName}</form:label>
					<form:input path="hospital" />
					<form:errors class="formError" path="request.hospital" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${requestFields.requestedBy.hidden != true }">
				<div>
					<form:label path="requestedBy">${requestFields.requestedBy.displayName}</form:label>
					<form:input path="requestedBy" />
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
			<button type="button" class="saveRequestButton autoWidthButton">
				Save
			</button>
			<!-- button type="button" class="clearFormButton autoWidthButton">
				Clear form
			</button-->				
			<button type="button" class="cancelButton autoWidthButton">
				Cancel
			</button>				
		</div>
	</div>
</div>
