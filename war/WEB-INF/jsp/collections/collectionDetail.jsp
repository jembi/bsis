<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="formInTabPane printableArea">
	<br />
	<div class="collectionBarcode"></div>

	<c:if test="${collectionFields.collectionNumber.hidden != true }">
		<div>
			<label>${collectionFields.collectionNumber.displayName}</label>
			<label>${collectedSample.collectionNumber}</label>
		</div>
	</c:if>
	<c:if test="${collectionFields.donorNumber.hidden != true }">
		<div>
			<label>${collectionFields.donorNumber.displayName}</label>
			<c:if test="${not empty collectedSample.donorNumber}">
				<label style="width: auto;">${collectedSample.donorNumber} (${collectedSample.donor.firstName} ${collectedSample.donor.lastName})</label>
			</c:if>
		</div>
	</c:if>
	<c:if test="${collectionFields.donorType.hidden != true }">
		<div>
			<label>${collectionFields.donorType.displayName}</label>
			<label>${collectedSample.donorType}</label>
		</div>
	</c:if>
	<c:if test="${collectionFields.bloodBagType.hidden != true }">
		<div>
			<label>${collectionFields.bloodBagType.displayName}</label>
			<label>${collectedSample.bloodBagType}</label>
		</div>
	</c:if>
	<c:if test="${collectionFields.collectedOn.hidden != true }">
		<div>
			<label>${collectionFields.collectedOn.displayName}</label>
			<label style="width: auto;">${collectedSample.collectedOn}</label>
		</div>
	</c:if>
	<c:if test="${collectionFields.collectionCenter.hidden != true }">
		<div>
			<label>${collectionFields.collectionCenter.displayName}</label>
			<label>${collectedSample.collectionCenter}</label>
		</div>
	</c:if>
	<c:if test="${collectionFields.collectionSite.hidden != true }">
		<div>
			<label>${collectionFields.collectionSite.displayName}</label>
			<label>${collectedSample.collectionSite}</label>
		</div>
	</c:if>
	<c:if test="${collectionFields.notes.hidden != true }">
		<div>
			<label>${collectionFields.notes.displayName}</label>
			<label>${collectedSample.notes}</label>
		</div>
	</c:if>
	<div>
		<label>${collectionFields.lastUpdatedTime.displayName}</label>
		<label style="width: auto;">${collectedSample.lastUpdated}</label>
	</div>
	<div>
		<label>${collectionFields.lastUpdatedBy.displayName}</label>
		<label style="width: auto;">${collectedSample.lastUpdatedBy}</label>
	</div>
	<div>
		<label>${collectionFields.testedStatus.displayName}</label>
		<label style="width: auto;">${collectedSample.testedStatus}</label>
	</div>
	<hr />
</div>
