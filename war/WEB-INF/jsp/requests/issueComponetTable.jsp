<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="table_id">requestsTable-${unique_page_id}</c:set>
<c:set var="requestsTableEditRowDivId">requestsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var requestsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>',
        "bServerSide" : true,
        "sAjaxSource" : "${nextPageUrl}",
        "sPaginationType" : "full_numbers",
        "aoColumnDefs" :  [{ "sClass" : "hide_class", "aTargets": [0]}
                          ],
        "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
                            oSettings.jqXHR = $.ajax({
                              "datatype": "json",
                              "type": "GET",
                              "url": sSource,
                              "data": aoData,
                              "success": function(jsonResponse) {
                                            if (jsonResponse.iTotalRecords == 0) {
                                              $("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
                                            }
                                            fnCallback(jsonResponse);
                                          }
                              });
                            },
         "oTableTools" : {
           "sRowSelect" : "single",
           "aButtons" : [ "print" ],
           "fnRowSelected" : function(node) {
                               /* $("#${mainContentId}").parent().trigger("requestSummaryView");
                               var elements = $(node).children();
                               if (elements[0].getAttribute("class") === "dataTables_empty") {
                                 return;
                               }
                               var selectedRowId = elements[0].innerHTML;
                               createRequestSummary("requestSummary.html",
                                   {requestId: selectedRowId}); */
                              },
         "fnRowDeselected" : function(node) {
                             },
         },
         "oColVis" : {
            "aiExclude": [0,1],
         }
      });

      function createRequestSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
                     $("#${tabContentId}").trigger("requestSummaryView", response);
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

      $("#${tabContentId}").find(".requestsTable").bind("refreshResults", refreshResults);

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });

    });
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">

    <c:choose>
  
      <c:when test="${empty issuedComponent || fn:length(issuedComponent) eq -1}">
        <!-- <span
          style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
          Sorry no results found matching your search request </span> -->
      </c:when>
  
      <c:otherwise>
  
        <table id="${table_id}" class="dataTable requestsTable">
          <thead>
            <tr>
            	<th style="display: none"></th>
      			<th>ComponentType</th>
      			<th>DIN</th>
      			<th>Blood ABO</th>
      			<th>Blood Rh</th>
      			<th>Num. Units</th>
      			<c:if test="${bulkTransferStatus == true }"><th>Compatbility</th></c:if>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="requestedComponents" items="${issuedComponent}">
      			<tr>
      				<td style="display: none">${requestedComponents.id}</td>
      				<td align="left">${requestedComponents.products.get(0).issuedTo.requestedComponents.get(0).productType.productType}</td>
      				<td align="left">${requestedComponents.collectionNumber}</td>
      				<td align="center">${requestedComponents.products.get(0).issuedTo.requestedComponents.get(0).bloodABO}</td>
      				<td align="center">${requestedComponents.products.get(0).issuedTo.requestedComponents.get(0).bloodRh eq '+' ? 'POS' : requestedComponents.products.get(0).issuedTo.requestedComponents.get(0).bloodRh eq '-' ? 'NEG' : ''}</td>
      				<td align="center">${requestedComponents.products.get(0).issuedTo.requestedComponents.get(0).numUnits}</td>
      				<c:if test="${bulkTransferStatus == true }"><td align="center">${requestedComponents.products.get(0).issuedTo.crossmatchTests.get(0).compatibilityResult}</td></c:if>
      			</tr>
	      	</c:forEach>
          </tbody>
        </table>
  
      </c:otherwise>
    </c:choose>
  
    <div id="${noResultsFoundDivId}" style="display: none;">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
    </div>

  </div>

</div>

