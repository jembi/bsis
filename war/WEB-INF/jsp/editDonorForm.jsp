<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<form:form method="POST" commandName="editDonorForm">
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
				<td><form:label path="birthDate">${model.dobDisplayName}</form:label></td>
				<td><form:input path="birthDate" id="updateDonorBirthDate" /></td>
			</tr>
			<tr>
				<td><form:label path="gender">${model.genderDisplayName}</form:label></td>
				<td><form:select path="gender">
						<form:option value="male" label="Male" />
						<form:option value="female" label="Female" />
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="bloodType">${model.bloodTypeDisplayName}</form:label></td>
				<td><form:select path="bloodType">
						<form:option value="A+" label="A+" />
						<form:option value="A-" label="A-" />
						<form:option value="B+" label="B+" />
						<form:option value="B-" label="B-" />
						<form:option value="AB+" label="AB+" />
						<form:option value="AB-" label="AB-" />
						<form:option value="O+" label="O+" />
						<form:option value="O-" label="O-" />
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="address">${model.addressDisplayName}</form:label></td>
				<td><form:textarea path="address" id="donorAddressInputBox"
						maxlength="255" /></td>
			</tr>
			<tr>
				<td><form:label path="comments">${model.commentsDisplayName}</form:label></td>
				<td><form:textarea path="comments" id="donorCommentsInputBox"
						maxlength="255" /></td>
			</tr>
		</tbody>
	</table>
</form:form>