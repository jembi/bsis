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

<c:set var="wellPrefixId">wellPrefixId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  // this object stores all the data entered into the wells
  var ttidata = {};

  for (var i = 1; i <= ${plate.numRows}; ++i) {
    ttidata[i] = {};
    for (var j = 1; j <= ${plate.numColumns}; ++j) {
      ttidata[i][j] = {'result' : ''};
    }
  }

  console.log(ttidata);

  $("#${mainContentId}").find(".wellInput").focusout(focusOutOfWell);

  
  function focusOutOfWell() {
    if ($(this).val().length > 0) {
    	$(this).addClass("wellWithData");
    }
  }

  $("#${mainContentId}").find(".wellInput").focus(function() {
    $("#${mainContentId}").find(".wellDetails")
    										  .show();
    if ($(this).val().length > 0) {
    	$(this).removeClass("wellWithData");
    }
  });

  $.each($("#${mainContentId}").find(".wellInput"), focusOutOfWell);

	$("#${mainContentId}").find(".saveButton").
	button({icons : {primary: 'ui-icon-plusthick'}}).click(saveTestResults);

	function saveTestResults(eventObj, saveUninterpretableResults) {
	  console.log(this);
	  console.log(saveUninterpretableResults);
	  if (saveUninterpretableResults === undefined)
	  	saveUninterpretableResults = false;
	  var inputs = $("#${mainContentId}").find(".wellInput");
	  var data = {};
	  for (var index = 0; index < inputs.length; index++) {
			var input = inputs[index];
			var collectionId = $(input).data("collectionid");
			var testId = $(input).data("testid");
			if (data[collectionId] === undefined) {
			  data[collectionId] = {};
			}
			data[collectionId][testId] = $(input).val();
	  }
    var collectionNumbers = [];
    var collectionNumberStrs = "${collectionNumbers}".split(",");
    for (var index = 0; index < collectionNumberStrs.length; index++) {
      collectionNumbers.push(collectionNumberStrs[index]);
    }
    showLoadingImage($("#${tabContentId}"));
    var bloodTestsData = {bloodTypingTests: JSON.stringify(data),
        									collectionNumbers : collectionNumbers,
        									refreshUrl: "${refreshUrl}",
        									saveUninterpretableResults : saveUninterpretableResults};
    saveTestResultsWithConfirmation("saveBloodTypingTests.html", bloodTestsData, saveBloodTestsSuccess, saveBloodTestsError);
	}

	function saveBloodTestsSuccess(response) {
	  $("#${tabContentId}").replaceWith(response);
	}

	function saveBloodTestsError(response) {
	  $("#${tabContentId}").replaceWith(response.responseText);
	}

	$("#${mainContentId}").find(".clearFormButton").
	button().click(refetchForm);

	function refetchForm() {
		$.ajax({
		  url: "${refreshUrl}",
		  data: {},
		  type: "POST",
		  success: function (response) {
		    			 	 $("#${tabContentId}").replaceWith(response);
		  				 },
		  error:   function (response) {
		    				 $("#${tabContentId}").replaceWith(response);
		  				 }
		  
		});
	}

	$("#${mainContentId}").find(".changeCollectionsButton")
												.button()
												.click(
							function() {
								$.ajax({
								  url: "${changeCollectionsUrl}",
								  data: {},
								  type: "GET",
								  success: function (response) {
								    			 	 $("#${tabContentId}").replaceWith(response);
								  				 },
								  error:   function (response) {
														 showErrorMessage("Something went wrong. Please try again.");
								  				 }
								  
			  				});	  
							});

	$("#${mainContentId}").find(".saveUninterpretableResultsButton")
												.button()
												.click(function(event) {
												  saveTestResults(event, true);
												});

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane">

		<br />
		<br />

		<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
		</c:if>

		<div style="width: 620px;">
		<div class="ttiPlate">
				<input style="width: ${ttiConfig['titerWellRadius']}px;height: ${ttiConfig['titerWellRadius']}px;
									 border-radius: ${ttiConfig['titerWellRadius']}px;
									 text-align: center;
									 background: white;
									 border: none;
									 padding: 0;" disabled="disabled" />

				<c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
					<c:set var="collection" value="${collections[colNum-1]}" />
					<div class="wellBoxHeader">
							<input style="width: ${ttiConfig['titerWellRadius']}px;height: ${ttiConfig['titerWellRadius']}px;
												 border-radius: ${ttiConfig['titerWellRadius']}px;
												 text-align: center;
												 background: rgb(255, 208, 165);
												 color: black;
												 padding: 0;" value="${colNum}" disabled="disabled" />
					 	</div>
				</c:forEach>


			<br />

			<c:forEach var="rowNum" begin="1" end="${plate.numRows}">
					<!-- Top row style similar to input wells -->
					<input style="width: ${ttiConfig['titerWellRadius']}px;height: ${ttiConfig['titerWellRadius']}px;
										 border-radius: ${ttiConfig['titerWellRadius']}px;
										 text-align: center;
										 background: rgb(255, 208, 165);
										 color: black;
										 padding: 0;" value="&#${65 + rowNum-1};" disabled="disabled" />

					<c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
						<div class="wellBox">
							<!-- square around the well -->
							<!-- non-empty wells -->
							<c:set var="testId" value="${bloodTestsOnPlate[rowNum-1].id + 0}" />
						  <c:set var="testResultValue" value="${empty bloodTypingTestResults ? '' : bloodTypingTestResults[collection.id][testId]}" />
						  <c:if test="${not empty errorMap[collection.id][testId]}">
						  	<c:set var="wellBorderColor" value="red" />
					  	</c:if>
						  <c:if test="${empty errorMap[collection.id][testId]}">
						  	<c:set var="wellBorderColor" value="" />
					  	</c:if>

							<input
								style="width: ${ttiConfig['titerWellRadius']}px; 
											 height: ${ttiConfig['titerWellRadius']}px;
											 border-radius: ${ttiConfig['titerWellRadius']}px;
											 text-align: center;
											 border-color: ${wellBorderColor};
											 padding: 0;
											 "
								data-rownum = "${rowNum}"
								data-colnum = "${colNum}"
								value="${testResultValue}"
								class="wellInput" />

					 	</div>
					</c:forEach>
				<br />
			</c:forEach>
		</div>

		<div style="position: relative; height: 220px;">
		<div class="wellDetails formInTabPane" style="height: 150px; margin-top: 15px; width: 93%; position: absolute; left: 0; right: 0; top: 0;">
			<div>
				<label>Collection number</label>
				<input name="collectionNumber" />
			</div>
		</div>

			<div style="position: absolute; right: 0; bottom: 0;">
				<label></label>
				<button type="button" class="saveButton">
					Save all test results on plate
				</button>
			</div>
		</div>
		</div>


	</div>
</div>