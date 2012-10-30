<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">donorsTable-${unique_page_id}</c:set>

<script>
  var fnRowSelected = function(node) {
    var elements = $(node).children();
    if (elements[0].getAttribute("class") === "dataTables_empty") {
      return;
    }
    replaceContent("${tabContentId}", "${model.requestUrl}", "editTestResultFormGenerator.html", {collectionNumber: elements[0].innerHTML});
  }

  var testResultsTable = $("#${table_id}").dataTable({
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

</script>

<div id="${tabContentId}">
<br />

<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.collectionNoDisplayName}</th>
			<c:if test="${model.showdateCollected==true}">
				<th>${model.dateCollectedDisplayName}</th>
			</c:if>
			<c:if test="${model.showdateTested==true}">
				<th>${model.dateTestedDisplayName}</th>
			</c:if>
			<c:if test="${model.showhiv==true}">
				<th>${model.hivDisplayName} Reactive</th>
			</c:if>
			<c:if test="${model.showhbv==true}">
				<th>${model.hbvDisplayName} Reactive</th>
			</c:if>
			<c:if test="${model.showhcv==true}">
				<th>${model.hcvDisplayName} Reactive</th>
			</c:if>
			<c:if test="${model.showsyphilis==true}">
				<th>${model.syphilisDisplayName} Reactive</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="testResult" items="${model.allTestResults}">
			<tr>
				<td>${testResult.collectionNumber}</td>
				<c:if test="${model.showdateCollected==true}">
					<td>${testResult.dateCollected}</td>
				</c:if>
				<c:if test="${model.showdateTested==true}">
					<td>${testResult.dateTested}</td>
				</c:if>
				<c:if test="${model.showhiv == true}">
					<td><c:if test="${testResult.hiv =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
				<c:if test="${model.showhbv == true}">
					<td><c:if test="${testResult.hbv =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
				<c:if test="${model.showhcv == true}">
					<td><c:if test="${testResult.hcv =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
				<c:if test="${model.showsyphilis == true}">
					<td><c:if test="${testResult.syphilis =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>