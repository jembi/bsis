<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

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
                collectionNumber : elements[0].innerHTML
              });
        }

        var collectionsTable = $("#${table_id}").dataTable({
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
	<jsp:include page="addCollectionButton.jsp" flush="true" />
	<br /> <br />

	<table id="${table_id}" class="dataTable collectionsTable">
		<thead>
			<tr>
				<th>${model.collectionNoDisplayName}</th>
				<c:if test="${model.showdonorNo==true}">
					<td>${model.donorNoDisplayName}</td>
				</c:if>
				<c:if test="${model.showcenter==true}">
					<th>${model.centerDisplayName}</th>
				</c:if>
				<c:if test="${model.showsite==true}">
					<th>${model.siteDisplayName}</th>
				</c:if>
				<c:if test="${model.showdateCollected==true}">
					<th>${model.dateCollectedDisplayName}</th>
				</c:if>
				<c:if test="${model.showsampleNo==true}">
					<th>${model.sampleNoDisplayName}</th>
				</c:if>
				<c:if test="${model.showshippingNo==true}">
					<th>${model.shippingNoDisplayName}</th>
				</c:if>
				<c:if test="${model.showdonorType==true}">
					<th>${model.donorTypeDisplayName}</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="collection" items="${model.allCollections}">
				<tr>
					<td>${collection.collectionNumber}</td>
					<c:if test="${model.showdonorNo==true}">
						<td>${collection.donorNumber}</td>
					</c:if>
					<c:if test="${model.showcenter==true}">
						<td>${collection.centerName}</td>
					</c:if>
					<c:if test="${model.showsite==true}">
						<td>${collection.siteName}</td>
					</c:if>
					<c:if test="${model.showsampleNo==true}">
						<td>${collection.sampleNumber}</td>
					</c:if>
					<c:if test="${model.showshippingNo==true}">
						<td>${collection.shippingNumber}</td>
					</c:if>
					<c:if test="${model.showdonorType==true}">
						<td>${collection.donorType}</td>
					</c:if>
			</c:forEach>
		</tbody>
	</table>
</div>