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
  $("#findCollectionButton").button().click(function() {
    var findCollectionFormData = $("#findCollectionForm").serialize();
    $.ajax({
      type : "GET",
      url : "findCollection.html",
      data : findCollectionFormData,
      success : function(data) {
        $('#findCollectionResult').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });
  $("#findCollectionFormCenters").multiselect({
    position : {
      my : 'left top',
      at : 'right center'

    }
  });
  $("#dateCollectedFrom").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yyyy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateCollectedTo").datepicker("option", "minDate", selectedDate);
    }
  });
  $("#dateCollectedTo").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "yy-mm-dd",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateCollectedFrom").datepicker("option", "maxDate", selectedDate);
    }
  });
</script>

<form:form method="GET" commandName="findCollectionForm"
	id="findCollectionForm" class="findCollectionForm">
	<h3>Find a Collection</h3>
	<table>
		<thead>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="collectionNumber">${model.collectionNoDisplayName}</form:label></td>
				<td><form:input path="collectionNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="sampleNumber">${model.sampleNoDisplayName}</form:label></td>
				<td><form:input path="sampleNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="shippingNumber">${model.shippingNoDisplayName}</form:label></td>
				<td><form:input path="shippingNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="dateCollectedFrom">${model.dateCollectedDisplayName} From </form:label></td>
				<td><form:input path="dateCollectedFrom" id="dateCollectedFrom" />
					to</td>
				<td><form:input path="dateCollectedTo" id="dateCollectedTo" /></td>
			</tr>
			<tr>
				<td><form:label path="centers">${model.centerDisplayName}</form:label></td>
				<td><form:select path="centers" id="findCollectionFormCenters">
						<form:options items="${model.centers}" />
					</form:select></td>
			</tr>
			<tr>
				<td />
				<td><input type="button" value="Find Collection"
					id="findCollectionButton" /></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="findCollectionResult"></div>