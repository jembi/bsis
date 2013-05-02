<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
  $("#${mainContentId}").find(".productMovementTable")
                        .dataTable({
                          "bJQueryUI": true,
                          "sDom": '<"H"lr>t<"F"ip',
                          "bPaginate" : false
                        })
  $("#${tabContentId}").find(".doneButton")
                       .button({icons: {primary: 'ui-icon-check'}})
                       .click(function() {
                         $("#${tabContentId}").trigger("productHistoryDone");
                       });
});
</script>

<div id="${tabContentId}">
  <div id="${mainContentId}">
    <button class="doneButton">
      Done
    </button>
    <c:if test="${fn:length(allProductMovements) eq 0}">
        <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
    </c:if>
    <c:if test="${fn:length(allProductMovements) gt 0}">
      <table class="productMovementTable">
        <thead>
          <tr>
            <th>${productFields.statusChangeType.displayName}</th>
            <th>${productFields.status.displayName}</th>
            <th>${productFields.statusChangedOn.displayName}</th>
            <th>${productFields.statusChangedBy.displayName}</th>
            <th>${productFields.statusChangeReason.displayName}</th>
            <th>${productFields.statusChangeReasonText.displayName}</th>
            <th>${productFields.issuedTo.displayName}</th>
          </tr>
        </thead>
        <c:forEach var="productStatusChange" items="${allProductMovements}">
            <tr>
              <td>${productStatusChange.statusChangeType}</td>
              <td>${productStatusChange.newStatus}</td>
              <td>${productStatusChange.statusChangedOn}</td>
              <td>${productStatusChange.changedBy}</td>
              <td>${productStatusChange.statusChangeReason.statusChangeReason}</td>
              <td>${productStatusChange.statusChangeReasonText}</td>
              <td>${productStatusChange.issuedTo.requestNumber}</td>
            </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>
</div>