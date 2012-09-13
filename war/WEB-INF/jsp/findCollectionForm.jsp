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
    console.log(findCollectionFormData);
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
  //$(document).ready(function() {
  // TODO: can't position the multiselect elsewhere due to a bug in jquery
  // when switching to latest jquery multiselect does not work
  $("#findCollectionFormCenters").multiselect({
    position : {
      my : 'left top',
      at : 'right center'

    // only include the "of" property if you want to position
    // the menu against an element other than the button.
    // multiselect automatically sets "of" unless you explictly
    // pass in a value.
    }
  });
  //});
</script>

<form:form method="GET" commandName="findCollectionForm"
	id="findCollectionForm" class="findCollectionForm">
	<table>
		<thead>
			<tr>
				<th>Find a Collection</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="collectionNumber">${model.collectionNoDisplayName}</form:label></td>
				<td><form:input path="collectionNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="centers">${model.centerDisplayName}</form:label></td>
				<td><form:select path="centers" id="findCollectionFormCenters">
						<form:options items="${model.centers}" />
					</form:select></td>
			<tr>
				<td />
				<td><input type="button" value="Find Collection"
					id="findCollectionButton" /></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="findCollectionResult"></div>