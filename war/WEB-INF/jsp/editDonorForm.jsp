<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editDonorFormId">editDonorForm-${unique_page_id}</c:set>
<c:set var="deleteDonorConfirmDialogId">deleteDonorConfirmDialog-${unique_page_id}</c:set>
<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>
<c:set var="bloodTypeSelectorId">bloodTypeSelector-${unique_page_id}</c:set>
<c:set var="updateDonorButtonId">updateDonorButton-${unique_page_id}</c:set>
<c:set var="deleteDonorButtonId">deleteDonorButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>

<script>
  $("#${updateDonorButtonId}").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    updateExistingDonor($("#${editDonorFormId}")[0]);
  });

  $("#${deleteDonorButtonId}").button({
    icons : {
      primary : 'ui-icon-minusthick'
    }
  }).click(
      function() {
        $("#${deleteDonorConfirmDialogId}").dialog(
            {
              modal : true,
              title : "Confirm Delete",
              buttons : {
                "Delete" : function() {
                  var donorNumber = $("#${editDonorFormId}").find("[name='donorNumber']").val();
                  deleteDonor(donorNumber, $("#${editDonorFormId}"));
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
  
  $("#${genderSelectorId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${bloodTypeSelectorId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });
</script>

<form:form id="${editDonorFormId}" method="POST"
	commandName="editDonorForm">
	<table>
		<thead>
			<tr>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="donorNumber">${model.donorIDDisplayName}</form:label></td>
				<td><form:input path="donorNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="firstName">${model.firstNameDisplayName}</form:label></td>
				<td><form:input path="firstName" /></td>
			</tr>
			<tr>
				<td><form:label path="lastName">${model.lastNameDisplayName}</form:label></td>
				<td><form:input path="lastName" /></td>
			</tr>
			<tr>
				<td><form:label path="birthDate">${model.dobDisplayName}</form:label></td>
				<td><form:input path="birthDate" id="updateDonorBirthDate" /></td>
			</tr>
			<tr>
				<td><form:label path="gender">${model.genderDisplayName}</form:label></td>
				<td><form:select path="gender" id="${genderSelectorId}">
						<form:option value="male" label="Male" />
						<form:option value="female" label="Female" />
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="bloodType">${model.bloodTypeDisplayName}</form:label></td>
				<td><form:select path="bloodType" id="${bloodTypeSelectorId}">
						<form:option value="A+" label="A+" />
						<form:option value="A-" label="A-" />
						<form:option value="B+" label="B+" />
						<form:option value="B-" label="B-" />
						<form:option value="AB+" label="AB+" />
						<form:option value="AB-" label="AB-" />
						<form:option value="O+" label="O+" />
						<form:option value="O-" label="O-" />
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="address">${model.addressDisplayName}</form:label></td>
				<td><form:textarea path="address" id="donorAddressInputBox"
						maxlength="255" /></td>
			</tr>
			<tr>
				<td><form:label path="comments">${model.commentsDisplayName}</form:label></td>
				<td><form:textarea path="comments" id="donorCommentsInputBox"
						maxlength="255" /></td>
			</tr>
			<tr>
				<td />
				<td><button type="button" id="${updateDonorButtonId}"
						style="margin-left: 10px">Save changes</button>
					<button type="button" id="${deleteDonorButtonId}"
						style="margin-left: 10px">Delete</button>
					<button type="button" id="${goBackButtonId}"
						style="margin-left: 10px">Go Back</button></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="${deleteDonorConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Donor?</div>