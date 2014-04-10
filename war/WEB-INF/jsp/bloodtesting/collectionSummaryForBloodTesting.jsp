<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_TEST_OUTCOME)">
<div id="${tabContentId}" class="collectionSummaryForTestingSection formFormatClass" style="margin-left: 5px;">
  <div>
    <label>${collectionFields.collectionNumber.displayName}</label>
    <label>${collection.collectionNumber}</label>
  </div>

  <div>
    <label>Blood Typing Status</label>
    <label>${collection.bloodTypingStatus}</label>
  </div>

  <div>
    <label>Blood ABO</label>
    <label>${collection.bloodAbo}</label>
  </div>

  <div>
    <label>Blood Rh</label>
    <label>${collection.bloodRh eq '+' ? 'POS' : collection.bloodRh eq '-' ? 'NEG' : ''}</label>
  </div>

  <div>
    <label>Extra information</label>
    <label>${collection.extraBloodTypeInformation}</label>
  </div>

  <div>
    <label>TTI Testing Status</label>
    <label>${collection.TTIStatus}</label>
  </div>

</div>
</sec:authorize>