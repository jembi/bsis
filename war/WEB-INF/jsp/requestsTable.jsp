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
<c:set var="table_id">requestsTable-${unique_page_id}</c:set>
<c:set var="requestsTableEditRowDivId">requestsTableEditRowDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var requestsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : 'C<"H"lfrT>t<"F"ip>T',
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
															$("#${tabContentId}").parent().trigger("requestSummaryView");
											        var elements = $(node).children();
											        if (elements[0].getAttribute("class") === "dataTables_empty") {
											          return;
											        }
											        selectedRowId = elements[0].innerHTML;
											        createRequestSummary("requestSummary.html",
									  							{requestId: selectedRowId});
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
        $.ajax({url: "${model.refreshUrl}",
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

	<c:choose>

		<c:when test="${fn:length(model.allRequests) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>

			<table id="${table_id}" class="dataTable requestsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.requestFields.bloodGroup.hidden != true}">
							<th>${model.requestFields.bloodGroup.displayName}</th>
						</c:if>
						<c:if test="${model.requestFields.requestDate.hidden != true}">
							<th>${model.requestFields.requestDate.displayName}</th>
						</c:if>
						<c:if test="${model.requestFields.requiredDate.hidden != true}">
							<th>${model.requestFields.requiredDate.displayName}</th>
						</c:if>
						<c:if test="${model.requestFields.productType.hidden != true}">
							<th>${model.requestFields.productType.displayName}</th>
						</c:if>
						<c:if test="${model.requestFields.requestedQuantity.hidden != true}">
							<th>${model.requestFields.requestedQuantity.displayName}</th>
						</c:if>
						<c:if test="${model.requestFields.issuedQuantity.hidden != true}">
							<th>${model.requestFields.issuedQuantity.displayName}</th>
						</c:if>
						<c:if test="${model.requestFields.requestSite.hidden != true}">
							<th>${model.requestFields.requestSite.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="request" items="${model.allRequests}">
						<tr>
							<td style="display: none">${request.id}</td>
							<c:if test="${model.requestFields.bloodGroup.hidden != true}">
								<td>${request.bloodGroup}</td>
							</c:if>
							<c:if test="${model.requestFields.requestDate.hidden != true}">
								<td>${request.requestDate}</td>
							</c:if>
							<c:if test="${model.requestFields.requiredDate.hidden != true}">
								<td>${request.requiredDate}</td>
							</c:if>
							<c:if test="${model.requestFields.productType.hidden != true}">
								<td>${request.productTypeName}</td>
							</c:if>
							<c:if test="${model.requestFields.requestedQuantity.hidden != true}">
								<td>${request.requestedQuantity}</td>
							</c:if>
							<c:if test="${model.requestFields.issuedQuantity.hidden != true}">
								<td>${request.issuedQuantity}</td>
							</c:if>
							<c:if test="${model.requestFields.requestSite.hidden != true}">
								<td>${request.requestSite}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:otherwise>
	</c:choose>

</div>
