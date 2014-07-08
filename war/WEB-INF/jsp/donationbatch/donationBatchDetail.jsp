<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONATION_BATCH)">
<div class="formFormatClass printableArea">
  <br />

  <c:if test="${donationBatchFields.batchNumber.hidden != true }">
    <div>
      <label>${donationBatchFields.batchNumber.displayName}</label>
      <label>${donationBatch.batchNumber}</label>
    </div>
  </c:if>
  <c:if test="${donationBatchFields.collectionCenter.hidden != true }">
    <div>
      <label>${donationBatchFields.collectionCenter.displayName}</label>
      <label>${donationBatch.donationCenter}</label>
    </div>
  </c:if>
  <c:if test="${donationBatchFields.collectionSite.hidden != true }">
    <div>
      <label>${donationBatchFields.collectionSite.displayName}</label>
      <label>${donationBatch.donationSite}</label>
    </div>
  </c:if>
   <c:if test="${donationBatchFields.collectionSite.hidden != true }">
    <div>
      <label>${donationBatchFields.collectionSite.displayName}</label>
      <label>${donationBatch.donationSite}</label>
    </div>
  </c:if>
   <c:if test="${donationBatchFields.batchOpenedOn.hidden != true }">
    <div>
      <label>${donationBatchFields.batchOpenedOn.displayName}</label>
      <label>${donationBatch.batchOpenedOn}</label>
    </div>
  </c:if>
   <c:if test="${donationBatchFields.batchClosedOn.hidden != true }">
    <div>
      <label>${donationBatchFields.batchClosedOn.displayName}</label>
      <label>${donationBatch.batchClosedOn}</label>
    </div>
  </c:if>
  <c:if test="${donationBatchFields.notes.hidden != true }">
    <div>
      <label>${donationBatchFields.notes.displayName}</label>
      <label>${donationBatch.notes}</label>
    </div>
  </c:if>
  <div>
    <label>${donationBatchFields.lastUpdatedTime.displayName}</label>
    <label style="width: auto;">${donationBatch.lastUpdated}</label>
  </div>
  <div>
    <label>${donationBatchFields.lastUpdatedBy.displayName}</label>
    <label style="width: auto;">${donationBatch.lastUpdatedBy}</label>
  </div>
  <hr />
</div>
</sec:authorize>
