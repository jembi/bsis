<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

<script>
  var table_id = "${table_id}";
  var collectionsTable = $("#" + table_id).dataTable({
    "bJQueryUI" : true
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
  $("." + table_id + "Edit").live("click",
      function(event) {

        // remove row_selected class everywhere
        $(collectionsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var collectionId = elements[0].innerHTML;

        generateEditForm("editCollectionFormGenerator.html", {
          collectionNumber : collectionId,
          isDialog : "yes"
        }, updateExistingCollection, "Edit Collection: "
            + elements[1].innerHTML + " " + elements[2].innerHTML,
            'collectionsTable', decorateEditCollectionDialog, 550, 500);
      });

  // we need to invoke the live function here in order for click event to be
  // registered across pages of table
  // http://stackoverflow.com/questions/5985884/jquery-datatables-row-click-not-registering-on-pages-other-than-first
  $("." + table_id + "Delete").live("click",
      function(event) {
        // remove row_selected class everywhere
        $(collectionsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var collectionId = elements[0].innerHTML;
        $("<div> Are you sure you want to delete Collection with Number: " + collectionId + "</div>").dialog({
      			autoOpen : true,
      			height : 150,
      			width : 400,
      			modal : true,
      			title : "Confirm Delete",
      			buttons : {
        				"Delete" : function() {
          									 deleteCollection(collectionId);
          									 $(this).dialog("close");
        									 },
				        "Cancel" : function() {
				          					 $(this).dialog("close");
				        			     }
				      }

		    });
  });  
</script>

<jsp:include page="addCollectionButton.jsp" flush="true" />
<br />
<br />

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
			<th>Actions</th>
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
				<td><span class="ui-icon ui-icon-pencil ${table_id}Edit"
					style="display: inline-block;" title="Edit"
					></span> <span
					class="ui-icon ui-icon-trash ${table_id}Delete"
					style="display: inline-block; margin-left: 10px;" title="Delete"></span>
				</td>
		</c:forEach>
	</tbody>
</table>
