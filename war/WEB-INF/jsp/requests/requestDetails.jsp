<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="printableArea">
	<div class="formInTabPane">
		<div class="requestBarcode"></div>	
		<c:if test="${requestFields.requestDate.hidden != true }">
			<div>
				<label>${requestFields.requestDate.displayName}</label>
				<label style="width: auto;">${request.requestDateWithTime}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.requiredDate.hidden != true }">
			<div>
				<label>${requestFields.requiredDate.displayName}</label>
				<label style="width:auto;">${request.requiredDate}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.patientBloodAbo.hidden != true }">
			<div>
				<label>${requestFields.patientBloodAbo.displayName}</label>
				<label>${request.patientBloodAbo}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.patientBloodRh.hidden != true }">
			<div>
				<label>${requestFields.patientBloodRh.displayName}</label>
				<label>${request.patientBloodRh}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.productType.hidden != true }">
			<div>
				<label>${requestFields.productType.displayName}</label>
				<label>${request.productType.productType}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.requestType.hidden != true }">
			<div>
				<label>${requestFields.requestType.displayName}</label>
				<label>${request.requestType.requestType}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.numUnitsRequested.hidden != true }">
			<div>
				<label>${requestFields.numUnitsRequested.displayName}</label>
				<label>${request.numUnitsRequested}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.numUnitsIssued.hidden != true }">
			<div>
				<label>${requestFields.numUnitsIssued.displayName}</label>
				<label>${request.numUnitsIssued}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.requestSite.hidden != true }">
			<div>
				<label>${requestFields.requestSite.displayName}</label>
				<label>${request.requestSite.name}</label>
			</div>
		</c:if>
	</div>
	<div class="formInTabPane showMoreSection">
		<c:if test="${requestFields.patientNumber.hidden != true }">
			<div>
				<label>${requestFields.patientNumber.displayName}</label>
				<label>${request.patientNumber}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.patientFirstName.hidden != true }">
			<div>
				<label>${requestFields.patientFirstName.displayName}</label>
				<label>${request.patientFirstName}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.patientLastName.hidden != true }">
			<div>
				<label>${requestFields.patientLastName.displayName}</label>
				<label>${request.patientLastName}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.patientBirthDate.hidden != true }">
			<div>
				<label>${requestFields.patientBirthDate.displayName}</label>
				<label>${request.patientBirthDate}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.patientAge.hidden != true }">
			<div>
				<label>${requestFields.patientAge.displayName}</label>
				<c:if test="${empty request.patientAge}">
					<label></label>
				</c:if>
				<c:if test="${not empty request.patientAge}">
					<label>${request.patientAge} years</label>
				</c:if>
			</div>
		</c:if>
		<c:if test="${requestFields.patientDiagnosis.hidden != true }">
			<div>
				<label>${requestFields.patientDiagnosis.displayName}</label>
				<label>${request.patientDiagnosis}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.ward.hidden != true }">
			<div>
				<label>${requestFields.ward.displayName}</label>
				<label>${request.ward}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.department.hidden != true }">
			<div>
				<label>${requestFields.department.displayName}</label>
				<label>${request.department}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.hospital.hidden != true }">
			<div>
				<label>${requestFields.hospital.displayName}</label>
				<label>${request.hospital}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.requestedBy.hidden != true }">
			<div>
				<label>${requestFields.requestedBy.displayName}</label>
				<label>${request.requestedBy}</label>
			</div>
		</c:if>
		<c:if test="${requestFields.notes.hidden != true }">
			<div>
				<label>${requestFields.notes.displayName}</label>
				<label>${request.notes}</label>
			</div>
		</c:if>
		<div>
			<label>${requestFields.lastUpdatedTime.displayName}</label>
			<label style="width: auto;">${request.lastUpdated}</label>
		</div>
		<div>
			<label>${requestFields.lastUpdatedBy.displayName}</label>
			<label style="width: auto;">${request.lastUpdatedBy}</label>
		</div>
	</div>
</div>
<button class="showMoreButton">Show more</button>
<button class="showLessButton">Show less</button>
