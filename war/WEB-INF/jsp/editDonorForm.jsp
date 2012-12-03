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
<c:set var="editDonorFormBarcodeId">editDonorFormBarcode-${unique_page_id}</c:set>
<c:set var="editDonorFormId">editDonorForm-${unique_page_id}</c:set>
<c:set var="deleteDonorConfirmDialogId">deleteDonorConfirmDialog-${unique_page_id}</c:set>
<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>
<c:set var="bloodGroupSelectorId">bloodTypeSelector-${unique_page_id}</c:set>
<c:set var="birthDateInputId">birthDateInput-${unique_page_id}</c:set>
<c:set var="updateDonorButtonId">updateDonorButton-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParent() {
					 // let the parent know we are done
				   $("#${editDonorFormDivId}").parent().trigger("editDonorSuccess");
    		}

        $("#${updateDonorButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingDonor}" == "true")
                updateExistingDonor($("#${editDonorFormId}")[0],
                    								"${editDonorFormDivId}", notifyParent);
              else
                addNewDonor($("#${editDonorFormId}")[0], "${editDonorFormDivId}", notifyParent);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${editDonorFormId}").printArea();
        });

        $("#${editDonorFormId}").find(".clearFormButton").button({
          icons : {
            primary : 'ui-icon-grip-solid-horizontal'
          }
        }).click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${model.refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
              			 	 $("#${editDonorFormDivId}").replaceWith(response);
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

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

        $("#${birthDateInputId}").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c0",
        });

        $("#${editDonorFormBarcodeId}").barcode(
            							  "${editDonorForm.donor.donorNumber}-${editDonorForm.donor.id}",
            								"code128",
            								{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
        
        copyMirroredFields("${editDonorFormId}", JSON.parse('${model.donorFields.mirroredFields}'));
      });
</script>

<div id="${editDonorFormDivId}">
	<form:form id="${editDonorFormId}" method="POST" class="formInTabPane"
		commandName="editDonorForm">
		<c:if test="${model.existingDonor}">
			<div id="${editDonorFormBarcodeId}"></div>
		</c:if>
		<form:hidden path="id" />
		<c:if test="${model.donorFields.donorNumber.hidden != true }">
			<div>
				<form:label path="donorNumber">${model.donorFields.donorNumber.displayName}</form:label>
				<form:input path="donorNumber" value="${model.existingDonor ? '' : model.donorFields.donorNumber.defaultValue}" />
				<form:errors class="formError" path="donor.donorNumber"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.firstName.hidden != true }">
			<div>
				<form:label path="firstName">${model.donorFields.firstName.displayName}</form:label>
				<form:input path="firstName" value="${model.existingDonor ? '' : model.donorFields.firstName.defaultValue}" />
				<form:errors class="formError" path="donor.firstName" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.middleName.hidden != true }">
			<div>
				<form:label path="middleName">${model.donorFields.middleName.displayName}</form:label>
				<form:input path="middleName" value="${model.existingDonor ? '' : model.donorFields.middleName.defaultValue}" />
				<form:errors class="formError" path="donor.middleName"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.lastName.hidden != true }">
			<div>
				<form:label path="lastName">${model.donorFields.lastName.displayName}</form:label>
				<form:input path="lastName" value="${model.existingDonor ? '' : model.donorFields.lastName.defaultValue}" />
				<form:errors class="formError" path="donor.lastName" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.birthDate.hidden != true }">

			<div>
				<form:label path="birthDate">${model.donorFields.birthDate.displayName}</form:label>
				<form:input path="birthDate" id="${birthDateInputId}"
										value="${model.existingDonor ? '' : model.donorFields.birthDate.defaultValue}" />
				<form:errors class="formError" path="donor.birthDate" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.gender.hidden != true }">
			<div>
				<form:label path="gender">${model.donorFields.gender.displayName}</form:label>
				<form:select path="gender" id="${genderSelectorId}">
					<form:option value="not_known" label="Not Known" />
					<form:option value="male" label="Male" />
					<form:option value="female" label="Female" />
					<form:option value="not_applicable" label="Not Applicable" />
				</form:select>
				<form:errors class="formError" path="donor.gender" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.bloodGroup.hidden != true }">
			<div>
				<form:label path="bloodGroup">${model.donorFields.bloodGroup.displayName}</form:label>
				<form:select path="bloodGroup" id="${bloodGroupSelectorId}">
					<form:option value="Unknown" label="Unknown" />
					<form:option value="A+" label="A+" />
					<form:option value="A-" label="A-" />
					<form:option value="B+" label="B+" />
					<form:option value="B-" label="B-" />
					<form:option value="AB+" label="AB+" />
					<form:option value="AB-" label="AB-" />
					<form:option value="O+" label="O+" />
					<form:option value="O-" label="O-" />
				</form:select>
				<form:errors class="formError" path="bloodGroup" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.address.hidden != true }">
			<div>
				<form:label path="address" class="labelForTextArea">${model.donorFields.address.displayName}</form:label>
				<form:textarea path="address" value="${model.existingDonor ? '' : model.donorFields.address.defaultValue}" maxlength="255" />
				<form:errors class="formError" path="donor.address" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.city.hidden != true }">
			<div>
				<form:label path="city">${model.donorFields.city.displayName}</form:label>
				<form:input path="city" value="${model.existingDonor ? '' : model.donorFields.city.defaultValue}" />
				<form:errors class="formError" path="donor.city" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.state.hidden != true }">
			<div>
				<form:label path="state">${model.donorFields.state.displayName}</form:label>
				<form:input path="state" value="${model.existingDonor ? '' : model.donorFields.state.defaultValue}" />
				<form:errors class="formError" path="donor.state" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.country.hidden != true }">
			<div>
				<form:label path="country">${model.donorFields.country.displayName}</form:label>
				<form:input path="country" value="${model.existingDonor ? '' : model.donorFields.country.defaultValue}" />
				<form:errors class="formError" path="donor.country" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.donorFields.zipcode.hidden != true }">
			<div>
				<form:label path="zipcode">${model.donorFields.zipcode.displayName}</form:label>
				<form:input path="zipcode" value="${model.existingDonor ? '' : model.donorFields.zipcode.defaultValue}" />
				<ul>
					<form:errors class="formError" path="donor.zipcode" delimiter=", "></form:errors>
				</ul>
			</div>
		</c:if>
		<c:if test="${model.donorFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.donorFields.notes.displayName}</form:label>
				<textarea name="notes">${model.existingDonor ? '' : model.donorFields.notes.defaultValue}</textarea>
				<form:errors class="formError" path="donor.notes"></form:errors>
			</div>
		</c:if>
		<div>
			<label></label>
			<c:if test="${!(model.existingDonor)}">
				<button type="button" id="${updateDonorButtonId}" class="autoWidthButton">
					Save and add another
				</button>
				<button type="button" class="clearFormButton autoWidthButton">
					Clear form
				</button>				
			</c:if>
			<c:if test="${model.existingDonor}">
				<button type="button" id="${updateDonorButtonId}">
					Save
				</button>
			</c:if>

			<button type="button" id="${printButtonId}">
				Print
			</button>

		</div>
	</form:form>
</div>
