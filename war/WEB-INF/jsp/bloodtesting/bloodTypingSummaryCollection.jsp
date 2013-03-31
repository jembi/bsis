<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	$("#${mainContentId}").find(".showHideButton")
  											.button({icons: {primary : 'ui-icon-plusthick'}})
  											.click(showHideToggle);

	function showHideToggle() {
		var showHideButton = $(this);
		var parentSection = showHideButton.closest(".moreTestsSection");
		var showHideSection = parentSection.find(".showHideTestsSection");
		var currentlyVisible = showHideSection.is(":visible");
		if (currentlyVisible) {
			 $("#${mainContentId}").find(".availableTestResultInput")
			   										 .each(function() {
		     			                       $(this).prop("type", "hidden");
		    		                       });
			$("#${mainContentId}").find(".availableTestResultLabel")
				                    .each(function() {
				   			                    $(this).show();
				   		                    });
			showHideButton.button("option", "label", "Show more tests");
			showHideButton.button("option", "icons", {primary : "ui-icon-plusthick"});
			showHideSection.hide();
		}
		else {
			showHideButton.button("option", "label", "Show fewer tests");
			showHideButton.button("option", "icons", {primary : "ui-icon-minusthick"});
			showHideSection.show();
		}
	}

	function unhideShowHideSection() {
		var showHideButton = $("#${mainContentId}").find(".showHideButton")
																							 .button();
		var parentSection = showHideButton.closest(".moreTestsSection");
		var showHideSection = parentSection.find(".showHideTestsSection");
		showHideButton.button("option", "label", "Show fewer tests");
		showHideButton.button("option", "icons", {primary : "ui-icon-minusthick"});
		showHideSection.show();
	}

	$("#${mainContentId}").find(".showHideButton")
	 											.each(showHideToggle);

	$("#${mainContentId}").find(".saveTestsButton")
												.button({icons: {primary : 'ui-icon-plusthick'}})
												.click(saveTests);

	$("#${mainContentId}").find(".cancelTestsButton")
												.button()
												.click(cancelTestsInput);

	function saveTests() {

	  var inputs = $("#${mainContentId}").find(".bloodTypingTestInput");
	  var saveTestsData = {};

		for (var index = 0; index < inputs.length; index++) {
	    var input = $(inputs[index]);
	    if (input.is(":hidden"))
	      continue;
	    var testId = input.data("testid");
	    var val = input.val();
	    if (val !== undefined && val.length > 0 && testId !== undefined) {
	      saveTestsData[testId] = val;
	    }
	  }

	  showLoadingImage($("#${tabContentId}"));
	  $.ajax({
	    url: "saveAdditionalBloodTypingTests.html",
	    type: "POST",
	    data: {saveTestsData : JSON.stringify(saveTestsData), collectionId: "${collectionId}"},
	    success: function () {
								 $("#${tabContentId}").trigger("testResultsUpdated");
	    				 },
	    error:   function() {
	      				 showErrorMessage("Something went wrong");
	    	 			 }
	  });
	}

	function reloadBloodTypingSummaryForCollection() {
	  $.ajax({
	    url: "${refreshUrl}",
	    type: "GET",
	    success: function(response) {
								 $("#${tabContentId}").replaceWith(response);
	    				 }
	  });
	}

	function cancelTestsInput() {
		var cancelButton = $(this);
		var parentSection = cancelButton.closest(".moreTestsSection");
		parentSection.find("input").each(function() {
									   $(this).val(this.defaultValue);
									 });
		 $("#${mainContentId}").find(".availableTestResultInput")
		   										 .each(function() {
	     			 											 $(this).prop("type", "hidden");
	    		 											 });
		 $("#${mainContentId}").find(".availableTestResultLabel")
			 										 .each(function() {
			   			 										 $(this).show();
			   		 										 });
	}


	$("#${mainContentId}").find(".availableTestEdit")
											  .click(function() {
											    			 $("#${mainContentId}").find(".availableTestResultInput")
	  															 									   .each(function() {
	  																								     			 $(this).prop("type", "text");
	  																								    		 });
	  														 $("#${mainContentId}").find(".availableTestResultLabel")
	  															 										 .each(function() {
	  															 										   			 $(this).hide();
	  															 										   		 });
	  														 unhideShowHideSection();
															 });

});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<c:set var="availableTestResults" value="${bloodTypingOutputForCollection.availableTestResults}" />
		<c:set var="pendingTests" value="${bloodTypingOutputForCollection.pendingBloodTypingTestsIds}" />

		<div class="bloodTypingForCollectionSection formInTabPane" style="margin: 0;">

			<div class="testsPerformed">
				<div class="formInTabPane" style="margin-left: 0px;">

					<div style="position: relative; height: 30px;">
						<label style="width: auto; position: absolute;">
							<b>Available test results</b>
						</label>
						<label style="width: auto; position: absolute; right: 10px;">
							<span class="link availableTestEdit">Edit</span>
						</label>
					</div>

					<c:set var="availableTestResultCount" value="${0}" />
					<c:if test="${fn:length(availableTestResults) gt 0}">
						<!-- traverse blood typing tests in order to make sure they are traversed in the order of id's -->
						<c:forEach var="bloodTypingTest" items="${allBloodTypingTests}">
							<c:set var="testCategory" value="${bloodTypingTest.value.category}" />
							<c:if test="${not empty availableTestResults[bloodTypingTest.key] and testCategory eq 'BLOODTYPING'}">
								<div>
									<label>${bloodTypingTest.value.testName}</label>
									<label class="availableTestResultLabel" style="font-size: 1.5em; vertical-align: middle; width: 80px;">${availableTestResults[bloodTypingTest.key]}</label>
									<input name="bloodTypingTest-${bloodTypingTest.key}" class="bloodTypingTestInput availableTestResultInput"
												 value="${availableTestResults[bloodTypingTest.key]}"
											   data-testid="${bloodTypingTest.value.id}"
											   data-available="true"
											   style="width: 80px;"
											   type="hidden" />
								</div>
								<c:set var="availableTestResultCount" value="${availableTestResultCount + 1}" />
							</c:if>
						</c:forEach>
					</c:if>

					<c:if test="${availableTestResultCount eq 0}">
						<div>
							<label>
								<span style="font-style: italic;">None available</span>
							</label>
						</div>
					</c:if>

				</div>
			</div>

			<div class="moreTestsSection">
				<div class="formInTabPane" style="margin-left: 0px;">
					<c:if test="${fn:length(pendingTests) gt 0}">
						<div>
							<label>
								<b>Pending tests</b>
							</label>
						</div>
						<c:forEach var="pendingTestId" items="${pendingTests}">
							<c:set var="pendingTest" value="${allBloodTypingTests[pendingTestId]}" />
							<div>
								<label>${pendingTest.testName}</label>
								<input name="pendingTest-${pendingTestId}"
											 class="bloodTypingTestInput"
											 style="width: 80px;"
											 data-testid="${pendingTestId}" />
							</div>
						</c:forEach>
					</c:if>
				</div>

				<div class="showHideTestsSection formInTabPane" style="margin-left: 0px;">
					<div>
						<label>
							<b>Other tests</b>
						</label>
					</div>
					<!-- Show other tests to the user -->
					<c:forEach var="bloodTypingTest" items="${allBloodTypingTests}">
		
						<c:set var="isPendingTest" value="${false}" />
						<c:forEach var="pendingTestId" items="${pendingTests}">
							<c:if test="${pendingTestId eq bloodTypingTest.key}">
								<c:set var="isPendingTest" value="${true}" />
							</c:if>
						</c:forEach>
		
						<c:if test="${!isPendingTest && empty availableTestResults[bloodTypingTest.key]}">
							<div>
								<label>${bloodTypingTest.value.testName}</label>
								<input name="bloodTypingTest-${bloodTypingTest.value.id}"
											 class="bloodTypingTestInput"
											 style="width: 80px;"
											 data-testid="${bloodTypingTest.value.id}" />
							</div>
						</c:if>
					</c:forEach>

					<!-- Save/Cancel button should be hidable if no pending tests -->
					<c:if test="${fn:length(pendingTests) eq 0}">
						<div>
							<button class="saveTestsButton">Save</button>
							<button class="cancelTestsButton">Cancel</button>
						</div>
					</c:if>

				</div>

				<!-- Save/Cancel button should always be displayed if there are pending tests -->
				<c:if test="${fn:length(pendingTests) gt 0}">
					<div>
						<button class="saveTestsButton">Save</button>
						<button class="cancelTestsButton">Cancel</button>
					</div>
				</c:if>

				<br />

				<div>
					<button class="showHideButton">
					</button>
				</div>

			</div>
		</div>


	</div>

</div>