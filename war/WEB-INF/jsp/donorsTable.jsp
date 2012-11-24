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
<c:set var="table_id">donorsTable-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        var fnRowSelected = function(node) {
          var elements = $(node).children();
          console.log(elements);
          if (elements[0].getAttribute("class") === "dataTables_empty") {
            return;
          }
          replaceContent("${tabContentId}", "${model.requestUrl}",
              "editDonorFormGenerator.html", {
                donorId : elements[0].innerHTML
              });
        }

        var donorsTable = $("#${table_id}").dataTable({
          "bJQueryUI" : true,
          "sDom" : 'C<"H"lfrT>t<"F"ip>T',
          "oTableTools" : {
            "sRowSelect" : "single",
            "aButtons" : [ "print" ],
            "fnRowSelected" : fnRowSelected
          },
          "oColVis" : {
           	"aiExclude": [0],
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

		<c:when test="${fn:length(model.allDonors) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>
			<table id="${table_id}" class="dataTable donorsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.donor.donorNumber.hidden != true}">
							<th>${model.donor.donorNumber.displayName}</th>
						</c:if>
						<c:if test="${model.donor.firstName.hidden != true}">
							<th>${model.donor.firstName.displayName}</th>
						</c:if>
						<c:if test="${model.donor.lastName.hidden != true}">
							<th>${model.donor.lastName.displayName}</th>
						</c:if>
						<c:if test="${model.donor.gender.hidden != true}">
							<th>${model.donor.gender.displayName}</th>
						</c:if>
						<c:if test="${model.donor.bloodGroup.hidden != true}">
							<th>${model.donor.bloodGroup.displayName}</th>
						</c:if>
						<c:if test="${model.donor.birthDate.hidden != true}">
							<th>${model.donor.birthDate.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="donor" items="${model.allDonors}">
						<tr>
							<td style="display: none">${donor.id}</td>
							<c:if test="${model.donor.donorNumber.hidden != true}">
								<td>${donor.donorNumber}</td>
							</c:if>
							<c:if test="${model.donor.firstName.hidden != true}">
								<td>${donor.firstName}</td>
							</c:if>
							<c:if test="${model.donor.lastName.hidden != true}">
								<td>${donor.lastName}</td>
							</c:if>
							<c:if test="${model.donor.gender.hidden != true}">
								<td>${donor.gender}</td>
							</c:if>
							<c:if test="${model.donor.bloodGroup.hidden != true}">
								<td>${donor.bloodGroup}</td>
							</c:if>
						<c:if test="${model.donor.birthDate.hidden != true}">
								<td>${donor.birthDate}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>
</div>