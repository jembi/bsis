<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

	<form:form method="POST" commandName="addDonorForm">
		<table>
			<thead>
				<tr>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><form:label path="donorNumber">${model.donorIDDisplayName}</form:label></td>
					<td><form:input path="donorNumber" /></td>
				</tr>
				<tr>
					<td><form:label path="firstName">${model.firstNameDisplayName}</form:label></td>
					<td><form:input path="firstName" /></td>
				</tr>
				<tr>
					<td><form:label path="lastName">${model.lastNameDisplayName}</form:label></td>
					<td><form:input path="lastName" /></td>
				</tr>
				<tr>
					<td />
					<td><input type="button" value="Add Donor" id="addDonorButton" /></td>
				</tr>
			</tbody>
		</table>
	</form:form>