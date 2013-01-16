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
<c:set var="productsTableEditRowDivId">productsTableEditRowDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var productsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : 'C<"H"lfrT>t<"F"ip>T',
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
															$("#${tabContentId}").parent().trigger("productSummaryView");
											        var elements = $(node).children();
											        if (elements[0].getAttribute("class") === "dataTables_empty") {
											          return;
											        }
											        selectedRowId = elements[0].innerHTML;
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
        $.ajax({url: "${model.refreshUrl}",
          			data: {},
          			type: "GET",
          			success: function(response) {
          			  				 $("#${tabContentId}").html(response);
          							 }
        });
      }

      $("#${tabContentId}").find(".productsTable").bind("refreshResults", refreshResults);

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

		<c:when test="${fn:length(model.allProducts) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>

			<table id="${table_id}" class="dataTable productsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.productFields.productNumber.hidden != true}">
							<th>${model.productFields.productNumber.displayName}</th>
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
						<c:if test="${model.productFields.isAvailable.hidden != true}">
							<th>${model.productFields.isAvailable.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.isSafe.hidden != true}">
							<th>${model.productFields.isSafe.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${model.allProducts}">
					<c:if test="${!product.isSafe}">
						<tr style="color: red;">
					</c:if>
					<c:if test="${product.isSafe}">
						<tr style="color: green;">
					</c:if>
							<td style="display: none">${product.id}</td>
							<c:if test="${model.productFields.productNumber.hidden != true}">
								<td>${product.productNumber}</td>
							</c:if>
							<c:if test="${model.productFields.productType.hidden != true}">
								<td>${product.productType.productTypeName}</td>
							</c:if>
							<c:if test="${model.productFields.createdOn.hidden != true}">
								<td>${product.createdOn}</td>
							</c:if>
							<c:if test="${model.productFields.expiresOn.hidden != true}">
								<td>${product.expiresOn}</td>
							</c:if>
							<c:if test="${model.productFields.isAvailable.hidden != true}">
								<td>${product.isAvailable ? "&#x2713" : "&#x2717"}</td>
							</c:if>
							<c:if test="${model.productFields.isSafe.hidden != true}">
								<td>${product.isSafe ? "&#x2713" : "&#x2717"}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:otherwise>
	</c:choose>

</div>
