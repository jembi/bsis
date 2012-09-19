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
  $("#findRequestButton").button().click(function() {
    var findRequestFormData = $("#findRequestForm").serialize();
    $.ajax({
      type : "GET",
      url : "findRequest.html",
      data : findRequestFormData,
      success : function(data) {
        $('#findRequestResult').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });

  $("#findRequestFormProductTypes").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    }
  });

  $("#findRequestFormSites").multiselect({
    position : {
      my : 'left top',
      at : 'right center'

    }
  });

  $("#dateRequestedFrom").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateRequestedTo").datepicker("option", "minDate", selectedDate);
    }
  });

  $("#dateRequestedTo").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateRequestedFrom").datepicker("option", "maxDate", selectedDate);
    }
  });

  $("#dateRequiredFrom").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateRequiredTo").datepicker("option", "minDate", selectedDate);
    }
  });

  $("#dateRequiredTo").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateRequiredFrom").datepicker("option", "maxDate", selectedDate);
    }
  });
</script>

<form:form method="GET" commandName="findRequestForm"
	id="findRequestForm" class="findRequestForm">
	<h3>Find a Request</h3>
	<table>
		<thead>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="requestNumber">${model.requestNoDisplayName}</form:label></td>
				<td><form:input path="requestNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="dateRequestedFrom">${model.requestDateDisplayName} From </form:label></td>
				<td><form:input path="dateRequestedFrom" id="dateRequestedFrom" />
					to</td>
				<td><form:input path="dateRequestedTo" id="dateRequestedTo" /></td>
			</tr>
			<tr>
				<td><form:label path="dateRequiredFrom">${model.requiredDateDisplayName} From </form:label></td>
				<td><form:input path="dateRequiredFrom" id="dateRequiredFrom" />
					to</td>
				<td><form:input path="dateRequiredTo" id="dateRequiredTo" /></td>
			</tr>
			<tr>
				<td><form:label path="sites">${model.siteDisplayName}</form:label></td>
				<td><form:select path="sites" id="findRequestFormSites">
						<form:options items="${model.sites}" />
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="productTypes">${model.productTypeDisplayName}</form:label></td>
				<td><form:select path="productTypes"
						id="findRequestFormProductTypes">
						<form:option value="wholeBlood" label="Whole Blood"
							selected="" />
						<form:option value="rcc" label="RCC"
							selected="" />
						<form:option value="ffp" label="ffp"
							selected="" />
						<form:option value="platelets" label="Platelets"
							selected="" />
						<form:option value="partialPlatelets" label="Partial Platelets"
							selected="" />
					</form:select></td>
			</tr>
			<tr>
				<td />
				<td><input type="button" value="Find Request"
					id="findRequestButton" /></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="findRequestResult"></div>