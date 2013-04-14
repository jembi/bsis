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

    var collectionNumberInput = $("#${mainContentId}").find('input[name="collectionNumber"]');
    var collectionNumber = collectionNumberInput.val();

    if (collectionNumber == undefined || collectionNumber.length == 0) {
		  showErrorMessage("Collection number should be specified");
		  return;
		}

    var saveTestsData = {collectionNumber : collectionNumber};
    var ttiInput = {};
    var inputs = $("#${mainContentId}").find(".ttiInput:checked");
    for (var i = 0; i < inputs.length; ++i) {
      var input = $(inputs[i]);
      var testid = input.data("testid");
      var value = input.val();
      ttiInput[testid] = value;
    }
    saveTestsData.ttiInput = JSON.stringify(ttiInput);
    console.log(saveTestsData);
    saveTTITests(saveTestsData);
  }

  function saveTTITests(saveTestsData) {
    $.ajax({
      url: "saveTTITests.html",
      type: "POST",
      data: saveTestsData,
      success: function(response) {
        				 $("#${tabContentId}").replaceWith(response);
      				 },
      error: function(response) {
        				$("#${tabContentId}").replaceWith(response.responseText);
      			 }
    });
  }

  function refetchForm() {
    showLoadingImage($("#${tabContentId}"));
    $("#${tabContentId}").load("${refreshUrl}");
  }

  $("#${mainContentId}").find("form")
  											.submit(function(event) {
  											  event.preventDefault();
  											});
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
				<input type="text" name="collectionNumber" placeholder="Collection Number" value="${firstTimeRender ? '' : collectionNumber}"/>
			</div>

			<c:forEach var="ttiTest" items="${allTTITests}">
				<div>
					<label>${ttiTest.testName}</label>
					<c:forEach var="validResult" items="${ttiTest.validResults}">
						<input id="result-${ttiTest.id}-${validResult}"
									 name="${ttiTest.testNameShort}-${ttiTest.id}"
									 value="${validResult}"
									 class="ttiInput"
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