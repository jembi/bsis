<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONATION_BATCH)">
<div class="formFormatClass printableArea">
  <br />

  <c:if test="${collectionBatchFields.batchNumber.hidden != true }">
    <div>
      <label>${collectionBatchFields.batchNumber.displayName}</label>
      <label>${collectionBatch.batchNumber}</label>
    </div>
  </c:if>
  <c:if test="${collectionBatchFields.collectionCenter.hidden != true }">
    <div>
      <label>${collectionBatchFields.collectionCenter.displayName}</label>
      <label>${collectionBatch.collectionCenter}</label>
    </div>
  </c:if>
  <c:if test="${collectionBatchFields.collectionSite.hidden != true }">
    <div>
      <label>${collectionBatchFields.collectionSite.displayName}</label>
      <label>${collectionBatch.collectionSite}</label>
    </div>
  </c:if>
  <c:if test="${collectionBatchFields.notes.hidden != true }">
    <div>
      <label>${collectionBatchFields.notes.displayName}</label>
      <label>${collectionBatch.notes}</label>
    </div>
  </c:if>
  <div>
    <label>${collectionBatchFields.lastUpdatedTime.displayName}</label>
    <label style="width: auto;">${collectionBatch.lastUpdated}</label>
  </div>
  <div>
    <label>${collectionBatchFields.lastUpdatedBy.displayName}</label>
    <label style="width: auto;">${collectionBatch.lastUpdatedBy}</label>
  </div>
  <hr />
</div>
</sec:authorize>
