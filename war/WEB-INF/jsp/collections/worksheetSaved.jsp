<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  
});
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_DONATION)">
<div id="${tabContentId}">

  <div id="${mainContentId}">
    <div class="tipsBox ui-state-highlight">
      <p>
        <c:if test="${model.success}">
          All collections saved to worksheet. Worksheet Number is ${model.worksheetNumber}.
        </c:if>
        <c:if test="${!model.success}">
          <span style="color: red">Error saving collections to worksheet with Worksheet Number ${model.worksheetNumber}.</span>
        </c:if>
      </p>
    </div>
  </div>

</div>
</sec:authorize>