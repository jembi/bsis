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

			<c:if test="${fn:length(pendingTests) gt 0}">
				<div>
					<label>
						<b>Pending Tests</b>
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

			<div>
				<label>
					<b>Other Tests</b>
				</label>
			</div>

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
		</div>
	</c:forEach>

</div>
