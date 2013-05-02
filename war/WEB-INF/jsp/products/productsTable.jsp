<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">productsTable-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var productsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>T',
        "bServerSide" : true,
        "sAjaxSource" : "${nextPageUrl}",
        "sPaginationType" : "full_numbers",
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]}
                         ],
        "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
                           oSettings.jqXHR = $.ajax({
                             "datatype": "json",
                             "type": "GET",
                             "url": sSource,
                             "data": aoData,
                             "success": function(jsonResponse) {
                                           if (jsonResponse.iTotalRecords == 0) {
                                             $("#${tabContentId}").html($("#${noResultsFoundDivId}").html());
                                           }
                                           fnCallback(jsonResponse);
                                         }
                             });
                           },
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
                              $("#${tabContentId}").parent().trigger("productSummaryView");
                              var elements = $(node).children();
                              if (elements[0].getAttribute("class") === "dataTables_empty") {
                                return;
                              }
                              var selectedRowId = elements[0].innerHTML;
                              createProductSummary("productSummary.html",
                                  {productId: selectedRowId});
                             },
        "fnRowDeselected" : function(node) {
                            },
        },
        "oColVis" : {
           "aiExclude": [0,1],
        }
      });
      
      function createProductSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
                     $("#${tabContentId}").trigger("productSummaryView", response);
                   }
        });
      }

      function refreshResults() {
        showLoadingImage($("#${tabContentId}"));
        $.ajax({url: "${refreshUrl}",
                data: {},
                type: "GET",
                success: function(response) {
                           $("#${tabContentId}").html(response);
                         }
        });
      }

      $("#${tabContentId}").find(".productsTable").bind("refreshResults", refreshResults);

    });
</script>

<div id="${tabContentId}">

  <c:choose>

    <c:when test="${fn:length(allProducts) eq -1}">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
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
            <c:if test="${productFields.bloodGroup.hidden != true}">
              <th>${productFields.bloodGroup.displayName}</th>
            </c:if>
            <c:if test="${productFields.createdOn.hidden != true}">
              <th>${productFields.createdOn.displayName}</th>
            </c:if>
            <c:if test="${productFields.expiresOn.hidden != true}">
              <th>${productFields.expiresOn.displayName}</th>
            </c:if>
            <c:if test="${productFields.status.hidden != true}">
              <th>${productFields.status.displayName}</th>
            </c:if>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="product" items="${allProducts}">
            <tr>
              <td style="display: none">${product.id}</td>
              <c:if test="${productFields.collectionNumber.hidden != true}">
                <td>${product.collectionNumber}</td>
              </c:if>
              <c:if test="${productFields.productType.hidden != true}">
                <td>${product.productType.productTypeNameShort}</td>
              </c:if>
              <c:if test="${productFields.bloodGroup.hidden != true}">
                <td>${product.bloodGroup}</td>
              </c:if>
              <c:if test="${productFields.createdOn.hidden != true}">
                <td>${product.createdOn}</td>
              </c:if>
              <c:if test="${productFields.expiresOn.hidden != true}">
                <td>${product.expiresOn}</td>
              </c:if>
              <c:if test="${productFields.status.hidden != true}">
                <td>${product.status}</td>
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
