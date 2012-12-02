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
<c:set var="donorsTableEditDonorButtonId">donorsTableEditDonorButton-${unique_page_id}</c:set>
<c:set var="donorsTableDeleteDonorButtonId">donorsTableDeleteDonorButton-${unique_page_id}</c:set>
<c:set var="donorsTableCreateCollectionButtonId">donorsTableCreateCollectionButton-${unique_page_id}</c:set>
<c:set var="donorsTableViewDonorHistoryButtonId">donorsTableViewDonorHistoryButton-${unique_page_id}</c:set>
<c:set var="donorsTableEditRowDivId">donorsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="deleteDonorConfirmDialogId">deleteDonorConfirmDialog-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var donorsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : 'C<"H"lfrT>t<"F"ip>T',
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
            									clearEditSection();
            									$(".rowEditButton").button("enable");
            					        var elements = $(node).children();
            					        if (elements[0].getAttribute("class") === "dataTables_empty") {
            					          return;
            					        }
            					        selectedRowId = elements[0].innerHTML;
          									},
          "fnRowDeselected" : function(node) {
            									clearEditSection();
            									$(".rowEditButton").button("disable");
          									},
        },
        "oColVis" : {
         	"aiExclude": [0,1],
        }
      });

      function createEditSection(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
            				 $("#${donorsTableEditRowDivId}").html(response);
            				 $(".editRowDiv").show();
            				 $('html, body').animate({
            				 		scrollTop: $("#${donorsTableEditRowDivId}").offset().top
            				 }, 700);
            			 }
        });
      }

      function clearEditSection() {
        $("#${donorsTableEditRowDivId}").html("");
        $(".editRowDiv").hide();
      }

    	$(".closeButton").click(clearEditSection);

      $("#${tabContentId}").find(".editDonor").button({disabled: true}).click(function() {
        createEditSection("editDonorFormGenerator.html",
            							{donorId: selectedRowId});
      });

      $("#${tabContentId}").find(".createCollection").button({disabled: true}).click(function() {
        createEditSection("addCollectionFormForDonorGenerator.html",
						{donorId: selectedRowId});
      });

      $("#${tabContentId}").find(".viewDonorHistory").button({disabled: true}).click(function() {
        console.log("view donor history clicked");
      });

      $("#${tabContentId}").find(".deleteDonor").button({disabled: true}).click(function() {
        $("#${deleteDonorConfirmDialogId}").dialog(
            {
              modal : true,
              title : "Confirm Delete",
              buttons : {
                "Delete" : function() {
                  deleteDonor(selectedRowId, reloadCurrentTab);
                  $(this).dialog("close");
                },
                "Cancel" : function() {
                  $(this).dialog("close");
                }
              }
            });
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

			<button class="rowEditButton editDonor">
				Edit Donor
			</button>
			<button class="rowEditButton createCollection">
				Create Collection for Donor
			</button>
			<button class="rowEditButton viewDonorHistory">
				View Donor History
			</button>
			<button class="rowEditButton deleteDonor">
				Delete Donor
			</button>

			<table id="${table_id}" class="dataTable donorsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.donorFields.donorNumber.hidden != true}">
							<th>${model.donorFields.donorNumber.displayName}</th>
						</c:if>
						<c:if test="${model.donorFields.firstName.hidden != true}">
							<th>${model.donorFields.firstName.displayName}</th>
						</c:if>
						<c:if test="${model.donorFields.lastName.hidden != true}">
							<th>${model.donorFields.lastName.displayName}</th>
						</c:if>
						<c:if test="${model.donorFields.gender.hidden != true}">
							<th>${model.donorFields.gender.displayName}</th>
						</c:if>
						<c:if test="${model.donorFields.bloodGroup.hidden != true}">
							<th>${model.donorFields.bloodGroup.displayName}</th>
						</c:if>
						<c:if test="${model.donorFields.birthDate.hidden != true}">
							<th>${model.donorFields.birthDate.displayName}</th>
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

			<button class="rowEditButton editDonor">
				Edit Donor
			</button>
			<button class="rowEditButton createCollection">
				Create Collection for Donor
			</button>
			<button class="rowEditButton viewDonorHistory">
				View Donor History
			</button>
			<button class="rowEditButton deleteDonor">
				Delete Donor
			</button>

		</c:otherwise>
	</c:choose>

	<div class="editRowDiv" style="display: none;">
	<span class="closeButton">X</span>
		<div id="${donorsTableEditRowDivId}">	
		</div>
	</div>

</div>

<div id="${deleteDonorConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Donor?</div>
