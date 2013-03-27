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

	$("#${mainContentId}").find(".showHideButton")
										   	.each(showHideToggle);

	$("#${mainContentId}").find(".saveMoreTestsButton")
												.button({icons: {primary : 'ui-icon-plusthick'}})
												.click(saveTests);

	$("#${mainContentId}").find(".cancelMoreTestsButton")
												.button()
												.click(cancelTestsInput);

	function saveTests() {
	  console.log("save tests button clicked");
	}

	function cancelTestsInput() {
	  var cancelButton = $(this);
	  var parentSection = cancelButton.closest(".moreTestsSection");
	  parentSection.find("input").each(function() {
	    															   $(this).val(this.defaultValue);
	    															 });
	}

});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">
		<div class="successBox ui-state-highlight">
			<img src="images/check_icon.png"
					 style="height: 30px; padding-left: 10px; padding-right: 10px;" />
			<span class="successText">
				Blood Typing tests added successfully for collections.
				<br />
				Please review results below. Perform confirmatory tests for collections as indicated.
			</span>
		</div>
		<c:forEach var="collection" items="${collections}">
			<c:set var="bloodTypingOutputForCollection" value="${bloodTypingOutput[collection.key]}" />
			<c:set var="bloodTypingTestResultsForCollection" value="${bloodTypingOutputForCollection['testResults']}" />
			<c:set var="pendingTests" value="${bloodTypingOutputForCollection['pendingTests']}" />
			<div class="bloodTypingForCollectionSection formInTabPane">
				<div>
					<label>${collectionFields.collectionNumber.displayName}</label>
					<label>${collection.value.collectionNumber}</label>
				</div>
	
				<div>
					<label>Blood Typing Status</label>
					<label>${bloodTypingOutputForCollection['bloodTypingStatus']}</label>
				</div>
	
				<div>
					<label>Blood ABO</label>
					<label>${bloodTypingOutputForCollection['bloodAbo']}</label>
				</div>
	
				<div>
					<label>Blood Rh</label>
					<label>${bloodTypingOutputForCollection['bloodRh']}</label>
				</div>
	
	
				<div class="moreTestsSection">
					<div class="formInTabPane" style="margin-left: 0px;">
						<c:if test="${fn:length(pendingTests) gt 0}">
							<div>
								<label>
									<b>More tests required</b>
								</label>
							</div>
							<c:forEach var="pendingTestId" items="${pendingTests}">
								<c:set var="pendingTest" value="${advancedBloodTypingTests[pendingTestId]}" />
								<div>
									<label>${pendingTest.testName}</label>
									<input name="pendingTest" />
								</div>
							</c:forEach>
						</c:if>
					</div>

					<div class="showHideTestsSection formInTabPane" style="margin-left: 0px;">
						<!-- Show other tests to the user -->
						<c:forEach var="advancedTest" items="${advancedBloodTypingTests}">
			
							<c:set var="isPendingTest" value="${false}" />
							<c:forEach var="pendingTestId" items="${pendingTests}">
								<c:if test="${pendingTestId eq advancedTest.key}">
									<c:set var="isPendingTest" value="${true}" />
								</c:if>
							</c:forEach>
			
							<c:if test="${!isPendingTest}">
								<div>
									<label>${advancedTest.value.testName}</label>
									<input name="advancedTest" />
								</div>
							</c:if>
						</c:forEach>

						<!-- Save/Cancel button should be hidable if no pending tests -->
						<c:if test="${fn:length(pendingTests) eq 0}">
							<div>
								<button class="saveMoreTestsButton">Save</button>
								<button class="cancelMoreTestsButton">Cancel</button>
							</div>
						</c:if>

					</div>

					<!-- Save/Cancel button should always be displayed if there are pending tests -->
					<c:if test="${fn:length(pendingTests) gt 0}">
						<div>
							<button class="saveMoreTestsButton">Save</button>
							<button class="cancelMoreTestsButton">Cancel</button>
						</div>
					</c:if>

					<br />

					<div>
						<button class="showHideButton">
						</button>
					</div>

				</div>
	
			</div>
		</c:forEach>
	</div>

</div>
