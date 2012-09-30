<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script>
  $("#findTestResultButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findTestResultFormData = $("#findTestResultForm").serialize();
    $.ajax({
      type : "GET",
      url : "findTestResult.html",
      data : findTestResultFormData,
      success : function(data) {
        $('#findTestResultResult').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });

  $(".radioWithToggle").toggleRadio();

  $("#dateTestedFrom").datepicker({
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
  $("#dateTestedTo").datepicker({
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
</script>

<form:form method="GET" commandName="findTestResultForm"
	id="findTestResultForm" class="findTestResultForm">
	<table>
		<thead>
			<tr>
				<td><b>Find Test Results</b></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="collectionNumber">${model.collectionNoDisplayName}</form:label></td>
				<td><form:input path="collectionNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="hiv">${model.hivDisplayName}</form:label></td>
				<td><form:radiobutton path="hiv" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="hiv" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="hbv">${model.hbvDisplayName}</form:label></td>
				<td><form:radiobutton path="hbv" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="hbv" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="hcv">${model.hcvDisplayName}</form:label></td>
				<td><form:radiobutton path="hcv" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="hcv" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td><form:label path="syphilis">${model.syphilisDisplayName}</form:label></td>
				<td><form:radiobutton path="syphilis" value="reactive"
						label="reactive" class="radioWithToggle" /> <form:radiobutton
						path="syphilis" value="negative" label="negative"
						class="radioWithToggle" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b><i>Filter Test Results</i></b></td>
			</tr>
			<tr>
				<td>Having Date Tested Between</td>
			</tr>
			<tr>
				<td><form:input path="dateTestedFrom" id="dateTestedFrom" />
					to</td>
				<td><form:input path="dateTestedTo" id="dateTestedTo" /></td>
			</tr>
			<tr>
				<td />
				<td><button id="findTestResultButton" type="button">Find
						test result</button></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="findTestResultResult"></div>