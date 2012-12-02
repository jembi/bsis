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
<c:set var="editCollectedSampleFormDonorHiddenId">editCollectedSampleFormDonorHidden-${unique_page_id}</c:set>
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

        function notifyParent() {
						// let the parent know we are done
						$("#${editCollectedSampleFormDivId}").parent().trigger("editCollectionSuccess");
				}

        $("#${updateCollectedSampleButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingCollectedSample}" == "true")
                updateExistingCollectedSample($("#${editCollectedSampleFormId}")[0],
                  														"${editCollectedSampleFormDivId}",
                  														notifyParent);
              else
                addNewCollectedSample($("#${editCollectedSampleFormId}")[0],
                    									"${editCollectedSampleFormDivId}", notifyParent);
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

        $("#${editCollectedSampleFormDonorId}").click(function() {
          $("#${editCollectedSampleFormDonorId}").removeAttr("readonly");
        });

        $("#${editCollectedSampleFormDonorId}").autocomplete(
            {
              minLength : 3,
              source : function(request, response) {
                $.ajax({
                  url : "donorTypeAhead.html",
                  method : "GET",
                  data : {
                    term : request.term
                  },
                  success : function(jsonResponse) {
                    var suggestions = [];
                    if (jsonResponse.length == 0) {
                      suggestions.push({
                        label : "No Matching Results",
                        id : null
                      });
                    }
                    for ( var index in jsonResponse) {
                      var donor = jsonResponse[index];
                      suggestions.push({
                        label : getLabelForDonor({firstName: donor.firstName,
                          												lastName: donor.lastName,
                          												donorNumber: donor.donorNumber,
                          											 }),
                        id : donor.id
                      });
                    }
                    response(suggestions);
                  }
                });
              },
              select : function(event, ui) {
                var item = ui.item;
                $("#${editCollectedSampleFormDonorId}").val(item.label);
                $("#${editCollectedSampleFormDonorHiddenId}").val(item.id);
                $("#${editCollectedSampleFormDonorId}").attr("readonly",
                    "readonly");
              },
            });

        if ("${model.editCollectedSampleForm.donor}" != "") {
	        $("#${editCollectedSampleFormDonorId}").val(
						getLabelForDonor({ donorNumber: "${model.editCollectedSampleForm.donor.donorNumber}",
						  								 firstName: "${model.editCollectedSampleForm.donor.firstName}",
						  								 lastName: "${model.editCollectedSampleForm.donor.lastName}",
														 }));
	        $("#${editCollectedSampleFormDonorHiddenId}").val("${model.editCollectedSampleForm.donor.id}");
	        $("#${editCollectedSampleFormDonorId}").attr("readonly", "readonly");	
        }

        copyMirroredFields("${editCollectedSampleFormId}", JSON.parse('${model.collectedSampleFields.mirroredFields}'));

      });
</script>

<div id="${editCollectedSampleFormDivId}">

	<form:form method="POST" commandName="editCollectedSampleForm"
		class="formInTabPane" id="${editCollectedSampleFormId}">
		<form:hidden path="id" />
		<c:if test="${model.collectedSampleFields.collectionNumber.hidden != true }">
			<div>
				<form:label path="collectionNumber">${model.collectedSampleFields.collectionNumber.displayName}</form:label>
				<form:input path="collectionNumber" />
				<form:errors class="formError"
					path="collectedSample.collectionNumber" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.donor.hidden != true }">
			<div>
				<form:label path="donor">${model.collectedSampleFields.donor.displayName}</form:label>
				<form:hidden path="donorIdHidden"
					id="${editCollectedSampleFormDonorHiddenId}" />
				<input id="${editCollectedSampleFormDonorId}" />
				<form:errors class="formError" path="collectedSample.donor"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.donorType.hidden != true }">
			<div>
				<form:label path="donorType">${model.collectedSampleFields.donorType.displayName}</form:label>
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
		</c:if>
		<c:if test="${model.collectedSampleFields.shippingNumber.hidden != true }">
			<div>
				<form:label path="shippingNumber">${model.collectedSampleFields.shippingNumber.displayName}</form:label>
				<form:input path="shippingNumber" />
				<form:errors class="formError" path="collectedSample.shippingNumber"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.sampleNumber.hidden != true }">
			<div>
				<form:label path="sampleNumber">${model.collectedSampleFields.sampleNumber.displayName}</form:label>
				<form:input path="sampleNumber" />
				<form:errors class="formError" path="collectedSample.sampleNumber"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.center.hidden != true }">
			<div>
				<form:label path="center">${model.collectedSampleFields.center.displayName}</form:label>
				<form:select path="center" id="${editCollectedSampleFormCentersId}"
					class="editCollectedSampleFormCenters">
					<form:option label="" value="" selected="selected" />
					<c:forEach var="center" items="${model.centers}">
						<form:option value="${center.id}" label="${center.name}" />
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectedSample.center" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true }">
			<div>
				<form:label path="bloodBagType">${model.collectedSampleFields.bloodBagType.displayName}</form:label>
				<form:select path="bloodBagType"
					id="${editCollectedSampleFormBloodBagTypeId}">
					<form:option label="" value="" />
					<c:forEach var="bloodBagType" items="${model.bloodBagTypes}">
						<form:option value="${bloodBagType}" label="${bloodBagType}" />
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectedSample.bloodBagType"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.site.hidden != true }">
			<div>
				<form:label path="site">${model.collectedSampleFields.site.displayName}</form:label>
				<form:select path="site" id="${editCollectedSampleFormSitesId}"
					class="editCollectedSampleFormSites">
					<form:option label="" value="" selected="selected" />
					<c:forEach var="site" items="${model.sites}">
						<form:option value="${site.id}" label="${site.name}" />
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectedSample.site" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.collectedSampleFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.collectedSampleFields.notes.displayName}</form:label>
				<form:textarea path="notes" maxlength="255" />
				<form:errors class="formError" path="collectedSample.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<div>
			<label></label>
			<button type="button" id="${updateCollectedSampleButtonId}"
				style="margin-left: 10px">Save</button>
			<button type="button" id="${deleteCollectedSampleButtonId}"
				style="margin-left: 10px">Delete</button>
		</div>
	</form:form>
</div>

<div id="${deleteCollectedSampleConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Collection?</div>