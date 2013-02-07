<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>

<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDivId-${unique_page_id}</c:set>
<c:set var="editableFieldsForTableId">editableFieldsForTableId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  function getWorksheetForTestResultsTable() {
    return $("#${mainContentId}").find(".worksheetForTestResultsTable");
  }

  console.log("${model.nextPageUrl}");

  var testResultsTable = getWorksheetForTestResultsTable().dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lrT>t<"F"ip>T',
    "bServerSide" : true,
    "bSort" : false,
    "sAjaxSource" : "${model.nextPageUrl}",
    "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]},
    								 ],
    "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
											 console.log("here");
      								 oSettings.jqXHR = $.ajax({
      								   "datatype": "json",
      								   "type": "GET",
      								   "url": sSource,
      								   "data": aoData,
      								   "success": function(jsonResponse) {
      								     						makeRowsEditable(jsonResponse.aaData);
      								     						fnCallback(jsonResponse);
      								     						getWorksheetForTestResultsTable().find("tr").css("min-height", "200px");
      								     						if (jsonResponse.iTotalRecords == 0) {
      								     						  $("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
      								     						}
      								   						}
      								   });
      								 },
    "oTableTools" : {
      "sRowSelect" : "single",
      "aButtons" : [],
      "fnRowSelected" : rowSelectEdit,
			"fnRowDeselected" : rowDeselectDisableEdit,
    },
  });
  
  function makeRowsEditable(data) {
		for (var index in data) {
		  var row = data[index];
		  row[1] = getEditableCollectionNumber(row[1]);
		  row[2] = getEditableTestedOn(row[2]);
		  row[3] = getEditableBloodABOSelector(row[3]);
		  row[4] = getEditableBloodRhSelector(row[4]);
		}
  }

  function getEditableCollectionNumber(cell) {
    return '<span style="width: 50px;">' + cell + '</span>';
  }

  function getEditableTestedOn(cell) {
    var inputElement = '<input class="testedOn inlineInput" value="' + cell + '" />';
    var rowContents = '<div class="editableField testedOnEditableField" style="display: none;">' + inputElement + '</div>' +
    									'<div class="viewableField testedOnViewField">' + cell + '</div>';
		return rowContents;
  }

  function getEditableBloodABOSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableBloodABOField")[0].outerHTML;
    console.log(selectElement);
    var rowContents = '<div class="editableField bloodABOEditableField" style="display: none;">' + selectElement + '</div>' +
											'<div class="viewableField bloodABOViewField">' + cell + '</div>';
		return rowContents;
  }
  
  function getEditableBloodRhSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableBloodRhField")[0].outerHTML;
    var rowContents = '<div class="editableField bloodRHEditableField" style="display: none;">' + selectElement + '</div>' +
											'<div class="viewableField bloodRHViewField">' + cell + '</div>';
		return rowContents;
  }
  
  function rowSelectEdit(node) {
    var elements = $(node).children();
    if (elements[0].getAttribute("class") === "dataTables_empty")
      return;
    var selectedRowId = elements[0].innerHTML;

    $(node).find(".viewableField").hide();
    $(node).find(".editableField").show();

    if ("${model.worksheetConfig['collectionNumber']}" == "true") {
      console.log(elements[1]);
    }
    if ("${model.worksheetConfig['testedOn']}" == "true") {
      console.log(elements[2]);
    }
    if ("${model.worksheetConfig['Blood ABO']}" == "true") {
      console.log(elements[3]);
    }
    if ("${model.worksheetConfig['Blood Rh']}" == "true") {
      console.log(elements[4]);
    }
    if ("${model.worksheetConfig['HBV']}" == "true") {
      console.log(elements[5]);
    }
    if ("${model.worksheetConfig['HCV']}" == "true") {
      console.log(elements[6]);
    }
    if ("${model.worksheetConfig['HIV']}" == "true") {
      console.log(elements[7]);
    }
    if ("${model.worksheetConfig['Syphilis']}" == "true") {
      console.log(elements[8]);
    }
  }

  function rowDeselectDisableEdit(node) {
    getWorksheetForTestResultsTable().find(".editableField").hide();
    $(node).find(".viewableField").show();
  }
  
});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<c:if test="${!model.worksheetFound}">
			<span style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
					No worksheet found.
			</span>
		</c:if>
		<c:if test="${model.worksheetFound}">
		
			<br />
			<br />

			<div class="printableArea">

				<div style="margin-top: 20px; margin-bottom: 20px; font-size: 18pt;">Worksheet ID: ${model.worksheetBatchId}</div>
			
				<table class="dataTable worksheetForTestResultsTable">
					<thead>
						<tr style="width: 50px;">
								<th style="display: none"></th>
								<c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
									<th>
										Collection Number
									</th>
								</c:if>

							  <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
									<th>
										Tested On
									</th>
								</c:if>

								<c:forEach var="bloodTest" items="${model.bloodTests}">
								  <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
										<th>
											${bloodTest.name}
										</th>
									</c:if>
								</c:forEach>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="collectedSample" items="${model.allCollectedSamples}">
							<tr>
								<td style="display: none">${collectedSample.id}</td>
							  <c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
									<td>
										${collectedSample.collectionNumber}
									</td>
								</c:if>
							  <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
									<td></td>
							  </c:if>
								<c:forEach var="bloodTest" items="${model.bloodTests}">
								  <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
										<td></td>
									</c:if>
							</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
	<div id="${childContentId}"></div>
</div>

<div id="${noResultsFoundDivId}" style="display: none;">
	<span
		style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
		Sorry no results found matching your search request </span>
</div>

<div id="${editableFieldsForTableId}" style="display: none;">
	<c:forEach var="bloodTest" items="${model.bloodTests}">
		<select class="editable${fn:replace(bloodTest.name, ' ', '')}Field inlineSelect">
			<c:forEach var="allowedResult" items="${bloodTest.allowedResults}">
				<option value="${allowedResult}" label="${allowedResult}" />
			</c:forEach>
		</select>
	</c:forEach>
</div>