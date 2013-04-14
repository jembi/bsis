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

<c:set var="ttiTestSelectorId">ttiTestSelectorId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${mainContentId}").find(".ttiTestSelector")
												.multiselect({
												   multiple : false,
												   selectedList : 1,
												   header : false
												 });

  $("#${mainContentId}").find(".generateTtiPlateButton")
  											.button()
  											.click(generateTtiPlate);

  $("#${mainContentId}").find(".clearFormButton")
  											.button()
  											.click(function() {
  											  			refetchForm();
  											  			emptyChildContent();
  											  		 });

  function generateTtiPlate() {
    var ttiTestId = $("#${mainContentId}").find('select[name="ttiTestId"]')
     																 		  .val();
    console.log(ttiTestId);
    $.ajax({
      url: "ttiWellsWorksheetGenerator.html",
      data: {ttiTestId : ttiTestId},
      success: function (response) {
        				 $("#${childContentId}").html(response);
      				 },
      error: function(response) {
        			 showErrorMessage("Something went wrong. Please try again.");
      			 }
    });
  }

  function refetchForm() {
    $("#${tabContentId}").load("${refreshUrl}");
  }

  function emptyChildContent() {
    $("#${childContentId}").html("");
  }
});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<form class="formInTabPane">
			<div>
				<label style="width: auto;">
					<b>Enter TTI Results on plate</b>
				</label>
			</div>
			<div>
				<label style="width: auto;">Select TTI Result to record on the plate</label>
				<select id="${ttiTestSelectorId}" class="ttiTestSelector" name="ttiTestId">
					<option value="-1"></option>
					<c:forEach var="ttiTest" items="${ttiTests}">
						<option value="${ttiTest.id}">
							${ttiTest.testName}
						</option>
					</c:forEach>
				</select>
			</div>
		</form>

		<div class="formInTabPane">
			<div>
				<label>&nbsp;</label>
				<button class="generateTtiPlateButton">
					Generate TTI Plate for test
				</button>
				<button class="clearFormButton">
					Clear form
				</button>
			</div>
		</div>

	</div>

	<div id="${childContentId}">
	</div>

</div>