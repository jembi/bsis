<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}
	Long formId = getCurrentTime();%>
<c:set var="formId"><%=formId%></c:set>


<script>
  $(".addCollectionButton").button();
  $(".editCollectionFormCenters").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });
  $(".editCollectionFormSites").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });
  $(".editCollectionFormDonorType").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  }
);
  function updateCollection() {
    addNewCollection($("#editCollectionForm-" + "<%=formId%>")[0]);
    $("#editCollectionForm-" + "<%=formId%>")[0].reset();
  }
</script>


<form:form method="POST" commandName="editCollectionForm"
	id="editCollectionForm-${formId}">
	<table>
		<thead>
			<tr>
			</tr>
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
				<td><form:select path="centers"
						class="editCollectionFormCenters">
						<c:forEach var="center" items="${model.centers}">
							<form:option value="${center}" label="${center}"
								selected="${center == model.selectedCenter ? 'selected' : ''}" />
						</c:forEach>
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="sites">${model.siteDisplayName}</form:label></td>
				<td><form:select path="sites" class="editCollectionFormSites">
						<c:forEach var="site" items="${model.sites}">
							<form:option value="${site}" label="${site}"
								selected="${site == model.selectedSite ? 'selected' : ''}" />
						</c:forEach>
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="donorType">${model.donorTypeDisplayName}</form:label></td>
				<td><form:select path="donorType"
						class="editCollectionFormDonorType">
						<form:option value="voluntary" label="Voluntary" />
						<form:option value="family" label="Family" />
						<form:option value="other" label="Other" />
					</form:select></td>
			</tr>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td />
					<td><input type="button" value="Add Collection"
						class="addCollectionButton" onclick="updateCollection();" /></td>
				</tr>
			</c:if>

		</tbody>
	</table>
</form:form>
