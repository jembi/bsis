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
  $("#findDonorButton").button().click(function() {
    var findDonorFormData = $("#findDonorForm").serialize();
    console.log(findDonorFormData);
    $.ajax({
      type : "GET",
      url : "findDonor.html",
      data : findDonorFormData,
      success : function(data) {
        $('#findDonorResult').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });
</script>

<form:form method="GET" commandName="findDonorForm" id="findDonorForm" class="findDonorForm">
	<table>
		<thead>
			<tr>
				<th>Advanced Search</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="donorNumber">${model.donorIDDisplayName}</form:label></td>
				<td><form:input path="donorNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="firstName">${model.firstNameDisplayName} </form:label></td>
				<td><form:input path="firstName" /></td>
			</tr>
			<tr>
				<td><form:label path="lastName">${model.lastNameDisplayName}</form:label></td>
				<td><form:input path="lastName" /></td>
			</tr>
			<tr>
				<td><form:label path="bloodTypes">${model.bloodTypeDisplayName}</form:label></td>
				<td><form:checkbox path="bloodTypes"
						value="A+" label="A+" /> <form:checkbox path="bloodTypes"
						value="B+" label="B+" /> <form:checkbox path="bloodTypes"
						value="AB+" label="AB+" /> <form:checkbox path="bloodTypes"
						value="O+" label="O+" /> <form:checkbox path="bloodTypes"
						value="A-" label="A-" /> <form:checkbox path="bloodTypes"
						value="B-" label="B-" /> <form:checkbox path="bloodTypes"
						value="AB-" label="AB-" /> <form:checkbox path="bloodTypes"
						value="O-" label="O-" /></td>
			</tr>
			<tr>
				<td />
				<td><input type="button" value="Find Donor"
					id="findDonorButton" /></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="findDonorResult"></div>