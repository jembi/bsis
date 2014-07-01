<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="editCollectionFormId">editCollectionForm-${unique_page_id}</c:set>
<c:set var="editCollectionFormBarcodeId">editCollectionFormBarcode-${unique_page_id}</c:set>
<c:set var="editCollectionFormDonorId">editCollectionFormDonor-${unique_page_id}</c:set>
<c:set var="editCollectionFormDonorHiddenId">editCollectionFormDonorHidden-${unique_page_id}</c:set>
<c:set var="editCollectionFormCentersId">editCollectionFormCenters-${unique_page_id}</c:set>
<c:set var="editCollectionFormSitesId">editCollectionFormSites-${unique_page_id}</c:set>
<c:set var="editCollectionFormBloodBagTypeId">editCollectionFormBloodBagType-${unique_page_id}</c:set>
<c:set var="editCollectionFormDonationTypeId">editCollectionFormDonationType-${unique_page_id}</c:set>
<c:set var="editCollectionFormUseBatchCheckboxId">editCollectionFormUseBatchCheckbox-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
            // let the parent know we are done
            $("#${tabContentId}").parent().trigger("editCollectionSuccess");
        }
  
        function notifyParentCancel() {
          $("#${tabContentId}").parent().trigger("editCollectionCancel");
        }

        $("#${mainContentId}").find(".saveCollectionButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
                updateExistingCollection($("#${editCollectionFormId}")[0],
                                      "${tabContentId}", notifyParentSuccess);
            });

        $("#${editCollectionFormCentersId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectionFormSitesId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectionFormBloodBagTypeId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectionFormDonationTypeId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectionFormId}").find(".collectedOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 1,
          dateFormat : "dd/mm/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

//        var collectedOnDatePicker = $("#${editCollectionFormId}").find(".collectedOn");
//        if ("${existingCollectedSample}" == "false" && collectedOnDatePicker.val() == "") {
//          collectedOnDatePicker.datepicker('setDate', new Date());
//        }

        $("#${mainContentId}").find(".clearFormButton")
                              .button()
                              .click(refetchForm);

        $("#${mainContentId}").find(".cancelButton")
                              .button({icons: {primary: 'ui-icon-closethick'}})
                              .click(notifyParentCancel);

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

        if ("${disallowDonorChange}" == "true") {
          $("#${editCollectionFormId}").find('input[name="donorNumber"]').attr("readonly", "readonly");  
        }

        function updateBarcode(val) {
          if (val === null || val === undefined || val === "")
            val = "-";
          $("#${editCollectionFormId}").find(".barcodeContainer").barcode(
              val,
              "code128",
              {barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
        }
        updateBarcode("${editCollectionForm.collectedSample.donationNumber}");

        $("#${editCollectionFormId}").find('input[name="donationNumber"]').keyup(function() {
          updateBarcode($(this).val());
        });
        
        function getDonationTypeSelector() {
          return $("#${tabContentId}").find('select[name="donationType"]').multiselect();
        }

        function getCollectionCenterSelector() {
          return $("#${tabContentId}").find('select[name="donationCenter"]').multiselect();
        }

        function getBloodBagTypeSelector() {
          return $("#${tabContentId}").find('select[name="bloodBagType"]').multiselect();
        }

        function getCollectionSiteSelector() {
          return $("#${tabContentId}").find('select[name="donationSite"]').multiselect();
        }

        function toggleCheckboxDisabledState() {
          var isChecked = $("#${editCollectionFormUseBatchCheckboxId}").is(":checked");
          if (isChecked) {
            $("#${editCollectionFormCentersId}").closest("div").hide();
            $("#${editCollectionFormSitesId}").closest("div").hide();
          } else {
            $("#${editCollectionFormCentersId}").closest("div").show();
            $("#${editCollectionFormSitesId}").closest("div").show();
          }
        }

         toggleCheckboxDisabledState();

         $("#${editCollectionFormUseBatchCheckboxId}").change(toggleCheckboxDisabledState);

    });
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).EDIT_DONATION)">
<div id="${tabContentId}">

  <div id="${mainContentId}">
    <c:if test="${!empty success && !success}">
      <jsp:include page="../common/errorBox.jsp">
        <jsp:param name="errorMessage" value="${errorMessage}" />
      </jsp:include>
    </c:if>
  
    <form:form method="POST" commandName="editCollectionForm"
      class="formFormatClass" id="${editCollectionFormId}">
      <form:hidden path="id" />
      <c:if test="${donationFields.donationNumber.hidden != true }">
          <div class="barcodeContainer"></div>
          <div>
            <form:label path="donationNumber">${donationFields.donationNumber.displayName}</form:label>
            <form:input path="donationNumber" />
            <form:errors class="formError"
              path="collectedSample.donationNumber" delimiter=", "></form:errors>
          </div>
      </c:if>
      <c:if test="${donationFields.donationBatchNumber.hidden != true }">
        <div>
          <form:label path="donationBatchNumber">${donationFields.donationBatchNumber.displayName}</form:label>
          <form:input path="donationBatchNumber" />
          <form:errors class="formError"
            path="collectedSample.donationBatch" delimiter=", "></form:errors>
          <form:errors class="formError"
            path="collectedSample.donationBatchNumber" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donationFields.donor.hidden != true }">
        <div>
          <form:label path="donorNumber">${donationFields.donorNumber.displayName}</form:label>
          <form:hidden path="donorIdHidden" />
          <form:input path="donorNumber" class="donorNumber" />
          <form:errors class="formError" path="collectedSample.donorNumber"
            delimiter=", "></form:errors>
          <form:errors class="formError" path="collectedSample.donor"
            delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donationFields.collectedOn.hidden != true }">
        <div>
          <form:label path="collectedOn">${donationFields.collectedOn.displayName}</form:label>
          <form:input path="collectedOn" class="collectedOn" />
          <form:errors class="formError" path="collectedSample.collectedOn"
            delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donationFields.donationType.hidden != true }">
        <div>
          <form:label path="donationType">${donationFields.donationType.displayName}</form:label>
          <form:select path="donationType"
            id="${editCollectionFormDonationTypeId}"
            class="editCollectionFormDonationType">
            <form:option value="">&nbsp;</form:option>
            <c:forEach var="donationType" items="${donationTypes}">
              <form:option value="${donationType.id}">${donationType.donationType}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="collectedSample.donationType"
            delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donationFields.bloodBagType.hidden != true }">
        <div>
          <form:label path="bloodBagType">${donationFields.bloodBagType.displayName}</form:label>
          <form:select path="bloodBagType"
            id="${editCollectionFormBloodBagTypeId}">
            <form:option value="">&nbsp;</form:option>
            <c:forEach var="bloodBagType" items="${bloodBagTypes}">
              <form:option value="${bloodBagType.id}">${bloodBagType.bloodBagType}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="collectedSample.bloodBagType"
            delimiter=", "></form:errors>
        </div>
      </c:if>
  
      <c:if test="${donationFields.useParametersFromBatch.hidden != true }">
  
        <div>
  
          <c:if test="${editCollectionForm.useParametersFromBatch == true}">
            <form:checkbox id="${editCollectionFormUseBatchCheckboxId}" path="useParametersFromBatch" style="width:30px;" checked="checked"/>
          </c:if>
          <c:if test="${editCollectionForm.useParametersFromBatch != true}">
            <form:checkbox id="${editCollectionFormUseBatchCheckboxId}" path="useParametersFromBatch" style="width:30px;" />
          </c:if>

          <form:label path="useParametersFromBatch" for="${editCollectionFormUseBatchCheckboxId}" style="width: auto;">
            ${donationFields.useParametersFromBatch.displayName}
          </form:label>
  
          <form:errors class="formError"
            path="useParametersFromBatch" delimiter=", "></form:errors>
        </div>
  
      </c:if>
  
      <c:if test="${donationFields.donationCenter.hidden != true }">
        <div>
          <form:label path="donationCenter">${donationFields.donationCenter.displayName}</form:label>
          <form:select path="donationCenter" id="${editCollectionFormCentersId}" class="editCollectionFormCenters">
            <form:option value="" selected="selected">&nbsp;</form:option>
            <c:forEach var="center" items="${centers}">
              <form:option value="${center.id}">${center.name}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="collectedSample.donationCenter" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donationFields.donationSite.hidden != true }">
        <div>
          <form:label path="donationSite">${donationFields.donationSite.displayName}</form:label>
          <form:select path="donationSite" id="${editCollectionFormSitesId}"
            class="editCollectionFormSites">
            <form:option value="" selected="selected">&nbsp;</form:option>
            <c:forEach var="site" items="${sites}">
              <form:option value="${site.id}">${site.name}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="collectedSample.donationSite" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donationFields.donorWeight.hidden != true }">
        <div>
          <form:label path="donorWeight">${donationFields.donorWeight.displayName}</form:label>
          <form:input path="donorWeight" />
          <form:errors class="formError" path="collectedSample.donorWeight" delimiter=", "></form:errors>
       </div>
      </c:if>

      <c:if test="${donationFields.donorPulse.hidden != true }">
        <div>
          <form:label path="donorPulse">${donationFields.donorPulse.displayName}</form:label>
          <form:input path="donorPulse"/>
          <form:errors class="formError" path="collectedSample.donorPulse" delimiter=", "></form:errors>
       </div>
      </c:if>
      <c:if test="${donationFields.haemoglobinCount.hidden != true }">
        <div>
          <form:label path="haemoglobinCount">${donationFields.haemoglobinCount.displayName}</form:label>
          <form:input path="haemoglobinCount" />
          <form:errors class="formError" path="collectedSample.haemoglobinCount" delimiter=", "></form:errors>
       </div>
      </c:if>
      <c:if test="${donationFields.bloodPressureSystolic.hidden != true }">
        <div>
          <form:label path="bloodPressureSystolic">${donationFields.bloodPressureSystolic.displayName}</form:label>
          <form:input path="bloodPressureSystolic" />
          <form:errors class="formError" path="collectedSample.bloodPressureSystolic" delimiter=", "></form:errors>
        </div>
      </c:if>
      
      <c:if test="${donationFields.bloodPressureDiastolic.hidden != true }">
        <div>
          <form:label path="bloodPressureDiastolic">${donationFields.bloodPressureDiastolic.displayName}</form:label>
          <form:input path="bloodPressureDiastolic" />
          <form:errors class="formError" path="collectedSample.bloodPressureDiastolic" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donationFields.notes.hidden != true }">
        <div>
          <form:label path="notes" class="labelForTextArea">${donationFields.notes.displayName}</form:label>
          <form:textarea path="notes" />
          <form:errors class="formError" path="collectedSample.notes"
            delimiter=", "></form:errors>
        </div>
      </c:if>
      </form:form>
  
      <div style="margin-left: 200px;">
        <label></label>
        <button type="button" class="saveCollectionButton autoWidthButton">
          Save donation
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
</sec:authorize>
