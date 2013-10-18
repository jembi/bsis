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
  $("#${mainContentId}").find(".wellInput").focusout(focusOutOfWell);
  
  function focusOutOfWell() {
    if ($(this).val().length > 0) {
      $(this).addClass("wellWithData");
      var testresult = $(this).val();
      // valid results as an array
     	var validresults = $(this).data("validresults").split(",");
      var valid = false;
      for (var index = 0; index < validresults.length; ++index) {
        if (validresults[index] === testresult) {
          valid = true;
        }
      }
      if (!valid)
        $(this).addClass("wellWithInvalidData");
    }
  }

  $("#${mainContentId}").find(".wellInput").focus(function() {
    if ($(this).val().length > 0) {
      $(this).removeClass("wellWithData");
      $(this).removeClass("wellWithInvalidData");
    }
  });

  $.each($("#${mainContentId}").find(".wellInput"), focusOutOfWell);

  $("#${mainContentId}").find(".saveButton").
  button({icons : {primary: 'ui-icon-plusthick'}}).click(saveTestResults);

  function saveTestResults(eventObj, saveUninterpretableResults) {
    console.log(this);
    console.log(saveUninterpretableResults);
    if (saveUninterpretableResults === undefined)
      saveUninterpretableResults = false;
    var inputs = $("#${mainContentId}").find(".wellInput");
    var data = {};
    for (var index = 0; index < inputs.length; index++) {
      var input = inputs[index];
      var collectionId = $(input).data("collectionid");
      var testId = $(input).data("testid");
      if (data[collectionId] === undefined) {
        data[collectionId] = {};
      }
      data[collectionId][testId] = $(input).val();
    }
    var collectionNumbers = [];
    var collectionNumberStrs = "${collectionNumbers}".split(",");
    for (var index = 0; index < collectionNumberStrs.length; index++) {
      collectionNumbers.push(collectionNumberStrs[index]);
    }
    showLoadingImage($("#${tabContentId}"));
    var bloodTestsData = {bloodTypingTests: JSON.stringify(data),
                          collectionNumbers : collectionNumbers,
                          refreshUrl: "${refreshUrl}",
                          saveUninterpretableResults : saveUninterpretableResults};
    saveTestResultsWithConfirmation("saveBloodTypingTests.html", bloodTestsData, saveBloodTestsSuccess, saveBloodTestsError);
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
  
  document.getElementById('well1').focus(); // onPageLoad - set focus on first well
});

function autoTab(field,fieldID){
	
	if(event.keyCode == 37 && fieldID > 1) {   // left key pressed
		var prevFieldID = fieldID;
		prevFieldID--;
		document.getElementById('well' + prevFieldID).focus();
	}
	
	else if(event.keyCode == 39) {   // right key pressed
		var nextFieldID = fieldID;
		nextFieldID++;
		document.getElementById('well' + nextFieldID).focus();
	}
		
	else if(field.value.length >= 1){ // value entered into current field
		var nextFieldID = fieldID;
		nextFieldID++;
	    document.getElementById('well' + nextFieldID).focus();
	}
	
};


</script>

<div id="${tabContentId}">
  <div id="${mainContentId}" class="formFormatClass">

    <c:if test="${!empty success && !success}">
      <jsp:include page="../common/errorBox.jsp">
        <jsp:param name="errorMessage" value="${errorMessage}" />
      </jsp:include>
    </c:if>

    <!-- Show warning section when blood typing tests are uninterpretable for some collections -->
    <c:if test="${!empty collectionsWithUninterpretableResults && fn:length(collectionsWithUninterpretableResults) gt 0}">
      <div class="warningBox ui-state-highlight">
        <img src="images/warning_icon.png" style="height: 50px;" />
        The following collections have uninterpretable results. Please check the results you have entered.

        <c:forEach var="collectionId" items="${collectionsWithUninterpretableResults}">
  
          <table class="simpleTable">
            <thead>
              <tr>
                <th>Collection number</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  ${collectionsByCollectionId[collectionId].collectionNumber}
                </td>
              </tr>
            </tbody>
          </table>
          <br />

          Are you sure you want to save these results?
          <br />
          <button class="saveUninterpretableResultsButton">
            Save uninterpretable results also
          </button>

        </c:forEach>
      </div>
    </c:if>

    <!-- Show warning section when blood typing tests already added for some collections -->
    <c:if test="${!empty collectionsWithBloodTypingTests && fn:length(collectionsWithBloodTypingTests) gt 0}">
    <div class="warningBox ui-state-highlight">
      <br />
      <img src="images/warning_icon.png" style="height: 50px;" />
      <span class="warningText">
        <b>Warning</b>
        <br />
        The following collections already have blood typing results. Are you sure you want to add blood typing results again?
        <br />
        Previous data will be overwritten.
      </span>

      <br />
      <br />
      <div>
        <button class="changeCollectionsButton">Go back and change collections</button>
      </div>
      <br />
      <br />

      <table class="simpleTable">
        <thead>
          <tr>
            <th>Collection number</th>
            <th>Blood typing status</th>
            <th>Blood ABO</th>
            <th>Blood Rh</th>
            <th>Pending Tests</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="collectionWithTest" items="${collectionsWithBloodTypingTests}">
            <c:set var="collectionId" value="${collectionWithTest.key}" />
            <c:set var="bloodTypingRuleResult" value="${collectionWithTest.value}" />
            <tr>
              <td style="text-align: center;">${collectionMap[collectionId].collectionNumber}</td>
              <td style="text-align: center;">${collectionMap[collectionId].bloodTypingStatus}</td>
              <td style="text-align: center;">${collectionMap[collectionId].bloodAbo}</td>
              <td style="text-align: center;">${collectionMap[collectionId].bloodRh}</td>
              <td>
                <c:forEach var="pendingTestId" items="${bloodTypingRuleResult.pendingBloodTypingTestsIds}">
                  <c:set var="pendingTest" value="${bloodTypingTests[pendingTestId]}" />
                      <li>
                        ${pendingTest.testName}
                      </li>
                </c:forEach>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <br />
      <br />

      </div>
    </c:if>
<table width="100%">
<tr>
<td width="50%">
    <div class="bloodTypingPlate">
        <input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
                   border-radius: ${bloodTypingConfig['titerWellRadius']}px;
                   text-align: center;
                   background: white;
                   border: none;
                   padding: 0;" disabled="disabled" />

        <c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
          <c:set var="collection" value="${collections[colNum-1]}" />
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

	  <c:set var="wellNum" value="0" />
      <c:forEach var="rowNum" begin="1" end="${plate.numRows}">
          <!-- Top row style similar to input wells -->
          <input style="width: ${bloodTypingConfig['titerWellRadius']}px;height: ${bloodTypingConfig['titerWellRadius']}px;
                     border-radius: ${bloodTypingConfig['titerWellRadius']}px;
                     text-align: center;
                     background: rgb(255, 208, 165);
                     color: black;
                     padding: 0;" value="&#${65 + rowNum-1};" disabled="disabled" />
                     	  
          <c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
            <c:set var="collection" value="${collections[colNum-1]}" />
            <div class="wellBox">
              <!-- square around the well -->
              <c:if test="${empty collection or empty bloodTestsOnPlate[rowNum-1]}">
                <!-- show empty wells with disabled input -->
                <input
                  style="width: ${bloodTypingConfig['titerWellRadius']}px; 
                         height: ${bloodTypingConfig['titerWellRadius']}px;
                         border-radius: ${bloodTypingConfig['titerWellRadius']}px;
                         background: rgb(175, 175, 175);
                         text-align: center;
                         padding: 0;
                         "
                  disabled="disabled"/>
              </c:if>
              <c:if test="${not empty collection and not empty bloodTestsOnPlate[rowNum-1]}">
                <!-- non-empty wells -->
                <c:set var="testId" value="${bloodTestsOnPlate[rowNum-1].id + 0}" />
                <c:set var="testResultValue" value="${empty bloodTypingTestResults ? '' : bloodTypingTestResults[collection.id][testId]}" />
                <c:if test="${not empty errorMap[collection.id][testId]}">
                  <c:set var="wellBorderColor" value="red" />
                </c:if>
                <c:if test="${empty errorMap[collection.id][testId]}">
                  <c:set var="wellBorderColor" value="" />
                </c:if>
                <c:set var="wellNum" value="${wellNum + 1}" />
                  <input
                    style="width: ${bloodTypingConfig['titerWellRadius']}px; 
                           height: ${bloodTypingConfig['titerWellRadius']}px;
                           border-radius: ${bloodTypingConfig['titerWellRadius']}px;
                           text-align: center;
                           border-color: ${wellBorderColor};
                           padding: 0;
                           "
                    title="${collection.collectionNumber}"
                    data-validresults="${bloodTestsOnPlate[rowNum-1].validResults}"
                    data-collectionid="${collection.id}"
                    data-testid="${bloodTestsOnPlate[rowNum-1].id}"
                    value="${testResultValue}"
                    class="wellInput" 
                    id="well${wellNum}"
                    onkeyup="autoTab(this,'${wellNum}')"
                    />
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
</td>
<td width="50%">
    <table style="width: 40%" class="simpleTable">
      <thead>
        <tr>
          <th></th>
          <th>Collection Number</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="colNum" begin="${1}" end="${plate.numColumns}">
          <c:set var="collection" value="${collections[colNum-1]}" />
          <tr>
            <td style="text-align: center;">${colNum}</td>
            <td style="text-align: center;">
              <c:if test="${empty collection}">
                EMPTY
              </c:if>
              <c:if test="${not empty collection}">
                ${collection.collectionNumber}
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
</td>
</tr>
</table>

  </div>
</div>