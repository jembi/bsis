<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editDivId">editDiv-${unique_page_id}</c:set>
<c:set var="editDonorContentId">editDonorContent-${unique_page_id}</c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>

<c:set var="table_id">${model.tableName}</c:set>

<script>
  $(document).ready(function() {

    var fnRowSelected = function(node) {
      var elements = $(node).children();
      if (elements[0].getAttribute("class") === "dataTables_empty") {
        return;
      }
      replaceContent("${tabContentId}", "${model.requestUrl}", "editDonorFormGenerator.html", {donorNumber: elements[0].innerHTML});
    }

    var donorsTable = $("#${table_id}").dataTable({
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
	<jsp:include page="addDonorButton.jsp" flush="true" />
	<br /> <br />

	<table id="${table_id}" class="dataTable donorsTable">
		<thead>
			<tr>
				<th>${model.donorIDDisplayName}</th>
				<c:if test="${model.showfirstName==true}">
					<th>${model.firstNameDisplayName}</th>
				</c:if>
				<c:if test="${model.showlastName==true}">
					<th>${model.lastNameDisplayName}</th>
				</c:if>
				<c:if test="${model.showgender==true}">
					<th>${model.genderDisplayName}</th>
				</c:if>
				<c:if test="${model.showbloodType==true}">
					<th>${model.bloodTypeDisplayName}</th>
				</c:if>
				<c:if test="${model.showdateOfBirth==true}">
					<th>${model.dobDisplayName}</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="donor" items="${model.allDonors}">
				<tr>
					<td>${donor.donorNumber}</td>
					<c:if test="${model.showfirstName==true}">
						<td>${donor.firstName}</td>
					</c:if>
					<c:if test="${model.showlastName==true}">
						<td>${donor.lastName}</td>
					</c:if>
					<c:if test="${model.showgender==true}">
						<td>${donor.gender}</td>
					</c:if>
					<c:if test="${model.showbloodType==true}">
						<td>${donor.bloodType}</td>
					</c:if>
					<c:if test="${model.showdateOfBirth==true}">
						<td>${donor.birthDate}</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>