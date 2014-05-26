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
<c:set var="table_id">issuedProductsTable-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  var selectedRowId = null;
  var productsTable = $("#${table_id}").dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lrT>t<"F"ip>',
    "sPaginationType" : "full_numbers",
    "oTableTools" : {
      "sRowSelect" : "single",
      "aButtons" : [],
      "fnRowSelected" : function(node) {
                      var elements = $(node).children();
                      if (elements[0].getAttribute("class") === "dataTables_empty") {
                        return;
                      }
                      var selectedRowId = elements[0].innerHTML;
                      showAddUsageDialog(selectedRowId);
                      },
      "fnRowDeselected" : function(node) {
                          }
    }
  });

  function showAddUsageDialog(productId) {
    var dialogContents = $('<div>').load("addUsageForProductFormGenerator.html?" + $.param({productId : productId}));

    dialogContents.dialog({
      modal: true,
      title: "Add Usage",
      width: 800,
      height: 400,
      buttons: {
        "Add usage": function() {
                       dialogContents.find(".addUsageForProductForm").trigger("addUsageForProduct");
                     },
        "Close":    function() {
                       $(this).dialog("close");
                        var oTableTools = TableTools.fnGetInstance($("#${mainContentId}").find("table")[0]);
                       oTableTools.fnSelectNone();
                     } 
      }
    });
  }

});
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).ISSUE_COMPONENT)">
<div id="${tabContentId}">

  <div id="${mainContentId}">

    <br />
    <c:if test="${not empty success and not success}">
      <jsp:include page="../common/errorBox.jsp">
        <jsp:param name="errorMessage" value="${errorMessage}" />
      </jsp:include>
    </c:if>
  
    <c:if test="${not empty success and success}">
      <c:choose>
    
        <c:when test="${fn:length(issuedProducts) le 0}">
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
                <c:if test="${productFields.collectionNumber.hidden != true}">
                  <th>${productFields.collectionNumber.displayName}</th>
                </c:if>
                <c:if test="${productFields.productType.hidden != true}">
                  <th>${productFields.productType.displayName}</th>
                </c:if>
                <c:if test="${productFields.createdOn.hidden != true}">
                  <th>${productFields.createdOn.displayName}</th>
                </c:if>
                <c:if test="${productFields.expiresOn.hidden != true}">
                  <th>${productFields.expiresOn.displayName}</th>
                </c:if>
                <c:if test="${productFields.issuedOn.hidden != true}">
                  <th>${productFields.issuedOn.displayName}</th>
                </c:if>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="product" items="${issuedProducts}">
                <tr>
                  <td style="display: none">${product.id}</td>
                  <c:if test="${productFields.collectionNumber.hidden != true}">
                    <td>${product.collectionNumber}</td>
                  </c:if>
                  <c:if test="${productFields.productType.hidden != true}">
                    <td>${product.productType.productType}</td>
                  </c:if>
                  <c:if test="${productFields.createdOn.hidden != true}">
                    <td>${product.createdOn}</td>
                  </c:if>
                  <c:if test="${productFields.expiresOn.hidden != true}">
                    <td>${product.expiresOn}</td>
                  </c:if>
                  <c:if test="${productFields.issuedOn.hidden != true}">
                    <td>${product.issuedOn}</td>
                  </c:if>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </c:if>
  </div>

</div>

<div id="${noResultsFoundDivId}" style="display: none;">
  <span
    style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
    Sorry no results found matching your search request </span>
</div>
</sec:authorize>
