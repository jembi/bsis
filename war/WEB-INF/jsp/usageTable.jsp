<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">usageTable-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var fnRowSelected = function(node) {
    var elements = $(node).children();
    if (elements[0].getAttribute("class") === "dataTables_empty") {
      return;
    }
    replaceContent("${tabContentId}", "${model.requestUrl}", "editUsageFormGenerator.html", {productNumber: elements[0].innerHTML});
  }

  var usageTable = $("#${table_id}").dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lfrT>t<"F"ip>T',
    "oTableTools" : {
      "sRowSelect" : "single",
      "aButtons" : [ "print" ],
      "fnRowSelected" : fnRowSelected
    }
  });

  $("#${table_id}_filter").find("label").find("input").keyup(function() {
    var searchBox = $("#${table_id}_filter").find("label").find("input");
    $("#${table_id}").removeHighlight();
    if (searchBox.val() != "")
      $("#${table_id}").find("td").highlight(searchBox.val());
  });

});
</script>

<div id="${tabContentId}">
<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.productNoDisplayName}</th>
			<c:if test="${model.showdateUsed==true}">
				<th>${model.dateUsedDisplayName}</th>
			</c:if>
			<c:if test="${model.showhospital==true}">
				<th>${model.hospitalDisplayName}</th>
			</c:if>
			<c:if test="${model.showward==true}">
				<th>${model.wardDisplayName}</th>
			</c:if>
			<c:if test="${model.showuseIndication==true}">
				<th>${model.useIndicationDisplayName}</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="usage" items="${model.allUsage}">
			<tr>
				<td>${usage.productNumber}</td>
				<c:if test="${model.showdateUsed==true}">
					<td>${usage.dateUsed}</td>
				</c:if>
				<c:if test="${model.showhospital==true}">
					<td>${usage.hospital}</td>
				</c:if>
				<c:if test="${model.showward == true}">
					<td>${usage.ward}</td>
				</c:if>
				<c:if test="${model.showuseIndication == true}">
					<td>${usage.useIndication}</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>