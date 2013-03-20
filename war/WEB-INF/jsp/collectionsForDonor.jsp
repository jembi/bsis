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
<c:set var="table_id">collectionsTable-${unique_page_id}</c:set>
<c:set var="childContentId">childContentId-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var collectionsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : 'C<"H"lfrT>t<"F"ip>T',
        "oTableTools" : {
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

      $("#${tabContentId}").find(".doneButton").button({
        icons : {
          primary : 'ui-icon-check'
        }
      }).click(function() {
        $("#${tabContentId}").parent().trigger("donorHistoryDone");
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

	<button class="doneButton">Done</button>
	<c:choose>

		<c:when test="${fn:length(model.allCollectedSamples) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>

			<table id="${table_id}" class="dataTable collectionsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
							<th>${model.collectedSampleFields.collectionNumber.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.collectedOn.hidden != true}">
							<th>${model.collectedSampleFields.collectedOn.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true}">
							<th>${model.collectedSampleFields.bloodBagType.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.donationType.hidden != true}">
							<th>${model.collectedSampleFields.donationType.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="collectedSample" items="${model.allCollectedSamples}">
						<tr>
							<td style="display: none">${collectedSample.id}</td>
							<c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
								<td>${collectedSample.collectionNumber}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.collectedOn.hidden != true}">
								<td>${collectedSample.collectedOn}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true}">
								<td>${collectedSample.bloodBagType.bloodBagType}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.donationType.hidden != true}">
								<td>${collectedSample.donationType.donationType}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:otherwise>
	</c:choose>

</div>
