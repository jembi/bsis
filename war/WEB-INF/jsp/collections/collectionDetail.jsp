<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="formFormatClass printableArea">
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
        <label style="width: auto;">${collectedSample.donorNumber}</label>
      </c:if>
    </div>
  </c:if>
  <c:if test="${collectionFields.collectionBatchNumber.hidden != true }">
    <div>
      <label>${collectionFields.collectionBatchNumber.displayName}</label>
      <label style="width: auto;">${collectedSample.collectionBatchNumber}</label>
    </div>
  </c:if>
  <c:if test="${collectionFields.donationType.hidden != true }">
    <div>
      <label>${collectionFields.donationType.displayName}</label>
      <label>${collectedSample.donationType}</label>
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

  <sec:authorize access="hasRole('PERM_VIEW_TEST_INFORMATION')">
    <div>
      <label>${collectionFields.bloodTypingStatus.displayName}</label>
      <label style="width: auto;">${collectedSample.bloodTypingStatus}</label>
    </div>
    <div>
      <label>${collectionFields.ttiStatus.displayName}</label>
      <label style="width: auto;">${collectedSample.TTIStatus}</label>
    </div>
    <div>
      <label>${collectionFields.bloodAbo.displayName}</label>
      <label style="width: auto;">${collectedSample.bloodAbo}</label>
    </div>
    <div>
      <label>${collectionFields.bloodRh.displayName}</label>
      <label style="width: auto;">${collectedSample.bloodRh eq '+' ? 'POS' : collectedSample.bloodRh eq '-' ? 'NEG' : ''}</label>
    </div>
  </sec:authorize>
  
  <c:if test="${collectionFields.bloodPressureSystolic.hidden != true }">
    <div>
      <label>${collectionFields.bloodPressureSystolic.displayName}</label>
      <label>${collectedSample.bloodPressureSystolic}</label>
    </div>
  </c:if>
  
   <c:if test="${collectionFields.bloodPressureDiastolic.hidden != true }">
    <div>
      <label>${collectionFields.bloodPressureDiastolic.displayName}</label>
      <label>${collectedSample.bloodPressureDiastolic}</label>
    </div>
  </c:if>

	<c:if test="${collectionFields.haemoglobinCount.hidden != true }">
		<div>
			<label>${collectionFields.haemoglobinCount.displayName}</label>
		  <label>${collectedSample.haemoglobinCount}</label>
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
  <hr />
</div>
