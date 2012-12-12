<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="updateTTIWorksheetId">updateTTIWorksheetId-${unique_page_id}</c:set>
<c:set var="dateCollectedFromInputId">dateCollectedFromInputId-${unique_page_id}</c:set>
<c:set var="dateCollectedToInputId">dateCollectedToInputId-${unique_page_id}</c:set>
<c:set var="ttiWorksheetId">ttiWorksheetId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${updateTTIWorksheetId}").button().click(
      function(){
		    $.ajax({
		      url:  "showTTIWorksheet.html",
		      type: "GET",
		      data: {	dateCollectedFrom: $("#${dateCollectedFromInputId}").val(),
		        			dateCollectedTo:   $("#${dateCollectedToInputId}").val(),
		      			},
		      success: function(responseData) {
		        				 showMessage("TTI Worksheet generated");
		        				 $("#${ttiWorksheetId}").html(responseData);
		      				 },
		     	error: function(responseData) {
		     	  			 showErrorMessage("Something went wrong. Please try again later.");
		     				 }
		    });
  		});
  
  $("#${dateCollectedFromInputId}").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateTestedTo").datepicker("option", "minDate", selectedDate);
    }
  });

  $("#${dateCollectedToInputId}").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateTestedFrom").datepicker("option", "maxDate", selectedDate);
    }
  });

});
</script>

<div id="${tabContentId}">

<label for="dateCollectedFrom">Date Collected From</label>
<input id="${dateCollectedFromInputId}" name="dateCollectedFrom" value="${model.dateCollectedFrom}"></input>
<label for="dateCollectedTo"> to </label>
<input id="${dateCollectedToInputId}" name="dateCollectedTo" value="${model.dateCollectedTo}"></input>
<button id="${updateTTIWorksheetId}">Generate worksheet</button>

<div id="${ttiWorksheetId}">
</div>

</div>