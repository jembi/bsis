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

<script>
$(document).ready(function() {

  if (${collectionFound} === true) {
    fetchBloodTypingResults();
  }

  function fetchBloodTypingResults() {
    getBloodTypingSection().load("showBloodTypingResultsForCollection.html?" +
        $.param({collectionId : "${collectionId}"}));
    getTTISection().load("showTTIResultsForCollection.html?" +
        $.param({collectionId : "${collectionId}"}));
    getCollectionSummaryForTestingSection().load("showCollectionSummaryForTesting.html?" +
        $.param({collectionId : "${collectionId}"}));
  }

  function getBloodTypingSection() {
    return $("#${mainContentId}").find(".bloodTypingSection");
  }

  function getCollectionSummaryForTestingSection() {
    return $("#${mainContentId}").find(".collectionSummarySection");
  }

  function getTTISection() {
    return $("#${mainContentId}").find(".ttiSection");
  }

  $("#${mainContentId}").bind("testResultsUpdated", fetchBloodTypingResults);

});
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_TEST_OUTCOME)">
<div id="${tabContentId}">

  <div id="${mainContentId}">

    <c:if test="${not collectionFound}">
      Collection not found. Please check the collection number.
    </c:if>

    <c:if test="${collectionFound}">
      <div class="collectionSummarySection" style="width: 70%;">
      </div>

      <br />
      <div style="position: relative; height: 100%;">
        <div style="display: inline-block; width: 380px; vertical-align: top; margin-right: 5px;">
          <span style="font-weight: bold;">Blood typing results for collection</span>
          <div class="bloodTypingSection">
          </div>
        </div>
      
        <div style="display: inline-block; width: 380px; vertical-align: top; margin-left: 5px;">
          <span style="font-weight: bold;">TTI results for collection</span>
          <div class="ttiSection">
          </div>
        </div>
      </div>
    </c:if>

  </div>

  <div id="${childContentId}">
  </div>
</div>
</sec:authorize>