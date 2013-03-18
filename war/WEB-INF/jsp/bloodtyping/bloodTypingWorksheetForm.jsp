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
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	$("#${mainContentId}").find(".addCollectionsToPlate").
			button({icons : {primary: 'ui-icon-plusthick'}}).click(
	        function() {
	          var inputs = $("#${mainContentId}").find('input[class="collectionNumberInput"]');
	          var collectionNumbers = [];
	          for (var index = 0; index < inputs.length; index++) {
	            var val = $(inputs[index]).val();
	            collectionNumbers.push(val);
	          }
	          showLoadingImage($("#${tabContentId}"));
	          $.ajax({
	            url: "addCollectionsToBloodTypingPlate.html",
	            data: $.param({collectionNumbers: collectionNumbers}),
	            type: "GET",
	            success: function(response) {
	              				 $("#${tabContentId}").replaceWith(response);
	  					         },
	  					error: function(response) {
	  					  				$("#${tabContentId}").replaceWith(response.responseText);
	  								 }
	          });
	        });

	$("#${mainContentId}").find(".clearFormButton").
	button().click(refetchForm);

	function refetchForm() {
    $.ajax({
      url: "${refreshUrl}",
      data: {},
      type: "GET",
      success: function (response) {
        			 	 $("#${tabContentId}").replaceWith(response);
      				 },
      error:   function (response) {
								 showErrorMessage("Something went wrong. Please try again.");
      				 }
      
    });
  }

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<form class="formInTabPane">
			<b>Enter blood typing results</b>
			<div class="tipsBox ui-state-highlight">
				<p>
					${tips['bloodtyping.plate.step1']}
				</p>
			</div>

			<c:if test="${!empty success && !success}">
				<jsp:include page="../common/errorBox.jsp">
					<jsp:param name="errorMessage" value="${errorMessage}" />
				</jsp:include>
			</c:if>

			<div>
				<label style="width: auto;">
					Enter a maximum of ${plate.numColumns} collection numbers in the order they are placed on the plate
				</label>
			</div>

			<c:if test="${not empty success && !success}">
				<c:forEach var="columnNumber" begin="1" end="${plate.numColumns}">
					<c:set var="collection" value="${collections[columnNumber]}" />
					<div>
						<label>Column ${columnNumber}</label>
						<input name="collectionNumber_${columnNumber}"
									 value="${not empty collection ? collection.collectionNumber : ''}"
									 placeholder="Collection Number"
									 class="collectionNumberInput"
									 />
						<c:if test="${empty collection}">
							<label class="formError" style="width: auto;">Collection does not exist</label>
						</c:if>
					</div>
				</c:forEach>
			</c:if>

			<c:if test="${empty success || success}">
				<c:forEach var="columnNumber" begin="1" end="${plate.numColumns}">
					<div>
						<label>Column ${columnNumber}</label>
						<input name="collectionNumber_${columnNumber}"
									 placeholder="Collection Number"
									 class="collectionNumberInput"
									 />
					</div>
				</c:forEach>
			</c:if>

		</form>

		<div style="margin-left: 200px;">
			<button class="addCollectionsToPlate">
				Proceed to enter test results
			</button>
			<button class="clearFormButton">
				Clear form
			</button>
		</div>

	</div>
</div>