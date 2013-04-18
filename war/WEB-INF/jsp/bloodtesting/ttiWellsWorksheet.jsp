<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

  var currentWell = undefined;
  // this object stores all the data entered into the wells
  var ttidata = {};

  for (var i = 1; i <= ${plate.numRows}; ++i) {
    ttidata[i] = {};
    for (var j = 1; j <= ${plate.numColumns}; ++j) {
      ttidata[i][j] = {'contents' : 'empty'};
    }
  }

  function initAllWells() {
    $.each($("#${mainContentId}").find(".wellInput"), function() {
      updateWellColor(this);
      var rowNum = $(this).data("rownum");
      var colNum = $(this).data("colnum");
      ttidata[rowNum][colNum].well = this;
    });
  }

  initAllWells();

  $("#${mainContentId}").find(".wellInput")
  											.click(wellClicked);

  function wellClicked() {
    if (currentWell !== undefined) {
      updateWellDataFromForm(currentWell);
      emptyWellDetailsForm();
      showTestResultInWell(currentWell);
      updateWellColor(currentWell);
      deselectWell(currentWell);
    }
    if (this === currentWell) {
      hideWellDetailsForm();
      currentWell = undefined;
    } else {
	    showWellDetailsForm();
	    focusWellDetailsForm();
	    updateWellColor(this);
	    populateWellDetailsForm(this);
	    selectWell(this);
	    currentWell = this;
    }
  }

  function focusWellDetailsForm() {
    var collectionNumberInput = $("#${mainContentId}").find(".wellDetails")
    																									.find('input[name="collectionNumber"]');
    collectionNumberInput.select();
    collectionNumberInput.focus();
  }

  function showWellDetailsForm() {
    $("#${mainContentId}").find(".wellDetails").show();
  }

  function hideWellDetailsForm() {
    $("#${mainContentId}").find(".wellDetails").hide();
  }
  
  function showTestResultInWell(inputElement) {
    var rowNum = $(inputElement).data("rownum");
    var colNum = $(inputElement).data("colnum");
    if (ttidata[rowNum][colNum].contents === "sample") {
      $(inputElement).val(ttidata[rowNum][colNum].testResult);
    }
  }

  function populateWellDetailsForm(inputElement) {

    var rowNum = $(inputElement).data("rownum");
    var colNum = $(inputElement).data("colnum");

    var wellDetailsForm = $("#${mainContentId}").find(".wellDetails");

    wellDetailsForm.find(".rowNumber").text(String.fromCharCode("A".charCodeAt(0) + rowNum-1));
    wellDetailsForm.find(".columnNumber").text(colNum);
    wellDetailsForm.find('select[name="wellType"]').val(ttidata[rowNum][colNum].welltype);
    wellDetailsForm.find('select[name="wellType"]').change();
    wellDetailsForm.find('input[name="collectionNumber"]').val(ttidata[rowNum][colNum].collectionNumber);
    wellDetailsForm.find('input[name="testResult"]').val(ttidata[rowNum][colNum].testResult);
    wellDetailsForm.find('input[name="machineReading"]').val(ttidata[rowNum][colNum].machineReading);
  }

  $("#${mainContentId}").find('select[name="wellType"]')
  											.change(wellTypeSelectionChanged);

  function wellTypeSelectionChanged() {
    var selectedOption = $(this).find("option:selected");
    // type of data is boolean
    if (selectedOption.data("requiressample") !== true) {
      console.log("here1");
      $("#${mainContentId}").find(".wellDetails")
      											.find(".fieldForWellWithSample").each(function() {
      											  $(this).hide();
      											});
    } else {
      $("#${mainContentId}").find(".wellDetails")
			.find(".fieldForWellWithSample").each(function() {
			  $(this).show();
			});
    }
  }

  function emptyWellDetailsForm() {
    var wellDetailsForm = $("#${mainContentId}").find(".wellDetails");
    wellDetailsForm.find('input[name="collectionNumber"]').val("");
    wellDetailsForm.find('input[name="testResult"]').val("");
    wellDetailsForm.find('input[name="machineReading"]').val("");
  }

  function updateWellDataFromForm(inputElement) {
    var wellDetailsForm = $("#${mainContentId}").find(".wellDetails");
    var wellType = wellDetailsForm.find('select[name="wellType"]').val();
    var collectionNumber = wellDetailsForm.find('input[name="collectionNumber"]').val();
    var testResult = wellDetailsForm.find('input[name="testResult"]').val();
    var machineReading = wellDetailsForm.find('input[name="machineReading"]').val();

    var rowNum = $(inputElement).data("rownum");
    var colNum = $(inputElement).data("colnum");

    ttidata[rowNum][colNum].welltype = wellType;

    if (collectionNumber !== undefined && collectionNumber.length > 0) {
      ttidata[rowNum][colNum].contents = "sample";
      ttidata[rowNum][colNum].welltype = wellType;
      ttidata[rowNum][colNum].collectionNumber = collectionNumber;
      ttidata[rowNum][colNum].testResult = testResult;
      ttidata[rowNum][colNum].machineReading = machineReading;
    } else if (machineReading !== undefined && machineReading.length > 0) {
      // if collection number is not present then we can do nothing with the test result
      // do not store the test result
      ttidata[rowNum][colNum].contents = "other";
      ttidata[rowNum][colNum].welltype = wellType;
      ttidata[rowNum][colNum].machineReading = machineReading;
    }
  }

  function updateWellColor(inputElement) {
    var rowNum = $(inputElement).data("rownum");
    var colNum = $(inputElement).data("colnum");
    if (ttidata[rowNum] === undefined)
      return;
    if (ttidata[rowNum][colNum] === undefined)
      return;
    if (ttidata[rowNum][colNum].contents === undefined)
      return;

    var backgroundColor = "white";
    switch (ttidata[rowNum][colNum].contents) {
    case 'empty': backgroundColor = "white";
    							break;
    case 'sample': backgroundColor = "rgb(0, 100, 44)";
    							 break;
    default: backgroundColor = "rgb(155, 155, 213)";
    							break;
    }
    $(inputElement).css("background", backgroundColor);
  }

  function deselectWell(inputElement) {
    $(inputElement).css("border-color", "#dadada");
    $(inputElement).focusout();
  }

  function selectWell(inputElement) {
    $(inputElement).css("border-color", "red");
  }

  $("#${mainContentId}").find(".wellDetails").find(".moveUpButton")
  											.button({icons: {primary: 'ui-icon-arrowthick-1-n'}})
  											.click(function() {
  											  moveSelectedWell(-1, 0);
  											});
  
  $("#${mainContentId}").find(".wellDetails").find(".moveLeftButton")
												.button({icons: {primary: 'ui-icon-arrowthick-1-w'}})
												.click(function() {
												  moveSelectedWell(0, -1);
												});

  $("#${mainContentId}").find(".wellDetails").find(".moveDownButton")
												.button({icons: {primary: 'ui-icon-arrowthick-1-s'}})
												.click(function() {
												  moveSelectedWell(1, 0);
												});

  $("#${mainContentId}").find(".wellDetails")
  											.find(".moveRightButton")
												.button({icons: {primary: 'ui-icon-arrowthick-1-e'}})
												.click(function() {
													  moveSelectedWell(0, 1);
												});

  function moveSelectedWell(upMovement, rightMovement) {
    if (currentWell === undefined) {
      return;
    }

    var rowNum = $(currentWell).data("rownum") + upMovement;
    var colNum = $(currentWell).data("colnum") + rightMovement;
    if ((rowNum >= 1 && rowNum <= ${plate.numRows}) &&
        (colNum >= 1 && colNum <= ${plate.numColumns}))
      wellClicked.apply(ttidata[rowNum][colNum].well);
  }
  
	$("#${mainContentId}").find(".saveButton")
												.button({icons : {primary: 'ui-icon-plusthick'}}).click(saveTestResults);

	function saveTestResults(eventObj, saveUninterpretableResults) {
	  if (saveUninterpretableResults === undefined)
	  	saveUninterpretableResults = false;
	  if (currentWell !== undefined)
	    wellClicked.apply(currentWell);
	  var bloodTestsData = getDataToSave();
	  console.log(bloodTestsData);

	  console.log(JSON.stringify(bloodTestsData));
	  var dataToSend = {saveUninterpretableResults: saveUninterpretableResults, ttiTestId: ${ttiTestId}, ttiResults: JSON.stringify(bloodTestsData)};
	  console.log(dataToSend);
    saveTestResultsWithConfirmation("saveTTIResultsOnPlate.html", dataToSend, saveBloodTestsSuccess, saveBloodTestsError);
	}

	function getDataToSave() {
	  var bloodTestsData = {};
	  for (var i = 1; i <= ${plate.numRows}; ++i) {
	    for (var j = 1; j <= ${plate.numColumns}; ++j) {
	      if (ttidata[i][j].contents === 'empty')
	        continue;
	      if (bloodTestsData[i] === undefined)
	        bloodTestsData[i] = {};
	      if (bloodTestsData[i][j] === undefined)
	        bloodTestsData[i][j] = {};
	      bloodTestsData[i][j].welltype = ttidata[i][j].welltype;
	      bloodTestsData[i][j].collectionNumber = ttidata[i][j].collectionNumber;
	      bloodTestsData[i][j].testResult = ttidata[i][j].testResult;
	      bloodTestsData[i][j].machineReading = ttidata[i][j].machineReading;
	    }
	  }
	  return bloodTestsData;
	}
	
	function saveBloodTestsSuccess(response) {
	  $("#${tabContentId}").replaceWith(response);
	}

	function saveBloodTestsError(response) {
	  $("#${tabContentId}").replaceWith(response.responseText);
	}

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
		    				 $("#${tabContentId}").replaceWith(response);
		  				 }
		  
		});
	}

	$("#${mainContentId}").find(".changeCollectionsButton")
												.button()
												.click(
							function() {
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

	$("#${mainContentId}").find(".saveUninterpretableResultsButton")
												.button()
												.click(function(event) {
												  saveTestResults(event, true);
												});

});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}" class="formInTabPane">

		<br />
		<br />

		<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
		</c:if>

		<div style="width: 620px;">
			<div class="ttiPlate">
					<input style="width: ${ttiConfig['titerWellRadius']}px;height: ${ttiConfig['titerWellRadius']}px;
										 border-radius: ${ttiConfig['titerWellRadius']}px;
										 text-align: center;
										 background: white;
										 border: none;
										 padding: 0;" disabled="disabled" />

					<c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
						<c:set var="collection" value="${collections[colNum-1]}" />
						<div class="wellBoxHeader">
								<input style="width: ${ttiConfig['titerWellRadius']}px;height: ${ttiConfig['titerWellRadius']}px;
													 border-radius: ${ttiConfig['titerWellRadius']}px;
													 text-align: center;
													 background: rgb(255, 208, 165);
													 color: black;
													 padding: 0;" value="${colNum}" disabled="disabled" />
						 	</div>
					</c:forEach>


				<br />

				<c:forEach var="rowNum" begin="1" end="${plate.numRows}">
						<!-- Top row style similar to input wells -->
						<input style="width: ${ttiConfig['titerWellRadius']}px;height: ${ttiConfig['titerWellRadius']}px;
											 border-radius: ${ttiConfig['titerWellRadius']}px;
											 text-align: center;
											 background: rgb(255, 208, 165);
											 color: black;
											 padding: 0;" value="&#${65 + rowNum-1};" disabled="disabled" />

						<c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
							<div class="wellBox">
								<!-- square around the well -->
								<!-- non-empty wells -->
								<c:set var="testId" value="${bloodTestsOnPlate[rowNum-1].id + 0}" />
							  <c:set var="testResultValue" value="${empty bloodTypingTestResults ? '' : bloodTypingTestResults[collection.id][testId]}" />
							  <c:if test="${not empty errorMap[collection.id][testId]}">
							  	<c:set var="wellBorderColor" value="red" />
						  	</c:if>
							  <c:if test="${empty errorMap[collection.id][testId]}">
							  	<c:set var="wellBorderColor" value="" />
						  	</c:if>
	
								<input
									style="width: ${ttiConfig['titerWellRadius']}px; 
												 height: ${ttiConfig['titerWellRadius']}px;
												 border-radius: ${ttiConfig['titerWellRadius']}px;
												 text-align: center;
												 border-color: ${wellBorderColor};
												 color: white;
												 background: white;
												 padding: 0;
												 "
									data-rownum = "${rowNum}"
									data-colnum = "${colNum}"
									value="${testResultValue}"
									readonly="readonly"
									class="wellInput" />
	
						 	</div>
						</c:forEach>
					<br />
				</c:forEach>
			</div>

			<div style="position: relative; height: 300px;">
				<div class="wellDetails formInTabPane"
						 style="margin-top: 15px; width: 93%; display: none;
						 			  position: relative;">
					<div>
						<label style="width: auto;">Row No.</label>
						<label class="rowNumber" style="width: auto; text-align: left;"></label>
						<label style="width: auto;">Column No.</label>
						<label class="columnNumber" style="width: auto; text-align: left;"></label>
					</div>
					<div>
						<label>Type of well</label>
						<select name="wellType">
							<c:forEach var="wellType" items="${allWellTypes}">
								<option value="${wellType.id}" data-requiressample="${wellType.requiresSample}">${wellType.wellType}</option>
							</c:forEach>
						</select>
					</div>
					<div class="fieldForWellWithSample">
						<label>Collection number</label>
						<input name="collectionNumber" placeholder="Collection number" />
					</div>
					<div class="fieldForWellWithSample">
						<label>Result</label>
						<input name="testResult" placeholder="Allowed results: ${ttiTest.validResults}" />
					</div>
					<div>
						<label>Machine reading</label>
						<input name="machineReading" placeholder="Optical Density"/>
					</div>
					<div style="position: absolute; bottom: 3px;">
						<button class="moveWellButton moveUpButton">Move up</button>
						<button class="moveWellButton moveLeftButton">Move left</button>
						<button class="moveWellButton moveDownButton">Move down</button>
						<button class="moveWellButton moveRightButton">Move right</button>
					</div>
				</div>

				<div style="margin-top: 3px; position: relative; text-align: right;">
					<label></label>
					<button type="button" class="saveButton">
						Save all test results on plate
					</button>
				</div>
			</div>
		</div>

	</div>
</div>