<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	$("#${mainContentId}").find(".addCollectionsToWorksheetButton")
												.button({icons : {primary : "ui-icon-plusthick"}})
												.click(addCollectionsToWorksheet);

	function addCollectionsToWorksheet() {
		var inputs = $("#${mainContentId}").find(".collectionNumberForWorksheetInput");
		var collectionNumbers = [];
		for (var index = 0; index < inputs.length; ++index) {
		  var input = $(inputs[index]);
		  var value = input.val();
		  if (value != undefined && value.length > 0) {
		    collectionNumbers.push(value);
		  }
		}
		console.log(collectionNumbers);
	}

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane printableArea">
		<br />
		<div class="worksheetBarcode"></div>

		<c:if test="${worksheetFields.worksheetNumber.hidden != true }">
			<div>
				<label>${worksheetFields.worksheetNumber.displayName}</label>
				<label>${worksheet.worksheetNumber}</label>
			</div>
		</c:if>
		<c:if test="${worksheetFields.worksheetType.hidden != true }">
			<div>
				<label>${worksheetFields.worksheetType.displayName}</label>
				<label>${worksheet.worksheetType}</label>
			</div>
		</c:if>
		<c:if test="${worksheetFields.notes.hidden != true }">
			<div>
				<label>${worksheetFields.notes.displayName}</label>
				<label>${worksheet.notes}</label>
			</div>
		</c:if>
		<div>
			<label>${worksheetFields.lastUpdatedTime.displayName}</label>
			<label style="width: auto;">${worksheet.lastUpdated}</label>
		</div>
		<div>
			<label>${worksheetFields.lastUpdatedBy.displayName}</label>
			<label style="width: auto;">${worksheet.lastUpdatedBy}</label>
		</div>
		<hr />

		<div>
			<label style="width: auto;"><b>Add collections to worksheet</b></label>
		</div>
		<div>
			<label style="width: auto;">Scan/type collection numbers</label>
		</div>

		<c:forEach var="counter" begin="${1}" end="${12}">
			<div>
				<label>Collection ${counter}</label>
				<input class="collectionNumberForWorksheetInput" name="collectionNumber-${counter}" /> 
			</div>
		</c:forEach>

		<div>
			<button class="addCollectionsToWorksheetButton">Add collections to worksheet</button>
		</div>

	</div>
</div>