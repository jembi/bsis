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

<script>
  $(document).ready(
      function() {

        var fnRowSelected = function(node) {
          var elements = $(node).children();
          if (elements[0].getAttribute("class") === "dataTables_empty") {
            return;
          }
          replaceContent("${tabContentId}", "${model.requestUrl}",
              "editCollectionFormGenerator.html", {
                collectionId : elements[0].innerHTML
              });
        }

        var collectionsTable = $("#${table_id}").dataTable({
          "bJQueryUI" : true,
          "sDom" : 'C<"H"lfrT>t<"F"ip>T',
          "oTableTools" : {
            "sRowSelect" : "single",
            "aButtons" : [ "print" ],
            "fnRowSelected" : fnRowSelected
          },
          "oColVis" : {
            "aiExclude" : [ 0 ],
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
						<c:if
							test="${model.collectedSampleFields.collectionNumber.hidden != true}">
							<td>${model.collectedSampleFields.collectionNumber.displayName}</td>
						</c:if>
						<c:if test="${model.collectedSampleFields.donor.hidden != true}">
							<td>${model.collectedSampleFields.donor.displayName}</td>
						</c:if>
						<c:if test="${model.collectedSampleFields.center.hidden != true}">
							<th>${model.collectedSampleFields.center.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.site.hidden != true}">
							<th>${model.collectedSampleFields.site.displayName}</th>
						</c:if>
						<c:if
							test="${model.collectedSampleFields.collectedOn.hidden != true}">
							<th>${model.collectedSampleFields.collectedOn.displayName}</th>
						</c:if>
						<c:if
							test="${model.collectedSampleFields.sampleNumber.hidden != true}">
							<th>${model.collectedSampleFields.sampleNumber.displayName}</th>
						</c:if>
						<c:if
							test="${model.collectedSampleFields.shippingNumber.hidden != true}">
							<th>${model.collectedSampleFields.shippingNumber.displayName}</th>
						</c:if>
						<c:if
							test="${model.collectedSampleFields.donorType.hidden != true}">
							<th>${model.collectedSampleFields.donorType.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="collectedSample" items="${model.allCollectedSamples}">
						<tr>
							<th style="display: none"></th>
							<c:if
								test="${model.collectedSampleFields.collectionNumber.hidden != true}">
								<td>${collectedSample.collectionNumber}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.donor.hidden != true}">
								<td>${collectedSample.donor.donorNumber}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.center.hidden != true}">
								<td>${collectedSample.center}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.site.hidden != true}">
								<td>${collectedSample.site}</td>
							</c:if>
							<c:if
								test="${model.collectedSampleFields.collectedOn.hidden != true}">
								<td>${collectedSample.collectedOn}</td>
							</c:if>
							<c:if
								test="${model.collectedSampleFields.sampleNumber.hidden != true}">
								<td>${collectedSample.sampleNumber}</td>
							</c:if>
							<c:if
								test="${model.collectedSampleFields.shippingNumber.hidden != true}">
								<td>${collectedSample.shippingNumber}</td>
							</c:if>
							<c:if
								test="${model.collectedSampleFields.donorType.hidden != true}">
								<td>${collectedSample.donorType}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>
</div>