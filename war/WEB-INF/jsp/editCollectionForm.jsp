<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editCollectionFormId">editCollectionForm-${unique_page_id}</c:set>
<c:set var="deleteCollectionConfirmDialogId">deleteCollectionConfirmDialog-${unique_page_id}</c:set>
<c:set var="editCollectionFormCentersId">editCollectionFormCenters-${unique_page_id}</c:set>
<c:set var="editCollectionFormSitesId">editCollectionFormSites-${unique_page_id}</c:set>
<c:set var="editCollectionFormDonorTypeId">editCollectionFormDonorType-${unique_page_id}</c:set>
<c:set var="updateCollectionButtonId">updateCollectionButton-${unique_page_id}</c:set>
<c:set var="deleteCollectionButtonId">deleteCollectionButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>


<script>
  $("#${updateCollectionButtonId}").button({
    icons : {
      primary : 'ui-icon-circle-plus'
    }
  }).click(function() {
    updateExistingCollection($("#${editCollectionFormId}")[0]);
  });

  $("#${deleteCollectionButtonId}").button({
    icons : {
      primary : 'ui-icon-minusthick'
    }
  }).click(
      function() {
        $("#${deleteCollectionConfirmDialogId}").dialog(
            {
              modal : true,
              title : "Confirm Delete",
              buttons : {
                "Delete" : function() {
                  var collectionNumber = $("#${editCollectionFormId}").find(
                      "[name='collectionNumber']").val();
                  deleteCollection(collectionNumber,
                      $("#${editCollectionFormId}"));
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
  $("#${editCollectionFormDonorTypeId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

</script>

<div class="editFormDiv">
<form:form method="POST" commandName="editCollectionForm"
	id="${editCollectionFormId}">
	<table>
		<thead>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td><b>Add a New Collection</b></td>
				</tr>
			</c:if>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="collectionNumber">${model.collectionNoDisplayName}</form:label></td>
				<td><form:input path="collectionNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="donorNumber">${model.donorNoDisplayName}</form:label></td>
				<td><form:input path="donorNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="shippingNumber">${model.shippingNoDisplayName}</form:label></td>
				<td><form:input path="shippingNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="sampleNumber">${model.sampleNoDisplayName}</form:label></td>
				<td><form:input path="sampleNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="centers">${model.centerDisplayName}</form:label></td>
				<td style="padding-left: 10px;"><form:select path="centers"
						id="${editCollectionFormCentersId}"
						class="editCollectionFormCenters">
						<c:forEach var="center" items="${model.centers}">
							<form:option value="${center}" label="${center}"
								selected="${center == model.selectedCenter ? 'selected' : ''}" />
						</c:forEach>
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="sites">${model.siteDisplayName}</form:label></td>
				<td style="padding-left: 10px;"><form:select path="sites"
						id="${editCollectionFormSitesId}"
						class="editCollectionFormSites">
						<c:forEach var="site" items="${model.sites}">
							<form:option value="${site}" label="${site}"
								selected="${site == model.selectedSite ? 'selected' : ''}" />
						</c:forEach>
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="donorType">${model.donorTypeDisplayName}</form:label></td>
				<td style="padding-left: 10px;"><form:select path="donorType"
						id="${editCollectionFormDonorTypeId}"
						class="editCollectionFormDonorType">
						<form:option value="voluntary" label="Voluntary" />
						<form:option value="family" label="Family" />
						<form:option value="other" label="Other" />
					</form:select></td>
			</tr>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td />
					<td><button type="button" id="${updateCollectionButtonId}"
							style="margin-left: 10px">Save changes</button>
						<button type="button" id="${deleteCollectionButtonId}"
							style="margin-left: 10px">Delete</button>
						<button type="button" id="${goBackButtonId}"
							style="margin-left: 10px">Go Back</button></td>
				</tr>
			</c:if>

		</tbody>
	</table>
</form:form>
</div>

<div id="${deleteCollectionConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Collection?</div>