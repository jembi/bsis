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
  $("#${mainContentId}").find(".wellInput").focusout(focusOutOfWell);
  
  function focusOutOfWell() {
    if ($(this).val().length > 0) {
    	$(this).addClass("wellWithData");
    }
  }

  $("#${mainContentId}").find(".wellInput").focus(function() {
    if ($(this).val().length > 0) {
    	$(this).removeClass("wellWithData");
    }
  });

  $.each($("#${mainContentId}").find(".wellInput"), focusOutOfWell);

	$("#${mainContentId}").find(".saveButton").
	button({icons : {primary: 'ui-icon-plusthick'}}).click(saveTestResults);

	function saveTestResults() {
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
	  $.ajax({
	    url: "saveBloodTypingTests.html",
	    data: {bloodTypingTests: JSON.stringify(data), collectionNumbers : collectionNumbers, refreshUrl: "${refreshUrl}"},
	    type: "POST",
	    success: function(response) {
	      				 $("#${tabContentId}").replaceWith(response);
	    			   },
	   	error: function(response) {
							 $("#${tabContentId}").replaceWith(response.responseText);	   	  
	   				 }
	  });
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

	$("#${mainContentId}").find(".changeCollectionsButton").button().
	click(function() {
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

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane">

		<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
		</c:if>

		<c:if test="${!empty collectionsWithBloodTypingTests && fn:length(collectionsWithBloodTypingTests) gt 0}">
		<div class="warningBox ui-state-highlight">
			<img src="images/warning_icon.png" style="height: 50px;" />
			<span class="warningText">
				<b>Warning</b>
				<br />
				The following collections already have blood typing results. Are you sure you want to add blood typing results again?
				<br />
				Previous data will be overwritten.
			</span>
			<table class="simpleTable">
				<thead>
					<tr>
						<th>Collection number</th>
						<th>Blood typing status</th>
						<th>Blood ABO</th>
						<th>Blood Rh</th>
						<th>Pending Tests</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="collectionWithTest" items="${collectionsWithBloodTypingTests}">
						<c:set var="collectionId" value="${collectionWithTest.key}" />
						<c:set var="bloodTypingRuleResult" value="${collectionWithTest.value}" />
						<tr>
							<td>${collectionMap[collectionId].collectionNumber}</td>
							<td>${bloodTypingRuleResult.bloodTypingStatus}</td>
							<td>${bloodTypingRuleResult.bloodAbo}</td>
							<td>${bloodTypingRuleResult.bloodRh}</td>
							<td>
								<c:forEach var="pendingTestId" items="${bloodTypingRuleResult.pendingTestsIds}">
									<c:set var="pendingTest" value="${bloodTypingTests[pendingTestId]}" />
											<li>
												${pendingTest.testName}
											</li>
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</c:if>

		<div class="bloodTypingPlate">
				<input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
									 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
									 text-align: center;
									 background: white;
									 border: none;
									 padding: 0;" value="${colNum}" disabled="disabled" />

				<c:forEach var="colNum" begin="1" end="${plate.numColumns}">
					<c:set var="collection" value="${collections[colNum]}" />
					<div class="wellBoxHeader">
						<input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
											 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
											 text-align: center;
											 background: rgb(255, 208, 165);
											 color: black;
											 padding: 0;" value="${colNum}" disabled="disabled" title="${not empty collection ? collection.collectionNumber : ''}" />
				 	</div>
				</c:forEach>

			<br />

			<c:forEach var="rowNum" begin="1" end="${plate.numRows}">
					<!-- Top row style similar to input wells -->
					<input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
										 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
										 text-align: center;
										 background: rgb(255, 208, 165);
										 color: black;
										 padding: 0;" value="&#${65 + rowNum-1};" disabled="disabled" />

				<c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
					<c:set var="collection" value="${collections[colNum]}" />

						<div class="wellBox">
							<!-- square around the well -->
							<c:if test="${empty collection}">
								<!-- show empty wells with disabled input -->
								<input
									style="width: ${bloodTypingConfig['titerWellRadius']}px; 
												 height: ${bloodTypingConfig['titerWellRadius']}px;
												 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
												 background: rgb(175, 175, 175);
												 text-align: center;
												 padding: 0;
												 "
									disabled="disabled"/>
							</c:if>
							<c:if test="${not empty collection}">
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
										style="width: ${bloodTypingConfig['titerWellRadius']}px; 
													 height: ${bloodTypingConfig['titerWellRadius']}px;
													 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
													 text-align: center;
													 border-color: ${wellBorderColor};
													 padding: 0;
													 "
										title="${collection.collectionNumber}"
										data-validresults="${bloodTestsOnPlate[rowNum-1].validResults}"
										data-collectionid="${collection.id}"
										data-testid="${bloodTestsOnPlate[rowNum-1].id}"
										value="${testResultValue}"
										class="wellInput" />
							</c:if>
					 	</div>
					</c:forEach>
				<label style="width: 70px;">${bloodTestsOnPlate[rowNum-1].testNameShort}</label>
				<br />
			</c:forEach>

			<c:if test="${not empty success and !success}">
			</c:if>
		</div>

		<div style="margin-left: 200px;">
			<label></label>
			<button type="button" class="saveButton">
				Save
			</button>
			<button type="button" class="changeCollectionsButton">
				Change collections
			</button>
			<button type="button" class="clearFormButton">
				Clear form
			</button>
		</div>

	</div>
</div>