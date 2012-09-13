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
  
  $("#" + table_id + " tbody").dblclick(
      function(event) {

        // remove row_selected class everywhere
        $(collectionsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var collectionId = elements[0].innerHTML;

        generateEditForm("editCollectionFormGenerator.html", {
          collectionNumber : collectionId
        }, updateExistingCollection, "Edit Collection: " + elements[1].innerHTML + " "
            + elements[2].innerHTML, 'collectionsTable', decorateEditCollectionDialog, 550, 500);
      });
</script>

<br />
<jsp:include page="addCollectionButton.jsp" flush="true" />
<br />
<br />

<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.collectionIDDisplayName}</th>
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
			<c:if test="${model.showdonorNo==true}">
				<th>${model.donorNoDisplayName}</th>
			</c:if>
			<c:if test="${model.showdonorType==true}">
				<th>${model.donorTypeDisplayName}</th>
			</c:if>
			<c:if test="${model.showbloodGroup==true}">
				<th>${model.bloodGroupDisplayName}</th>
			</c:if>
			<c:if test="${model.showsampleNo==true}">
				<th>${model.sampleNoDisplayName}</th>
			</c:if>
			<c:if test="${model.showsampleNo==true}">
				<th>${model.sampleNoDisplayName}</th>
			</c:if>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="collection" items="${model.allCollections}">
			<tr>
				<td>${collection.collectionNumber}</td>
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
				<c:if test="${model.showaddress==true}">
					<td>${donor.address}</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
