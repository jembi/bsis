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
<c:set var="editCollectedSampleFormId">editCollectedSampleForm-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormBarcodeId">editCollectedSampleFormBarcode-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormDonorId">editCollectedSampleFormDonor-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormDonorHiddenId">editCollectedSampleFormDonorHidden-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormCentersId">editCollectedSampleFormCenters-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormSitesId">editCollectedSampleFormSites-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormBloodBagTypeId">editCollectedSampleFormBloodBagType-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormDonationTypeId">editCollectedSampleFormDonationType-${unique_page_id}</c:set>
<c:set var="updateCollectedSampleButtonId">updateCollectedSampleButton-${unique_page_id}</c:set>
<c:set var="cancelCollectedSampleButtonId">cancelCollectedSampleButton-${unique_page_id}</c:set>
<c:set var="deleteCollectedSampleButtonId">deleteCollectedSampleButton-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>

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

        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(notifyParentCancel);

        $("#${updateCollectedSampleButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingCollectedSample}" == "true")
                updateExistingCollection($("#${editCollectedSampleFormId}")[0],
                  														"${tabContentId}",
                  														notifyParentSuccess);
              else
                addNewCollection($("#${editCollectedSampleFormId}")[0],
                    									"${tabContentId}", notifyParentSuccess);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${editCollectedSampleFormId}").printArea();
        });

        $("#${editCollectedSampleFormCentersId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectedSampleFormSitesId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectedSampleFormBloodBagTypeId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectedSampleFormDonationTypeId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCollectedSampleFormId}").find(".collectedOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 1,
          dateFormat : "mm/dd/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        var collectedOnDatePicker = $("#${editCollectedSampleFormId}").find(".collectedOn");
        if ("${model.existingCollectedSample}" == "false" && collectedOnDatePicker.val() == "") {
          collectedOnDatePicker.datepicker('setDate', new Date());
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

        if ("${model.disallowDonorChange}" == "true") {
	        $("#${editCollectedSampleFormId}").find('input[name="donorNumber"]').attr("readonly", "readonly");	
        }

        function updateBarcode(val) {
          if (val === null || val === undefined || val === "")
            val = "-";
	        $("#${editCollectedSampleFormId}").find(".barcodeContainer").barcode(
						  val,
							"code128",
							{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
        }
        updateBarcode("${editCollectedSampleForm.collectedSample.collectionNumber}");

        $("#${editCollectedSampleFormId}").find('input[name="collectionNumber"]').keyup(function() {
          updateBarcode($(this).val());
        });
        
        if ("${model.existingCollectedSample}" !== "true" && "${model.hasErrors}" !== "true") {
          // just set the default values for the new collection  
        	$("#${tabContentId}").find('textarea[name="notes"]').html("${model.collectedSampleFields.notes.defaultValue}");
        	setDefaultValueForSelector(getDonationTypeSelector(), "${model.collectedSampleFields.donationType.defaultValue}");
        	setDefaultValueForSelector(getCollectionCenterSelector(), "${model.collectedSampleFields.collectionCenter.defaultValue}");
        	setDefaultValueForSelector(getBloodBagTypeSelector(), "${model.collectedSampleFields.bloodBagType.defaultValue}");
        	setDefaultValueForSelector(getCollectionSiteSelector(), "${model.collectedSampleFields.collectionSite.defaultValue}");
        }

        function getDonationTypeSelector() {
          return $("#${tabContentId}").find('select[name="donationType"]').multiselect();
        }

        function getCollectionCenterSelector() {
          return $("#${tabContentId}").find('select[name="collectionCenter"]').multiselect();
        }

        function getBloodBagTypeSelector() {
          return $("#${tabContentId}").find('select[name="bloodBagType"]').multiselect();
        }

        function getCollectionSiteSelector() {
          return $("#${tabContentId}").find('select[name="collectionSite"]').multiselect();
        }

		});
</script>

<div id="${tabContentId}">

	<form:form method="POST" commandName="editCollectedSampleForm"
		class="formInTabPane" id="${editCollectedSampleFormId}">
		<div class="barcodeContainer"></div>
		<form:hidden path="id" />
		<c:if test="${model.collectedSampleFields.collectionNumber.hidden != true }">
			<div>
				<form:label path="collectionNumber">${model.collectedSampleFields.collectionNumber.displayName}</form:label>
				<form:input path="collectionNumber" value="${model.existingCollectedSample ? '' : model.collectedSampleFields.collectionNumber.defaultValue}" />
				<form:errors class="formError"
					path="collectedSample.collectionNumber" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.donor.hidden != true }">
			<div>
				<form:label path="donorNumber">${model.collectedSampleFields.donorNumber.displayName}</form:label>
				<form:hidden path="donorIdHidden" />
				<form:input path="donorNumber" class="donorNumber" value="${model.existingCollectedSample ? '' : model.collectedSampleFields.donorNumber.defaultValue}" />
				<form:errors class="formError" path="collectedSample.donorNumber"
					delimiter=", "></form:errors>
				<form:errors class="formError" path="collectedSample.donor"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.collectedOn.hidden != true }">
			<div>
				<form:label path="collectedOn">${model.collectedSampleFields.collectedOn.displayName}</form:label>
				<form:input path="collectedOn" class="collectedOn" value="${model.existingCollectedSample ? '' : model.collectedSampleFields.collectedOn.defaultValue}" />
				<form:errors class="formError" path="collectedSample.collectedOn"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.donationType.hidden != true }">
			<div>
				<form:label path="donationType">${model.collectedSampleFields.donationType.displayName}</form:label>
				<form:select path="donationType"
					id="${editCollectedSampleFormDonationTypeId}"
					class="editCollectedSampleFormDonationType">
					<form:option value="">&nbsp;</form:option>
					<c:forEach var="donationType" items="${model.donationTypes}">
						<form:option value="${donationType.id}">${donationType.donationType}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectedSample.donationType"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.shippingNumber.hidden != true }">
			<div>
				<form:label path="shippingNumber">${model.collectedSampleFields.shippingNumber.displayName}</form:label>
				<form:input path="shippingNumber" value="${model.existingCollectedSample ? '' : model.collectedSampleFields.shippingNumber.defaultValue}" />
				<form:errors class="formError" path="collectedSample.shippingNumber"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.sampleNumber.hidden != true }">
			<div>
				<form:label path="sampleNumber">${model.collectedSampleFields.sampleNumber.displayName}</form:label>
				<form:input path="sampleNumber" value="${model.existingCollectedSample ? '' : model.collectedSampleFields.sampleNumber.defaultValue}" />
				<form:errors class="formError" path="collectedSample.sampleNumber"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.collectionCenter.hidden != true }">
			<div>
				<form:label path="collectionCenter">${model.collectedSampleFields.collectionCenter.displayName}</form:label>
				<form:select path="collectionCenter" id="${editCollectedSampleFormCentersId}" class="editCollectedSampleFormCenters">
					<form:option value="" selected="selected">&nbsp;</form:option>
					<c:forEach var="center" items="${model.centers}">
						<form:option value="${center.id}">${center.name}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectedSample.collectionCenter" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true }">
			<div>
				<form:label path="bloodBagType">${model.collectedSampleFields.bloodBagType.displayName}</form:label>
				<form:select path="bloodBagType"
					id="${editCollectedSampleFormBloodBagTypeId}">
					<form:option value="">&nbsp;</form:option>
					<c:forEach var="bloodBagType" items="${model.bloodBagTypes}">
						<form:option value="${bloodBagType.id}">${bloodBagType.bloodBagType}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectedSample.bloodBagType"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.site.hidden != true }">
			<div>
				<form:label path="collectionSite">${model.collectedSampleFields.collectionSite.displayName}</form:label>
				<form:select path="collectionSite" id="${editCollectedSampleFormSitesId}"
					class="editCollectedSampleFormSites">
					<form:option value="" selected="selected">&nbsp;</form:option>
					<c:forEach var="site" items="${model.sites}">
						<form:option value="${site.id}">${site.name}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectedSample.collectionSite" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.collectedSampleFields.notes.displayName}</form:label>
				<form:textarea path="notes" />
				<form:errors class="formError" path="collectedSample.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		</form:form>

		<div style="margin-left: 200px;">
			<c:if test="${!(model.existingCollectedSample)}">
				<button type="button" id="${updateCollectedSampleButtonId}" class="autoWidthButton">
					Save
				</button>
				<c:if test="${model.collectionForDonor}">
					<button type="button" class="cancelButton">
						Cancel
					</button>
				</c:if>
				<button type="button" class="clearFormButton autoWidthButton">
					Clear form
				</button>				
			</c:if>
			<c:if test="${model.existingCollectedSample}">
				<button type="button" id="${updateCollectedSampleButtonId}">
					Save
				</button>
				<button type="button" class="cancelButton">
					Cancel
				</button>
			</c:if>
			<button type="button" id="${printButtonId}">
				Print
			</button>
		</div>

</div>
