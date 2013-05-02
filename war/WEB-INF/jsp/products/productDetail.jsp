<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="formFormatClass printableArea">
	<br />
		<c:if test="${productFields.collectionNumber.hidden != true }">
			<div>
				<label>${productFields.collectionNumber.displayName}</label>
				<label>${product.collectionNumber}</label>
			</div>
		</c:if>
	<c:if test="${productFields.createdOn.hidden != true }">
		<div>
			<label>${productFields.createdOn.displayName}</label>
			<label style="width: auto;">${product.createdOn}</label>
		</div>
	</c:if>
	<c:if test="${productFields.expiresOn.hidden != true }">
		<div>
			<label>${productFields.expiresOn.displayName}</label>
			<label style="width: auto;">${product.expiresOn} (${product.expiryStatus})</label>
		</div>
	</c:if>
	<c:if test="${productFields.productType.hidden != true }">
		<div>
			<label>${productFields.productType.displayName}</label>
			<label>${product.productType.productType}</label>
		</div>
	</c:if>
	<c:if test="${productFields.status.hidden != true }">
		<div>
			<label>${productFields.status.displayName}</label>
			<label>${product.status}</label>
		</div>
	</c:if>
	<c:if test="${productFields.bloodGroup.hidden != true }">
		<div>
			<label>${productFields.bloodGroup.displayName}</label>
			<label>${product.bloodGroup}</label>
		</div>
	</c:if>
	<c:if test="${productFields.bloodGroup.hidden != true }">
		<div>
			<label>${productFields.age.displayName}</label>
			<label>${product.age}</label>
		</div>
	</c:if>
	<c:if test="${productFields.notes.hidden != true }">
		<div>
			<label>${productFields.notes.displayName}</label>
			<label>${product.notes}</label>
		</div>
	</c:if>
	<c:if test="${productFields.issuedTo.hidden != true && !empty product.issuedTo && product.status=='ISSUED'}">
		<div>
			<label>${productFields.issuedTo.displayName}</label>
			<label>${product.issuedTo.requestNumber}</label>
		</div>
		<c:if test="${productFields.issuedOn.hidden != true}">
			<div>
				<label>${productFields.issuedOn.displayName}</label>
				<label style="width: auto;">${product.issuedOn}</label>
			</div>
		</c:if>
	</c:if>
	<c:if test="${product.status == 'DISCARDED'}">
		<div>
			<label>${productFields.discardedOn.displayName}</label>
			<label style="width: auto;">${product.discardedOn}</label>
		</div>
	</c:if>
	<div>
		<label>${productFields.lastUpdatedTime.displayName}</label>
		<label style="width: auto;">${product.lastUpdated}</label>
	</div>
	<div>
		<label>${productFields.lastUpdatedBy.displayName}</label>
		<label style="width: auto;">${product.lastUpdatedBy}</label>
	</div>
	<hr />
</div>
