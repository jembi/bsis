<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="formInTabPane printableArea">
	<br />
	<div class="collectionBarcode"></div>

	<c:if test="${collectedSampleFields.collectionNumber.hidden != true }">
		<div>
			<label>${collectedSampleFields.collectionNumber.displayName}</label>
			<label>${collectedSample.collectionNumber}</label>
		</div>
	</c:if>
	<c:if test="${collectedSampleFields.donorNumber.hidden != true }">
		<div>
			<label>${collectedSampleFields.donorNumber.displayName}</label>
			<c:if test="${not empty collectedSample.donorNumber}">
				<label style="width: auto;">${collectedSample.donorNumber} (${collectedSample.donor.firstName} ${collectedSample.donor.lastName})</label>
			</c:if>
		</div>
	</c:if>
	<c:if test="${collectedSampleFields.donorType.hidden != true }">
		<div>
			<label>${collectedSampleFields.donorType.displayName}</label>
			<label>${collectedSample.donorType}</label>
		</div>
	</c:if>
	<c:if test="${collectedSampleFields.bloodBagType.hidden != true }">
		<div>
			<label>${collectedSampleFields.bloodBagType.displayName}</label>
			<label>${collectedSample.bloodBagType}</label>
		</div>
	</c:if>
	<c:if test="${collectedSampleFields.collectedOn.hidden != true }">
		<div>
			<label>${collectedSampleFields.collectedOn.displayName}</label>
			<label style="width: auto;">${collectedSample.collectedOn}</label>
		</div>
	</c:if>
	<c:if test="${collectedSampleFields.collectionCenter.hidden != true }">
		<div>
			<label>${collectedSampleFields.collectionCenter.displayName}</label>
			<label>${collectedSample.collectionCenter}</label>
		</div>
	</c:if>
	<c:if test="${collectedSampleFields.collectionSite.hidden != true }">
		<div>
			<label>${collectedSampleFields.collectionSite.displayName}</label>
			<label>${collectedSample.collectionSite}</label>
		</div>
	</c:if>
	<c:if test="${collectedSampleFields.notes.hidden != true }">
		<div>
			<label>${collectedSampleFields.notes.displayName}</label>
			<label>${collectedSample.notes}</label>
		</div>
	</c:if>
	<div>
		<label>${collectedSampleFields.lastUpdatedTime.displayName}</label>
		<label style="width: auto;">${collectedSample.lastUpdated}</label>
	</div>
	<div>
		<label>${collectedSampleFields.lastUpdatedBy.displayName}</label>
		<label style="width: auto;">${collectedSample.lastUpdatedBy}</label>
	</div>
	<div>
		<label>${collectedSampleFields.testedStatus.displayName}</label>
		<label style="width: auto;">${collectedSample.testedStatus}</label>
	</div>
	<hr />
</div>
