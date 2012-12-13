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
<c:set var="table_id">testResultsTable-${unique_page_id}</c:set>
<c:set var="childContentId">childContentId-${unique_page_id}</c:set>

<script>
$(document).ready(
  function() {

    var testResultsTable = $("#${table_id}").dataTable({
      "bJQueryUI" : true,
      "sDom" : 'C<"H"lfrT>t<"F"ip>T',
      "oTableTools" : {
        "sRowSelect" : "single",
        "aButtons" : [ "print" ],
        "fnRowSelected" : function(node) {
												},
			"fnRowDeselected" : function(node) {
													},
      },
      "oColVis" : {
       	"aiExclude": [0,1],
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

	<c:choose>

		<c:when test="${fn:length(model.allUntestedCollectedSamples) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>
			<table id="${table_id}" class="dataTable testResultsTable">
				<thead>
					<tr>
							<th>Collection Number</th>
							<th>Tested On</th>
							<th>Blood Group</th>
							<th>HIV</th>
							<th>HBV</th>
							<th>HCV</th>
							<th>Syphilis</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="collectedSample" items="${model.allUntestedCollectedSamples}">
						<tr>
								<td>${collectedSample.collectionNumber}</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>

</div>
