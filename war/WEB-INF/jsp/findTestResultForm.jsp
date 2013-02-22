<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<c:set var="findTestResultFormId">findTestResultForm-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${tabContentId}").find(".findTestResultButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findTestResultFormData = $("#${findTestResultFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findTestResults");
    showLoadingImage(resultsDiv);
    $.ajax({
      type : "GET",
      url : "findTestResult.html",
      data : findTestResultFormData,
      success: function(data) {
				         resultsDiv.html(data);
        				 window.scrollTo(0, document.body.scrollHeight);
      				 },
      error: function(data) {
							 showErrorMessage("Something went wrong. Please try again later.");        
      			 }
    });
  });

  $("#${tabContentId}").find(".clearFindFormButton").button({
    icons : {
      
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

  function getDateTestedFromInput() {
    return $("#${findTestResultFormId}").find(".dateTestedFrom");  
  }

  function getDateTestedToInput() {
    return $("#${findTestResultFormId}").find(".dateTestedTo");  
  }

  getDateTestedFromInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      getDateTestedToInput().datepicker("option", "minDate", selectedDate);
    }
  });

  getDateTestedToInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      getDateTestedFromInput().datepicker("option", "maxDate", selectedDate);
    }
  });

  // child div shows donor information. bind this div to testResultSummaryView event
  $("#${tabContentId}").bind("testResultSummaryView",
      function(event, content) {
    		$("#${mainContentId}").hide();
    		$("#${childContentId}").html(content);
  		});

  $("#${tabContentId}").bind("testResultSummarySuccess",
      function(event, content) {
    		$("#${mainContentId}").show();
    		$("#${childContentId}").html("");
    		$("#${tabContentId}").find(".testResultsTable").trigger("refreshResults");
  		});

});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Find Test Results</b>
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['testResults.find']}
			</p>
		</div>
		<form:form method="GET" commandName="findTestResultForm" id="${findTestResultFormId}"
			class="formInTabPane">
	
			<div class="collectionNumberInput">
				<form:label path="collectionNumber">${model.testResultFields.collectionNumber.displayName}</form:label>
				<!-- Spring supports dynamic attributes so placeholder can be added -->
				<form:input path="collectionNumber" placeholder="Collection Number"/>
			</div>
	
			<!-- div>
				<span style="margin-left: 15px;"> Date of testing between </span>
			</div>
			<div>
				<form:input path="dateTestedFrom" class="dateTestedFrom" placeholder="From"/>
					and
				<form:input path="dateTestedTo" class="dateTestedTo" placeholder="To"/>
			</div-->
	
			<div>
				<label></label>
				<button type="button" class="findTestResultButton">
					Find test results
				</button>
				<button type="button" class="clearFindFormButton">
					Clear form
				</button>
			</div>
		</form:form>
		<div class="findTestResults"></div>
	</div>

	<div id="${childContentId}"></div>
	
</div>
