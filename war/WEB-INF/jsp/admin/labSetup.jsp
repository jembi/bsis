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
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>

<script>
$(document).ready(function(){

  $("#${mainContentId}").find(".updateLabSetupButton")
  											.button()
  											.click(function() {
  											  var labSetupData = {};
  											  var inputs = $("#${mainContentId}").find("input");
  											  for (var index = 0; index < inputs.length; ++index) {
  											    var input = $(inputs[index]);
  											    if (input.is(':checked'))
	  											    labSetupData[input.prop('name')] = 'true'; 
  											    else
  											      labSetupData[input.prop('name')] = 'false';
  											  }

  											 	console.log(labSetupData);

  											  $.ajax({
  											    url: "updateLabSetup.html",
  											    data: {labSetupParams : JSON.stringify(labSetupData)},
  											    type: "POST",
  											    success: function(response) {
  											      				 showMessage("Lab Setup successfully updated. Please refresh the page.");
  											    				 },
  											    error:   function(response) {
																			 showErrorMessage("Something went wrong. Please try again.");  											      
  											    				 }
  											  });
  											});
  
});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">


		<form class="formInTabPane">

			<div>
				<label>
					<b>Lab setup form</b>
				</label>
			</div>

			<div>
				<label>Record outcomes</label>
				<c:if test="${labsetup['recordOutcomes'] eq 'true'}">
					<input name="recordOutcomes" type="checkbox" checked />
				</c:if>
				<c:if test="${labsetup['recordOutcomes'] ne 'true'}">
					<input name="recordOutcomes" type="checkbox" />
				</c:if>
			</div>

			<div>
				<label>Record blood test results</label>
				<c:if test="${labsetup['recordBloodTestResults'] eq 'true'}">
					<input name="recordBloodTestResults" type="checkbox" checked/>
				</c:if>
				<c:if test="${labsetup['recordBloodTestResults'] ne 'true'}">
					<input name="recordBloodTestResults" type="checkbox" />
				</c:if>
			</div>

			<div>
				<label>Use ELISA Plates</label>
				<c:if test="${labsetup['useElisaPlates'] eq 'true'}">
					<input name="useElisaPlates" type="checkbox" checked/>
				</c:if>
				<c:if test="${labsetup['useElisaPlates'] ne 'true'}">
					<input name="useElisaPlates" type="checkbox" />
				</c:if>
			</div>

			<div>
				<label>Use Worksheets</label>
				<c:if test="${labsetup['useWorksheets'] eq 'true'}">
					<input name="useWorksheets" type="checkbox" checked/>
				</c:if>
				<c:if test="${labsetup['useWorksheets'] ne 'true'}">
					<input name="useWorksheets" type="checkbox" />
				</c:if>
			</div>

		</form>

		<button class="updateLabSetupButton">
		Update lab setup
		</button>

	</div>

</div>