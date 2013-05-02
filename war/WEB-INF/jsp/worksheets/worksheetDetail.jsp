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
		if (collectionNumbers.length == 0)
		  return;
		showLoadingImage($("#${tabContentId}"));
		$.ajax({
		  url: "addCollectionsToWorksheet.html",
		  type: "POST",
		  data: {worksheetId: ${worksheet.id}, collectionNumbers: collectionNumbers},
		  success: function(response) {
		    							$("#${tabContentId}").trigger("refreshWorksheet");
				 							$("#${tabContentId}").replaceWith(response);
								 			showMessage("Collections added successfully to worksheet");
		  				 },
		  error: function(response) {
							 $("#${tabContentId}").replaceWith(response.responseText);
							 showErrorMessage("Error adding collections to worksheet");
		  	     }
		});
	}

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formFormatClass printableArea">
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

		<div class="inputCollectionsSection">
			<div>
				<label style="width: auto;"><b>Add collections to worksheet</b></label>
			</div>
			<div>
				<label style="width: auto;">Scan/type collection numbers and then click the button below to add collections to the worksheet</label>
			</div>
	
			<div>
				<button class="addCollectionsToWorksheetButton">Click here to add collections to worksheet</button>
			</div>
	
			<c:forEach var="counter" begin="${1}" end="${12}">
				<div>
					<label>Collection ${counter}</label>
					<c:if test="${not empty enteredCollectionNumbers and not empty enteredCollectionNumbers[counter-1]}">
						<c:set var="collectionNumber" value="${enteredCollectionNumbers[counter-1]}" />
						<input class="collectionNumberForWorksheetInput"
									 name="collectionNumber-${counter}"
									 value="${collectionNumber}" />
						<c:if test="${not empty invalidCollectionNumbers[collectionNumber]}">
							<label class="formError" style="width: auto;">Invalid collection number</label>
						</c:if>
					</c:if>
					<c:if test="${empty enteredCollectionNumbers or empty enteredCollectionNumbers[counter-1]}">
						<input class="collectionNumberForWorksheetInput" name="collectionNumber-${counter}" />
					</c:if> 
					
				</div>
			</c:forEach>
	
			<div>
				<button class="addCollectionsToWorksheetButton">Click here to add collections to worksheet</button>
			</div>
		</div>
	</div>
</div>