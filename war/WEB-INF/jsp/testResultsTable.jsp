<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

<script>
$(function() {
  var table_id = "${table_id}";
  var testResultsTable = $("#" + table_id).dataTable({
    "bJQueryUI" : true,
    "sDom": '<"H"lfrT>t<"F"ip>T',
    "oTableTools" : {"aButtons": ["print"]}
  });

  $("#${table_id}_filter").find("label").find("input").keyup(function() {
    var searchBox = $("#${table_id}_filter").find("label").find("input");
    $("#" + table_id).removeHighlight();
    if (searchBox.val() != "")
      $("#" + table_id).find("td").highlight(searchBox.val());
  });

  // we need to invoke the live function here in order for click event to be
  // registered across pages of table
  // http://stackoverflow.com/questions/5985884/jquery-datatables-row-click-not-registering-on-pages-other-than-first
  $("." + table_id + "Edit").die("click");
  $("." + table_id + "Edit").live("click",
      function(event) {

        // remove row_selected class everywhere
        $(testResultsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var collectionId = elements[0].innerHTML;

        generateEditForm("editTestResultFormGenerator.html", {
          collectionNumber : collectionId,
          isDialog : "yes"
        }, updateExistingTestResult, "Edit Test Result: "
            + elements[1].innerHTML + " " + elements[2].innerHTML,
            'testResultsTable', decorateEditTestResultDialog, 550, 500);
      });

  // we need to invoke the live function here in order for click event to be
  // registered across pages of table
  // http://stackoverflow.com/questions/5985884/jquery-datatables-row-click-not-registering-on-pages-other-than-first
  $("." + table_id + "Delete").die("click");
  $("." + table_id + "Delete").live("click",
      function(event) {
        // remove row_selected class everywhere
        $(testResultsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var collectionId = elements[0].innerHTML;
        $("<div id='deleteTestResultDialog'> Are you sure you want to delete Test Results for Collection Number: " + collectionId + "</div>").dialog({
      			autoOpen : false,
      			height : 150,
      			width : 400,
      			modal : true,
      			title : "Confirm Delete",
      			buttons : {
        				"Delete" : function() {
          									 deleteTestResult(collectionId);
          									 $(this).dialog("close");
        									 },
				        "Cancel" : function() {
				          					 $(this).dialog("close");
				        			     }
				      },
              close : function() {
                $("#deleteTestResultDialog").remove();
              }
            });
            $("#deleteTestResultDialog").dialog("open");
		    });
  });
</script>

<jsp:include page="addTestResultButton.jsp" flush="true" />
<br />
<br />

<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.collectionNoDisplayName}</th>
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
			<th>Actions</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="testResult" items="${model.allTestResults}">
			<tr>
				<td>${testResult.collectionNumber}</td>
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
				<td><span class="ui-icon ui-icon-pencil ${table_id}Edit"
					style="display: inline-block;" title="Edit"></span> <span
					class="ui-icon ui-icon-trash ${table_id}Delete"
					style="display: inline-block; margin-left: 10px;" title="Delete"></span>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
