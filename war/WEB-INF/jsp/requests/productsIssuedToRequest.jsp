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
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">issuedProductsTable-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {
      var selectedRowId = null;
      var productsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lfr>t<"F"ip>',
        "sPaginationType" : "full_numbers",
      });

      $("#${tabContentId}").find(".doneButton").button({ icons: {primary: 'ui-icon-check' }}).click(function() {
        $("#${tabContentId}").parent().trigger("listIssuedProductsDone");
      });

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });
    });
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).ISSUE_COMPONENT)">
<div id="${tabContentId}">

  <div>
    <button class="doneButton">Done</button>
  </div>

  <br />

  <c:choose>

    <c:when test="${fn:length(model.issuedProducts) le 0}">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        No products issued for this request
      </span>
    </c:when>

    <c:otherwise>

      <table id="${table_id}" class="dataTable productsTable">
        <thead>
          <tr>
            <th style="display: none"></th>
            <c:if test="${model.productFields.collectionNumber.hidden != true}">
              <th>${model.productFields.collectionNumber.displayName}</th>
            </c:if>
            <c:if test="${model.productFields.productType.hidden != true}">
              <th>${model.productFields.productType.displayName}</th>
            </c:if>
            <c:if test="${model.productFields.createdOn.hidden != true}">
              <th>${model.productFields.createdOn.displayName}</th>
            </c:if>
            <c:if test="${model.productFields.expiresOn.hidden != true}">
              <th>${model.productFields.expiresOn.displayName}</th>
            </c:if>
            <c:if test="${model.productFields.issuedOn.hidden != true}">
              <th>${model.productFields.issuedOn.displayName}</th>
            </c:if>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="product" items="${model.issuedProducts}">
            <tr>
              <td style="display: none">${product.id}</td>
              <c:if test="${model.productFields.collectionNumber.hidden != true}">
                <td>${product.collectionNumber}</td>
              </c:if>
              <c:if test="${model.productFields.productType.hidden != true}">
                <td>${product.productType.productType}</td>
              </c:if>
              <c:if test="${model.productFields.createdOn.hidden != true}">
                <td>${product.createdOn}</td>
              </c:if>
              <c:if test="${model.productFields.expiresOn.hidden != true}">
                <td>${product.expiresOn}</td>
              </c:if>
              <c:if test="${model.productFields.issuedOn.hidden != true}">
                <td>${product.issuedOn}</td>
              </c:if>
            </tr>
          </c:forEach>
        </tbody>
      </table>

    </c:otherwise>
  </c:choose>

</div>

<div id="${noResultsFoundDivId}" style="display: none;">
  <span
    style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
    Sorry no results found matching your search request </span>
</div>
</sec:authorize>