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
<c:set var="testResultsTableEditRowDivId">testResultsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="deleteTestResultConfirmDialogId">deleteTestResultConfirmDialog-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var testResultsTable = $("#${table_id}").dataTable({
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
            				 $("#${testResultsTableEditRowDivId}").html(response);
            				 $(".editRowDiv").show();
            				 $('html, body').animate({
            				 		scrollTop: $("#${testResultsTableEditRowDivId}").offset().top
            				 }, 700);
            			 }
        });
      }

      function clearEditSection() {
        console.log("clear");
        $("#${testResultsTableEditRowDivId}").html("");
        $(".editRowDiv").hide();
      }

    	$(".closeButton").click(clearEditSection);

      $("#${tabContentId}").find(".editTestResult").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-pencil'
      			}
          }).click(function() {
        $("#${testResultsTableEditRowDivId}").bind("editTestResultSuccess", refreshResults);
        createEditSection("editTestResultFormGenerator.html",
            							{testResultId: selectedRowId});
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

      $("#${tabContentId}").find(".deleteTestResult").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-trash'
      			}
          }).click(function() {
        $("#${deleteTestResultConfirmDialogId}").dialog(
            {
              modal : true,
              title : "Confirm Delete",
              buttons : {
                "Delete" : function() {
                  deleteTestResult(selectedRowId, refreshResults);
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

		<c:when test="${fn:length(model.allTestResults) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>

			<button class="refreshResults">
				Refresh
			</button>
			<button class="rowEditButton editTestResult">
				Edit Test Result
			</button>
			<button class="rowEditButton deleteTestResult">
				Delete Test Result
			</button>

			<table id="${table_id}" class="dataTable testResultsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.testResultFields.collectionNumber.hidden != true}">
							<th>${model.testResultFields.collectionNumber.displayName}</th>
						</c:if>
						<c:if test="${model.testResultFields.testedOn.hidden != true}">
							<th>${model.testResultFields.testedOn.displayName}</th>
						</c:if>
						<c:if test="${model.testResultFields.bloodTest.hidden != true}">
							<th>${model.testResultFields.bloodTest.displayName}</th>
						</c:if>
						<c:if test="${model.testResultFields.bloodTestResult.hidden != true}">
							<th>${model.testResultFields.bloodTestResult.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="testResult" items="${model.allTestResults}">
						<tr>
							<td style="display: none">${testResult.id}</td>
							<c:if test="${model.testResultFields.collectionNumber.hidden != true}">
								<td>${testResult.collectedSample.collectionNumber}</td>
							</c:if>
							<c:if test="${model.testResultFields.testedOn.hidden != true}">
								<td>${testResult.testedOn}</td>
							</c:if>
							<c:if test="${model.testResultFields.bloodTest.hidden != true}">
								<td>${testResult.bloodTest.name}</td>
							</c:if>
							<c:if test="${model.testResultFields.bloodTestResult.hidden != true}">
								<td>${testResult.bloodTestResult.result}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<button class="refreshResults">
				Refresh
			</button>
			<button class="rowEditButton editTestResult">
				Edit Test Result
			</button>
			<button class="rowEditButton deleteTestResult">
				Delete Test Result
			</button>

		</c:otherwise>
	</c:choose>

	<div class="editRowDiv" style="display: none;">
		<span class="closeButton">X</span>
		<div id="${testResultsTableEditRowDivId}">	
		</div>
	</div>

</div>

<div id="${deleteTestResultConfirmDialogId}" style="display: none;">
	Are	you sure you want to delete this Test Result?
</div>
