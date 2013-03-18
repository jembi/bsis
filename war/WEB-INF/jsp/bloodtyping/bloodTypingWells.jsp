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

<c:set var="wellPrefixId">wellPrefixId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${mainContentId}").find(".wellInput").focusout(function() {
    if ($(this).val().length > 0) {
    	$(this).css("background", "rgb(137, 230, 178)");
    	$(this).css("border-color", "transparent");
    }
  });

  $("#${mainContentId}").find(".wellInput").focus(function() {
    if ($(this).val().length > 0) {
    	$(this).css("background", "");
    	$(this).css("border-color", "");
    }
  });

  $.each( $("#${mainContentId}").find(".wellInput"),
      function(index, value) {
    		var validResults = $(value).data("validresults").split(",");
    		var validResultsStr = [];
    		for (var i = 0; i < validResults.length; ++i) {
    		  validResultsStr.push("" + validResults[i]);
    		}
    		$(value).autocomplete({
      		source: validResultsStr,
    			minLength: 0,
    			delay: 0
  			}).focus(function() {
          	 if (this.value == "")
              $(this).autocomplete('search', '');
           });
  });

	$("#${mainContentId}").find(".saveButton").
	button({icons : {primary: 'ui-icon-plusthick'}}).click(
      function() {
        console.log("save clicked");
      });

	$("#${mainContentId}").find(".clearFormButton").
	button().click(refetchForm);
	
	function refetchForm() {
		$.ajax({
		  url: "${refreshUrl}",
		  data: {},
		  type: "POST",
		  success: function (response) {
		    			 	 $("#${tabContentId}").replaceWith(response);
		  				 },
		  error:   function (response) {
								 showErrorMessage("Something went wrong. Please try again.");
		  				 }
		  
		});
	}

	$("#${mainContentId}").find(".changeCollectionsButton").button().
	click(function() {
		$.ajax({
		  url: "${changeCollectionsUrl}",
		  data: {},
		  type: "GET",
		  success: function (response) {
		    			 	 $("#${tabContentId}").replaceWith(response);
		  				 },
		  error:   function (response) {
								 showErrorMessage("Something went wrong. Please try again.");
		  				 }
		  
		});	  
	});

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane">
		<div class="bloodTypingPlate">

				<input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
									 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
									 text-align: center;
									 background: white;
									 border: none;
									 padding: 0;" value="${colNum}" disabled="disabled" />

				<c:forEach var="colNum" begin="1" end="${plate.numColumns}">
					<c:set var="collection" value="${collections[colNum]}" />
					<div class="wellBoxHeader">
						<input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
											 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
											 text-align: center;
											 background: rgb(255, 208, 165);
											 color: black;
											 padding: 0;" value="${colNum}" disabled="disabled" title="${not empty collection ? collection.collectionNumber : ''}" />
				 	</div>
				</c:forEach>

			<br />

			<c:forEach var="rowNum" begin="1" end="${plate.numRows}">
					<input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
										 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
										 text-align: center;
										 background: rgb(255, 208, 165);
										 color: black;
										 padding: 0;" value="&#${65 + rowNum-1};" disabled="disabled" />
				<c:forEach var="colNum" begin="1" end="${plate.numColumns}">
					<c:set var="collection" value="${collections[colNum]}" />
					<div class="wellBox">
						<c:if test="${empty collection}">
							<input
								style="width: ${bloodTypingConfig['titerWellRadius']}px; 
											 height: ${bloodTypingConfig['titerWellRadius']}px;
											 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
											 background: rgb(228, 228, 228);
											 text-align: center;
											 padding: 0;
											 "
								disabled="disabled"/>
							</c:if>
							<c:if test="${not empty collection}">
								<input
									style="width: ${bloodTypingConfig['titerWellRadius']}px; 
												 height: ${bloodTypingConfig['titerWellRadius']}px;
												 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
												 text-align: center;
												 padding: 0;
												 "
									title="${not empty collection ? collection.collectionNumber: ''}"
									data-validresults="${bloodTestsOnPlate[rowNum-1].validResults}"
									class="wellInput" />
							</c:if>
					 </div>
				</c:forEach>
				<label style="width: 70px;">${bloodTestsOnPlate[rowNum-1].testNameShort}</label>
				<br />
			</c:forEach>
		</div>

		<div style="margin-left: 200px;">
			<label></label>
			<button type="button" class="saveButton">
				Save
			</button>
			<button type="button" class="changeCollectionsButton">
				Change collections
			</button>
			<button type="button" class="clearFormButton">
				Clear form
			</button>
		</div>

	</div>
</div>