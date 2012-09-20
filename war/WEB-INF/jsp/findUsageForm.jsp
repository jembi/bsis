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
  $("#findUsageButton").button().click(function() {
    var findUsageFormData = $("#findUsageForm").serialize();
    $.ajax({
      type : "GET",
      url : "findUsage.html",
      data : findUsageFormData,
      success : function(data) {
        $('#findUsageResult').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });

  $("#findUsageFormUseIndications").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    }
  });

  $("#dateUsedFrom").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateUsedTo").datepicker("option", "minDate", selectedDate);
    }
  });

  $("#dateUsedTo").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateUsedFrom").datepicker("option", "maxDate", selectedDate);
    }
  });
</script>

<form:form method="GET" commandName="findUsageForm" id="findUsageForm"
	class="findUsageForm">
	<table>
		<thead>
			<tr>
				<td><b>Find Product Usage Information</b></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="productNumber">${model.productNoDisplayName}</form:label></td>
				<td><form:input path="productNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="useIndications">${model.useIndicationDisplayName}</form:label></td>
				<td><form:select path="useIndications" id="findUsageFormUseIndications">
						<form:option path="useIndications" value="used" label="Used" />
						<form:option path="useIndications" value="discarded"
							label="Discarded" />
						<form:option path="useIndication" value="other" label="Other" />
					</form:select></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b><i>Filter Requests</i></b></td>
			</tr>
			<tr>
				<td>Having Usage Date Between</td>
			</tr>
			<tr>
				<td><form:input path="dateUsedFrom" id="dateUsedFrom" />&nbsp;and
				</td>
				<td><form:input path="dateUsedTo" id="dateUsedTo" /></td>
			</tr>
			<tr>
				<td><input type="button" value="Find Usage"
					id="findUsageButton" /></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="findUsageResult"></div>