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
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="table_id">worksheetsTable-${unique_page_id}</c:set>
<c:set var="worksheetsTableEditRowDivId">worksheetsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var worksheetsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>',
        "sPaginationType" : "full_numbers",
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]}
        								 ],
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
															$("#${mainContentId}").parent().trigger("collectionSummaryView");
											        var elements = $(node).children();
											        if (elements[0].getAttribute("class") === "dataTables_empty") {
											          return;
											        }
											        var selectedRowId = elements[0].innerHTML;
											        createWorksheetSummary("worksheetSummary.html",
									  							{worksheetId: selectedRowId});
 													  },
				"fnRowDeselected" : function(node) {
														},
        },
        "oColVis" : {
         	"aiExclude": [0,1],
        }
      });
      
      function createWorksheetSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
            				 $("#${tabContentId}").trigger("worksheetSummaryView", response);
            			 }
        });

      }

      function refreshResults() {
        showLoadingImage($("#${mainContentId}"));
        $.ajax({url: "${refreshUrl}",
          			type: "GET",
          			success: function(response) {
          			  				 $("#${mainContentId}").html(response);
          							 }
        });
      }

      $("#${mainContentId}").find(".worksheetsTable").bind("refreshResults", refreshResults);

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

			<c:when test="${fn:length(allWorksheets) eq 0}">
				<span style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
					Sorry no results found matching your search request
				</span>
			</c:when>
	
			<c:otherwise>
				<table id="${table_id}" class="dataTable worksheetsTable">
					<thead>
						<tr>
							<th style="display: none"></th>
							<c:if test="${worksheetFields.worksheetNumber.hidden != true}">
								<th>${worksheetFields.worksheetNumber.displayName}</th>
							</c:if>
							<c:if test="${worksheetFields.createdDate.hidden != true}">
								<th>${worksheetFields.createdDate.displayName}</th>
							</c:if>
							<c:if test="${worksheetFields.worksheetType.hidden != true}">
								<th>${worksheetFields.worksheetType.displayName}</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="worksheet" items="${allWorksheets}">
							<tr>
								<td style="display: none">${worksheet.id}</td>
								<c:if test="${worksheetFields.worksheetNumber.hidden != true}">
									<td>${worksheet.worksheetNumber}</td>
								</c:if>
								<c:if test="${worksheetFields.createdDate.hidden != true}">
									<td>${worksheet.createdDate}</td>
								</c:if>
								<c:if test="${worksheetFields.worksheetType.hidden != true}">
									<td>${worksheet.worksheetType.worksheetType}</td>
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
