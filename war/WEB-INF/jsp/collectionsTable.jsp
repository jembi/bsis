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
<c:set var="collectionsTableEditRowDivId">collectionsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="deleteCollectionConfirmDialogId">deleteCollectionConfirmDialog-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var collectionsTable = $("#${table_id}").dataTable({
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
            				 $("#${collectionsTableEditRowDivId}").html(response);
            				 $(".editRowDiv").show();
            				 $('html, body').animate({
            				 		scrollTop: $("#${collectionsTableEditRowDivId}").offset().top
            				 }, 700);
            			 }
        });
      }

      function clearEditSection() {
        console.log("clear");
        $("#${collectionsTableEditRowDivId}").html("");
        $(".editRowDiv").hide();
      }

    	$(".closeButton").click(clearEditSection);

      $("#${tabContentId}").find(".editCollection").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-pencil'
      			}
          }).click(function() {
        $("#${collectionsTableEditRowDivId}").bind("editCollectionSuccess", refreshResults);
        createEditSection("editCollectionFormGenerator.html",
            							{collectionId: selectedRowId});
      });

      function refreshResults() {
        showLoadingImage("${tabContentId}");
        $.ajax({url: "${model.refreshUrl}",
          			data: {},
          			type: "GET",
          			success: function(response) {
          			  				 $("#${tabContentId}").html(response);
          							 }
        });
      }

      $("#${tabContentId}").find(".refreshResults").button({
        icons : {
          primary : 'ui-icon-arrowrefresh-1-e'
        }
      }).click(refreshResults);

      $("#${tabContentId}").find(".createProduct").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-circle-zoomin'
      			}
          }).click(function() {
        $("#${collectionsTableEditRowDivId}").bind("editCollectionSuccess", clearEditSection);
        createEditSection("addCollectionFormForCollectionGenerator.html",
						{collectionId: selectedRowId});
      });

      $("#${tabContentId}").find(".viewCollectionHistory").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-info'
      			}
          }).click(function() {
        console.log("view collection history clicked");
      });

      $("#${tabContentId}").find(".deleteCollection").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-trash'
      			}
          }).click(function() {
        $("#${deleteCollectionConfirmDialogId}").dialog(
            {
              modal : true,
              title : "Confirm Delete",
              buttons : {
                "Delete" : function() {
                  deleteCollection(selectedRowId, refreshResults);
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

		<c:when test="${fn:length(model.allCollectedSamples) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>

			<button class="refreshResults">
				Refresh
			</button>
			<button class="rowEditButton editCollection">
				Edit Collection
			</button>
			<button class="rowEditButton createProduct">
				Create Product for Collection
			</button>
			<button class="rowEditButton deleteCollection">
				Delete Collection
			</button>

			<table id="${table_id}" class="dataTable collectionsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
							<th>${model.collectedSampleFields.collectionNumber.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.donor.hidden != true}">
							<th>${model.collectedSampleFields.donor.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.donorType.hidden != true}">
							<th>${model.collectedSampleFields.donorType.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.collectionSite.hidden != true}">
							<th>${model.collectedSampleFields.collectionSite.displayName}</th>
						</c:if>
						<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true}">
							<th>${model.collectedSampleFields.bloodBagType.displayName}</th>
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
							<c:if test="${model.collectedSampleFields.donor.hidden != true}">
								<td>${collectedSample.donor}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.donorType.hidden != true}">
								<td>${collectedSample.donorType}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.collectionSite.hidden != true}">
								<td>${collectedSample.collectionSite}</td>
							</c:if>
							<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true}">
								<td>${collectedSample.bloodBagType}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<button class="refreshResults">
				Refresh
			</button>
			<button class="rowEditButton editCollection">
				Edit Collection
			</button>
			<button class="rowEditButton createProduct">
				Create Product for Collection
			</button>
			<button class="rowEditButton deleteCollection">
				Delete Collection
			</button>

		</c:otherwise>
	</c:choose>

	<div class="editRowDiv" style="display: none;">
		<span class="closeButton">X</span>
		<div id="${collectionsTableEditRowDivId}">	
		</div>
	</div>

</div>

<div id="${deleteCollectionConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Collection?</div>
