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
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".addTTIButton")
  											.button({icons: {primary: 'ui-icon-plusthick'}})
  											.click(addTTITests);

  $("#${mainContentId}").find(".clearFormButton")
  											.button()
  											.click(refetchForm);

  function addTTITests() {
  }

  function refetchForm() {
    showLoadingImage($("#${tabContentId}"));
    $("#${tabContentId}").load("${refreshUrl}");
  }

});
</script>

<div id="${tabContentId}">

	<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
	</c:if>

	<div id="${mainContentId}">
		<form class="addTTIFrom formInTabPane">
			<div>
				<label>${ttiFormFields.collectionNumber.displayName}</label>
				<input type="text" placeholder="Collection Number" value="${firstTimeRender ? '' : collectionNumber}"/>
			</div>

			<c:forEach var="ttiTest" items="${allTTITests}">
				<div>
					<label>${ttiTest.testName}</label>
					<c:forEach var="validResult" items="${ttiTest.validResults}">
						<input id="result-${ttiTest.id}-${validResult}"
									 name="${ttiTest.testNameShort}-${ttiTest.id}"
									 value="${validResult}"
									 class="testInputRadioButton"
									 data-testid="${ttiTest.id}"
									 type="radio"
									 style="width: auto; height: 30px; vertical-align: middle;"
									 />
						<label for="result-${ttiTest.id}-${validResult}"
									 style="width: auto; margin-right: 50px; font-size: 1.5em; vertical-align: middle;">
							${validResult}
						</label>
					</c:forEach>
				</div>
			</c:forEach>
		</form>

		<div style="margin-left: 180px;">
			<button type="button" class="addTTIButton autoWidthButton">
				Add TTI Results
			</button>
			<button type="button" class="clearFormButton autoWidthButton">
				Clear form
			</button>				
		</div>

	</div>

	<div id="${childContentId}">
	</div>

</div>