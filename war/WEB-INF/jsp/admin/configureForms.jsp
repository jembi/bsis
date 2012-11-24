<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editConfigureFormDivId">editConfigureFormDiv-${unique_page_id}</c:set>
<c:set var="editConfigureFormId">editConfigureForm-${unique_page_id}</c:set>
<c:set var="deleteConfigureConfirmDialogId">deleteConfigureConfirmDialog-${unique_page_id}</c:set>
<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>
<c:set var="bloodGroupSelectorId">bloodTypeSelector-${unique_page_id}</c:set>
<c:set var="birthDateInputId">birthDateInput-${unique_page_id}</c:set>
<c:set var="updateConfigureButtonId">updateConfigureButton-${unique_page_id}</c:set>
<c:set var="deleteConfigureButtonId">deleteConfigureButton-${unique_page_id}</c:set>
<c:set var="addCollectionButtonId">addCollectionButton-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      $(".updateFormFieldButton").button().click(function() {
        console.log("Update Form Field Clicked");
      });
    });
</script>

<c:if test="${model.hasErrors}">
	<script>
     showErrorMessage("${model.message}");
	</script>
</c:if>
<c:if test="${model.success == true}">
	<script>
     showMessage("${model.message}");
	</script>
</c:if>
<c:if test="${model.success == false}">
	<script>
     showErrorMessage("${model.message}");
	</script>
</c:if>

<div id="${editConfigureFormDivId}" class="editFormDiv">
	<c:forEach var="formField" items="${model.formFields}">
		<div>
			<label>${formField.form}.${formField.field} Hide Field</label>
			<input name="isHidden" value="${formField.isHidden}" />
			<button type="button" class="updateFormFieldButton"
				style="margin-left: 10px">Update</button>
		</div>
		<div>
			<label>${formField.form}.${formField.field} Display Name</label>
			<input name="displayName" value="${formField.displayName}" />
			<button type="button" class="updateFormFieldButton"
				style="margin-left: 10px">Update</button>
		</div>
	</c:forEach>
</div>
