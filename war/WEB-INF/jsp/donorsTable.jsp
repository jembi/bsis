<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

<script>
  var table_id = "${table_id}";
  var donorsTable = $("#" + table_id).dataTable({
    "bJQueryUI" : true,
    "sDom" : 'Rlfrtip'
  });

  $("#" + table_id + " tbody").dblclick(
      function(event) {

        // remove row_selected class everywhere
        $(donorsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var donorId = elements[0].innerHTML;

        generateEditForm("editDonorFormGenerator.html", {
          donorNumber : donorId
        }, updateExistingDonor, "Edit Donor: " + elements[1].innerHTML + " "
            + elements[2].innerHTML, 'donorsTable', decorateEditDonorDialog,
            550, 500);
      });
</script>

<br />
<jsp:include page="addDonorButton.jsp" flush="true" />
<br />
<br />

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
