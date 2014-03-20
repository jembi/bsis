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
<c:set var="table_id">collectionsBatchTable-${unique_page_id}</c:set>
<c:set var="collectionsBatchTableEditRowDivId">collectionsBatchTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var collectionsBatchTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>T',
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]}
                         ],
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
                              $("#${tabContentId}").parent().trigger("collectionBatchSummaryView");
                              var elements = $(node).children();
                              if (elements[0].getAttribute("class") === "dataTables_empty") {
                                return;
                              }
                              selectedRowId = elements[0].innerHTML;
                              createCollectionBatchSummary("collectionBatchSummary.html",
                                  {collectionBatchId: selectedRowId});
                             },
        "fnRowDeselected" : function(node) {
                            },
        },
        "oColVis" : {
           "aiExclude": [0,1],
        }
      });
      
      function createCollectionBatchSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
                     $("#${tabContentId}").trigger("collectionBatchSummaryView", response);
                   }
        });
      }

      function refreshResults() {
        showLoadingImage($("#${tabContentId}"));
        $.ajax({url: "${refreshUrl}",
                type: "GET",
                success: function(response) {
                           $("#${tabContentId}").html(response);
                         }
        });
      }

      $("#${tabContentId}").find(".collectionsBatchTable").bind("refreshResults", refreshResults);

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });
    });
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONATION_BATCH)">
<div id="${tabContentId}">

  <div id="${mainContentId}">

    <c:choose>

      <c:when test="${fn:length(allCollectionBatches) eq 0}">
        <span style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
          Sorry no results found matching your search request
        </span>
      </c:when>
  
      <c:otherwise>
  
        <br />
        <br />
  
        <table id="${table_id}" class="dataTable collectionsBatchTable">
          <thead>
            <tr>
              <th style="display: none"></th>
              <c:if test="${collectionBatchFields.batchNumber.hidden != true}">
                <th>${collectionBatchFields.batchNumber.displayName}</th>
              </c:if>
              <c:if test="${collectionBatchFields.collectionCenter.hidden != true}">
                <th>${collectionBatchFields.collectionCenter.displayName}</th>
              </c:if>
              <c:if test="${collectionBatchFields.collectionSite.hidden != true}">
                <th>${collectionBatchFields.collectionSite.displayName}</th>
              </c:if>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="collectionBatch" items="${allCollectionBatches}">
              <tr>
                <td style="display: none">${collectionBatch.id}</td>
                <c:if test="${collectionBatchFields.batchNumber.hidden != true}">
                  <td>${collectionBatch.batchNumber}</td>
                </c:if>
                <c:if test="${collectionBatchFields.collectionCenter.hidden != true}">
                  <td>${collectionBatch.collectionCenter}</td>
                </c:if>
                <c:if test="${collectionBatchFields.collectionSite.hidden != true}">
                  <td>${collectionBatch.collectionSite}</td>
                </c:if>
              </tr>
            </c:forEach>
          </tbody>
        </table>
  
      </c:otherwise>
    </c:choose>
  </div>

  <div id="${childContentId}">
  </div>

</div>

<div id="${noResultsFoundDivId}" style="display: none;">
  <span
    style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
    Sorry no results found matching your search request </span>
</div>
</sec:authorize>
