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
<c:set var="findTestResultFormDivId">findTestResultFormDiv-${unique_page_id}</c:set>
<c:set var="findTestResultFormId">findTestResultForm-${unique_page_id}</c:set>
<c:set var="findTestResultFormSearchById">findTestResultFormSearchBySelector-${unique_page_id}</c:set>
<c:set var="findTestResultFormResultId">findTestResultFormResult-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${findTestResultFormDivId}").find(".findTestResultButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findTestResultFormData = $("#${findTestResultFormId}").serialize();
    showLoadingImage('${findTestResultFormResultId}');
    $.ajax({
      type : "GET",
      url : "findTestResult.html",
      data : findTestResultFormData,
      success: function(data) {
      				   $('#${findTestResultFormResultId}').html(data);
        				 window.scrollTo(0, document.body.scrollHeight);
      				 },
      error: function(data) {
							 showErrorMessage("Something went wrong. Please try again later.");        
      			 }
    });
  });

  $("#${findTestResultFormDivId}").find(".clearFindFormButton").button({
    icons : {
      primary : 'ui-icon-grip-solid-horizontal'
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
		$("#${findTestResultFormResultId}").html("");
		refetchContent("${model.refreshUrl}", $("${findTestResultFormDivId}"));
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

});
</script>

<div id="${findTestResultFormDivId}" class="formDiv">
	<b>Find test results</b>
	<form:form method="GET" commandName="findTestResultForm" id="${findTestResultFormId}"
		class="formInTabPane">

		<div class="collectionNumberInput">
			<form:label path="collectionNumber">${model.testResultFields.collectionNumber.displayName}</form:label>
			<!-- Spring supports dynamic attributes so placeholder can be added -->
			<form:input path="collectionNumber" placeholder="Collection Number"/>
		</div>

		<div>
			<span style="margin-left: 15px;"> Date of testing between (optional)</span>
		</div>
		<div>
			<form:input path="dateTestedFrom" class="dateTestedFrom" placeholder="From"/>
				and
			<form:input path="dateTestedTo" class="dateTestedTo" placeholder="To"/>
		</div>

		<div>
			<label></label>
			<button type="button" class="findTestResultButton">
				Find test result
			</button>
			<button type="button" class="clearFindFormButton">
				Clear form
			</button>
		</div>
	</form:form>
</div>

<div id="${findTestResultFormResultId}"></div>