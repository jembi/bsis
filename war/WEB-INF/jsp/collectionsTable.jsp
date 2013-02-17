<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="table_id">collectionsTable-${unique_page_id}</c:set>
<c:set var="collectionsTableEditRowDivId">collectionsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var collectionsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>T',
        "bServerSide" : true,
        "sAjaxSource" : "${model.nextPageUrl}",
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]},
                          { "bSortable" : false, "aTargets": [4, 5]}
        								 ],
        "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
          								 oSettings.jqXHR = $.ajax({
          								   "datatype": "json",
          								   "type": "GET",
          								   "url": sSource,
          								   "data": aoData,
          								   "success": function(jsonResponse) {
          								     						if (jsonResponse.iTotalRecords == 0) {
          								     						  $("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
          								     						}
          								     						fnCallback(jsonResponse);
          								   						}
          								   });
          								 },
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
															$("#${mainContentId}").parent().trigger("collectionSummaryView");
											        var elements = $(node).children();
											        if (elements[0].getAttribute("class") === "dataTables_empty") {
											          return;
											        }
											        selectedRowId = elements[0].innerHTML;
											        createCollectionSummary("collectionSummary.html",
									  							{collectionId: selectedRowId});
 													  },
				"fnRowDeselected" : function(node) {
														},
        },
        "oColVis" : {
         	"aiExclude": [0,1],
        }
      });
      
      function createCollectionSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
            				 $("#${mainContentId}").trigger("collectionSummaryView", response);
            			 }
        });
      }

      function refreshResults() {
        showLoadingImage($("#${mainContentId}"));
        $.ajax({url: "${model.refreshUrl}",
          			type: "GET",
          			success: function(response) {
          			  				 $("#${mainContentId}").html(response);
          							 }
        });
      }

      $("#${mainContentId}").find(".collectionsTable").bind("refreshResults", refreshResults);

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });

      $("#${mainContentId}").find(".saveAsWorksheetFormToggle").button({
        icons: {
          primary: "ui-icon-plusthick"
        }
      }).click(toggleSaveAsWorksheetFormDiv);

      $("#${mainContentId}").find(".cancelSaveAsWorksheetButton").button().click(toggleSaveAsWorksheetFormDiv);

      function toggleSaveAsWorksheetFormDiv() {
        $("#${mainContentId}").find(".saveAsWorksheetFormDiv").toggle("fast");
  		}

      function hideSaveAsWorksheetFormDiv() {
        $("#${mainContentId}").find(".saveAsWorksheetFormDiv").hide();
  		}

      function showSaveAsWorksheetFormDiv() {
        $("#${mainContentId}").find(".saveAsWorksheetFormDiv").show();
  		}

      function getWorksheetBatchIdInput() {
        var worksheetForm = $("#${mainContentId}").find(".saveAsWorksheetForm");
        return worksheetForm.find('input[name="worksheetBatchId"]').val()
      }
      
      $("#${mainContentId}").find(".saveAsWorksheetButton").button().click(
          function() {
            hideSaveAsWorksheetFormDiv();
            $.ajax({
              url: "${model.saveAsWorksheetUrl}",
              data: {worksheetBatchId: getWorksheetBatchIdInput()},
              type: "GET",
              success: function(response) {
                				 showSaveAsWorksheetFormDiv();
                				 var worksheetResult = $("#${mainContentId}").find(".saveWorksheetResult");
                				 worksheetResult.html(response);
                				 showMessage("Successfully saved collections to worksheet.");
              				 },
              error:   function (response) {
                				 showSaveAsWorksheetFormDiv();
                				 var worksheetResult = $("#${mainContentId}").find(".saveWorksheetResult");
                				 worksheetResult.html(response.responseText);
												 showErrorMessage("Something went wrong when trying to generate worksheet.");                
              				 }
            });
          });

      // hide the save as worksheet form for the first time
      hideSaveAsWorksheetFormDiv();
    });
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<c:choose>

			<c:when test="${fn:length(model.allCollections) eq -1}">
				<span style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
					Sorry no results found matching your search request
				</span>
			</c:when>
	
			<c:otherwise>
	
				<br />
				<div>
					<button class="saveAsWorksheetFormToggle">Save collections to worksheet</button>
				</div>
					<div class="saveAsWorksheetFormDiv">
						<div class="formDiv">
							<b>Save Collections to worksheet</b>
							<form class="saveAsWorksheetForm">
								<div>
									<label> Worksheet Batch ID </label>
									<input name="worksheetBatchId" />
								</div>
							</form>
							<div>
								<button class="saveAsWorksheetButton">Save</button>
								<button class="cancelSaveAsWorksheetButton">Cancel</button>
							</div>
							<div class="saveWorksheetResult">
							</div>
						</div>
					</div>
				<br />
	
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
							<c:if test="${model.collectedSampleFields.collectionCenter.hidden != true}">
								<th>${model.collectedSampleFields.collectionCenter.displayName}</th>
							</c:if>
							<c:if test="${model.collectedSampleFields.collectionSite.hidden != true}">
								<th>${model.collectedSampleFields.collectionSite.displayName}</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="collection" items="${model.allProducts}">
							<tr>
								<td style="display: none">${collection.id}</td>
								<c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
									<td>${collection.collectionNumber}</td>
								</c:if>
								<c:if test="${model.collectedSampleFields.collectedOn.hidden != true}">
									<td>${collection.collectedOn}</td>
								</c:if>
								<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true}">
									<td>${collection.bloodBagType.bloodBagType}</td>
								</c:if>
								<c:if test="${model.collectedSampleFields.collectionCenter.hidden != true}">
									<td>${product.collectionCenter}</td>
								</c:if>
								<c:if test="${model.collectedSampleFields.collectionSite.hidden != true}">
									<td>${product.collectionSite}</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
	
			</c:otherwise>
		</c:choose>
	</div>

	<div id="${childContentId}">
	</div>

</div>

<div id="${noResultsFoundDivId}" style="display: none;">
	<span
		style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
		Sorry no results found matching your search request </span>
</div>
