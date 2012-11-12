<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editDonorFormDivId">editDonorFormDiv-${unique_page_id}</c:set>
<c:set var="editDonorFormId">editDonorForm-${unique_page_id}</c:set>
<c:set var="deleteDonorConfirmDialogId">deleteDonorConfirmDialog-${unique_page_id}</c:set>
<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>
<c:set var="bloodGroupSelectorId">bloodTypeSelector-${unique_page_id}</c:set>
<c:set var="updateDonorButtonId">updateDonorButton-${unique_page_id}</c:set>
<c:set var="deleteDonorButtonId">deleteDonorButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>
<c:set var="addCollectionButtonId">addCollectionButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {
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
                        var donorNumber = $("#${editDonorFormId}").find(
                            "[name='donorNumber']").val();
                        deleteDonor(donorNumber);
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

        $("#${addCollectionButtonId}").button({
          icons : {
            primary : 'ui-icon-disk'
          }
        })
            .click(
                function() {
                  var parentDivId = $("#${editDonorFormDivId}").parent().attr(
                      "id");
                  replaceContent(parentDivId, "${model.requestUrl}",
                      "editCollectionFormGenerator.html", {
                        donorNumber : "${model.donorNumber}"
                      });
                  return false;
                });

        $("#${genderSelectorId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${bloodGroupSelectorId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editDonorFormId}").find(".birthdateinput").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c0",
        });

      });
</script>

<div id="${editDonorFormDivId}" class="editFormDiv">
	<form:form id="${editDonorFormId}" method="POST" class="editForm"
		commandName="editDonorForm">
		<div>
				<form:label path="donorNumber">${model.donorIDDisplayName}</form:label>
				<form:input path="donorNumber" />
		</div>
		<div>
			<form:label path="firstName">${model.firstNameDisplayName}</form:label>
			<form:input path="firstName" />
			<form:label path="lastName">${model.lastNameDisplayName}</form:label>
			<form:input path="lastName" />
		</div>
		<div>
			<form:label path="birthDate">${model.dobDisplayName}</form:label>
			<form:input path="birthDate" class="birthdateinput" />
		</div>
		<div>
			<form:label path="gender">${model.genderDisplayName}</form:label>
			<form:select path="gender" id="${genderSelectorId}">
				<form:option value="male" label="Male" />
				<form:option value="female" label="Female" />
				<form:option value="not_known" label="Not Known" />
				<form:option value="not_applicable" label="Not Applicable" />
			</form:select>

			<form:label path="bloodGroup">Blood Group</form:label>
			<form:select path="bloodGroup" id="${bloodGroupSelectorId}">
				<form:option value="A+" label="A+" />
				<form:option value="A-" label="A-" />
				<form:option value="B+" label="B+" />
				<form:option value="B-" label="B-" />
				<form:option value="AB+" label="AB+" />
				<form:option value="AB-" label="AB-" />
				<form:option value="O+" label="O+" />
				<form:option value="O-" label="O-" />
			</form:select>
		</div>
		<div>
			<form:label path="address">Address</form:label>
			<form:textarea path="address"
				maxlength="255" />
		</div>
		<div>
			<form:label path="city">City</form:label>
			<form:input path="city" />
			<form:label path="state">State</form:label>
			<form:input path="state" />
		</div>
		<div>
			<form:label path="country">Country</form:label>
			<form:input path="country" />
			<form:label path="zipcode">Zip Code</form:label>
			<form:input path="zipcode" />
		</div>
		<div>
			<form:label path="notes">Notes</form:label>
			<form:textarea path="notes"
				maxlength="255" />
		</div>
		<div>
			<button type="button" id="${updateDonorButtonId}"
				style="margin-left: 10px">Save</button>
			<button type="button" id="${deleteDonorButtonId}"
				style="margin-left: 10px">Delete</button>
			<button type="button" id="${goBackButtonId}"
				style="margin-left: 10px">Go Back</button>
			<c:if test="${model.existingDonor == 'true'}">
				<button type="button" id="${addCollectionButtonId}"
					style="margin-left: 10px">Add collection for this donor</button>
			</c:if>

		</div>
</form:form>
</div>

<div id="${deleteDonorConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Donor?</div>
