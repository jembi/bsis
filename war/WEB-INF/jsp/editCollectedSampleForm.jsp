<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editCollectedSampleFormDivId">editCollectedSampleFormDiv-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormId">editCollectedSampleForm-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormDonorId">editCollectedSampleFormDonor-${unique_page_id}</c:set>
<c:set var="deleteCollectedSampleConfirmDialogId">deleteCollectedSampleConfirmDialog-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormCentersId">editCollectedSampleFormCenters-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormSitesId">editCollectedSampleFormSites-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormBloodBagTypeId">editCollectedSampleFormBloodBagType-${unique_page_id}</c:set>
<c:set var="editCollectedSampleFormDonorTypeId">editCollectedSampleFormDonorType-${unique_page_id}</c:set>
<c:set var="updateCollectedSampleButtonId">updateCollectedSampleButton-${unique_page_id}</c:set>
<c:set var="deleteCollectedSampleButtonId">deleteCollectedSampleButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        $("#${updateCollectedSampleButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingCollectedSample}" == "true")
                updateExistingCollectedSample(
                    $("#${editCollectedSampleFormId}")[0],
                    "${editCollectedSampleFormDivId}");
              else
                addNewCollectedSample($("#${editCollectedSampleFormId}")[0],
                    "${editCollectedSampleFormDivId}");
            });

        $("#${deleteCollectedSampleButtonId}").button({
          icons : {
            primary : 'ui-icon-minusthick'
          }
        }).click(
            function() {
              $("#${deleteCollectedSampleConfirmDialogId}").dialog(
                  {
                    modal : true,
                    title : "Confirm Delete",
                    buttons : {
                      "Delete" : function() {
                        var collectedSampleId = $(
                            "#${editCollectedSampleFormId}")
                            .find("[name='id']").val();
                        deleteCollectedSample(collectedSampleId,
                            $("#${editCollectedSampleFormId}"));
                        $(this).dialog("close");
                      },
                      "Cancel" : function() {
                        $(this).dialog("close");
                      }
                    }
                  });
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

        $("#${editCollectedSampleFormDonorTypeId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $( "#${editCollectedSampleFormDonorId}" ).autocomplete({
          source: "donorTypeAhead.html",
          minLength: 2,
          response: function(event, ui) {
            					console.log(event);
            					console.log(ui);
          },
          select: function( event, ui ) {
            				console.log(event);
            				console.log(ui);
          },
      	});
      });
</script>

<div id="${editCollectedSampleFormDivId}" class="editFormDiv">
	<form:form method="POST" commandName="editCollectedSampleForm"
		class="editForm" id="${editCollectedSampleFormId}">
		<form:hidden path="id" />
		<div>
			<form:label path="collectionNumber">Collection Number</form:label>
			<form:input path="collectionNumber" />
			<form:errors class="formError"
				path="collectedSample.collectionNumber" delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="donor">${model.donorNoDisplayName}</form:label>
			<form:input path="donor" id="${editCollectedSampleFormDonorId}" />
			<form:errors class="formError"
				path="collectedSample.donor" delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="donorType">${model.donorTypeDisplayName}</form:label>
			<form:select path="donorType"
				id="${editCollectedSampleFormDonorTypeId}"
				class="editCollectedSampleFormDonorType">
				<form:option label="" value="" selected="selected" />
				<c:forEach var="donorType" items="${model.donorTypes}">
					<form:option value="${donorType}" label="${donorType}" />
				</c:forEach>
			</form:select>
			<form:errors class="formError" path="collectedSample.donorType"
				delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="shippingNumber">${model.shippingNoDisplayName}</form:label>
			<form:input path="shippingNumber" />
			<form:errors class="formError" path="collectedSample.shippingNumber"
				delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="sampleNumber">${model.sampleNoDisplayName}</form:label>
			<form:input path="sampleNumber" />
			<form:errors class="formError" path="collectedSample.sampleNumber"
				delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="centers">${model.centerDisplayName}</form:label>
			<form:select path="centers" id="${editCollectedSampleFormCentersId}"
				class="editCollectedSampleFormCenters">
				<form:option label="" value="" selected="selected" />
				<c:forEach var="center" items="${model.centers}">
					<form:option value="${center}" label="${center}" />
				</c:forEach>
			</form:select>
			<form:errors class="formError" path="centers" delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="bloodBagType">Blood Bag Type</form:label>
			<form:select path="bloodBagType" id="${editCollectedSampleFormBloodBagTypeId}">
				<form:option label="" value="" selected="selected" />
				<c:forEach var="bloodBagType" items="${model.bloodBagTypes}">
					<form:option value="${bloodBagType}" label="${bloodBagType}" />
				</c:forEach>
			</form:select>
			<form:errors class="formError" path="collectedSample.bloodBagType" delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="sites">${model.siteDisplayName}</form:label>
			<form:select path="sites" id="${editCollectedSampleFormSitesId}"
				class="editCollectedSampleFormSites">
				<form:option label="" value="" selected="selected" />
				<c:forEach var="site" items="${model.sites}">
					<form:option value="${site}" label="${site}" />
				</c:forEach>
			</form:select>
			<form:errors class="formError" path="sites" delimiter=", "></form:errors>
		</div>
		<div>
			<form:label path="notes">Notes</form:label>
			<form:textarea path="notes" maxlength="255" />
			<form:errors class="formError" path="collectedSample.notes"
				delimiter=", "></form:errors>
		</div>
		<c:if test="${model.isDialog != 'yes' }">
			<div>
				<button type="button" id="${updateCollectedSampleButtonId}"
					style="margin-left: 10px">Save</button>
				<button type="button" id="${deleteCollectedSampleButtonId}"
					style="margin-left: 10px">Delete</button>
			</div>
		</c:if>
	</form:form>
</div>

<div id="${deleteCollectedSampleConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Collection?</div>