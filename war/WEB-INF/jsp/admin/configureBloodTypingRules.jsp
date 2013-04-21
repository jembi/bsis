<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<c:set var="newBloodTypingRuleDialogId">newBloodTypingRuleDialog-${unique_page_id}</c:set>

<c:set var="subCategorySelectorId">subCategorySelector-${unique_page_id}</c:set>
<c:set var="collectionFieldChangedSelectorId">collectionFieldChanged-${unique_page_id}</c:set>
<c:set var="pendingTestsIdsSelectorId">pendingTestsIdsSelector-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var selectedRowId = undefined;
  
	var bloodTypingTable = $("#${mainContentId}").find(".bloodTypingTestsTable").dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lrC>t<"F"i>',
    "aaSorting" : [],
    "sScrollX": "100%",
    "bPaginate" : false,
    "bSort" : false,
      "oColVis" : {
       	"aiExclude": [0,1],
      }
	});

	new FixedColumns(bloodTypingTable, {"iLeftColumns" : 1, "iRightColumns" : 1});

	$("#${mainContentId}").find(".bloodTypingTestsTable")
												.find("tbody tr")
												.click(bloodTypingRowClicked);

	function bloodTypingRowClicked() {
	  var ruleIdElement = $(this).find(".bloodTypingRuleId");
	  if (ruleIdElement === undefined) {
	    return;
	  }

	  if (selectedRowId !== undefined) {
	    $(this).closest("table")
	    			 .find("tr")
	    			 .each(function() {
	      						 $(this).css("background", "");
	      					 });
	  }

	  var clickedRowId = ruleIdElement[0].innerHTML;
	  if (clickedRowId === selectedRowId) {
	    $("#${mainContentId}").find(".ruleSummarySection").html("");
	    selectedRowId = undefined;
	    return;
	  }

	  $(this).css("background", "#9BB5AA");
	  selectedRowId = clickedRowId;
	  $.ajax({
	    url: "bloodTypingRuleSummary.html",
	    type: "GET",
	    data: {bloodTypingRuleId : selectedRowId},
	    success: function(response) {
	      				 var ruleSummarySection = $("#${mainContentId}").find(".ruleSummarySection");
	      			   animatedScrollTo(ruleSummarySection);
	      			   ruleSummarySection.html(response);
	    				 },
	    error: function(response) {
	      			 showErrorMessage("Something went wrong. Please try again.");
	    			 }
	  })
	}

	$("#${tabContentId}").bind("bloodTypingRuleEditDone", refetchBloodTypingRules);
	$("#${tabContentId}").bind("bloodTypingRuleCancel", function() {
	  $("#${mainContentId}").find(".ruleSummarySection").html("");
	  if (selectedRowId !== undefined) {
	    $("#${mainContentId}").find(".bloodTypingTestsTable")
	    			 .find("tr")
	    			 .each(function() {
	      						 $(this).css("background", "");
	      					 });
	  }
	  selectedRowId = undefined;
	});

	function refetchBloodTypingRules() {
	  showLoadingImage($("#${tabContentId}"));
		refetchContent("${refreshUrl}", $("#${tabContentId}"));
	}

	$("#${mainContentId}").find(".newRuleButton")
												.button({icons: {primary: 'ui-icon-plusthick'}})
												.click(function() {
												  showNewRuleDialog();
												});

  $("#${newBloodTypingRuleDialogId}").find(".subCategory")
  																	 .multiselect({
  														          multiple : false,
  														          selectedList : 1,
  														          header : false
  														        });

  $("#${newBloodTypingRuleDialogId}").find(".newRuleBloodTestPattern")
	 .multiselect({
   multiple : false,
   selectedList : 1,
   header : false
 	});

  $("#${newBloodTypingRuleDialogId}").find(".collectionFieldChanged")
	 .multiselect({
     multiple : false,
     selectedList : 1,
     header : false
   });

  $("#${newBloodTypingRuleDialogId}").find(".pendingTestsIds")
	 .multiselect({
     multiple : true,
     header : true,
     noneSelectedText: 'No extra tests required',
     selectedText: function(numSelected, numTotal, selectedValues) {
								     var checkedValues = $.map(selectedValues, function(input) { return input.title; });
							  	   return checkedValues.length ? checkedValues.join(', ') : 'None';
  					  		 }
  });

  $("#${newBloodTypingRuleDialogId}").find(".pendingTestsIds").multiselect("uncheckAll");

  function showNewRuleDialog() {
		$("#${newBloodTypingRuleDialogId}").dialog({
		  modal: true,
		  title: "New Blood Typing Rule",
		  width: 700,
		  height: 400,
		  maxHeight: 400,
		  buttons: {
		    "Create" : function() {
											var data = getNewBloodTypingRuleData();
											saveNewBloodTypingRule(data);
											$(this).dialog("close");
		    					 },
		    "Cancel" : function() {
											$(this).dialog("close");
		    					 }
		  }
		});

		function getNewBloodTypingRuleData() {
		  var data = {};
		  var newBloodTypingRuleForm = $("#${newBloodTypingRuleDialogId}").find("form");
		  data.subCategory = newBloodTypingRuleForm.find(".subCategory").val();

		  data.pattern = {};
		  var testPatterns = newBloodTypingRuleForm.find(".newRuleBloodTestPattern");
		  for (var index = 0; index < testPatterns.length; ++index) {
		    if ($(testPatterns[index]).val() === "")
		      continue;
		    data.pattern[$(testPatterns[index]).data("testid")] = $(testPatterns[index]).val();
		  }

		  var pendingTestsSelector = newBloodTypingRuleForm.find(".pendingTestsIds"); 
		  data.pendingTestsIds = pendingTestsSelector.multiselect("getChecked").map(function() {
		    return this.value;
		  }).get().join(",");
		  data.collectionFieldChanged = newBloodTypingRuleForm.find(".collectionFieldChanged").val();
		  data.result = newBloodTypingRuleForm.find(".result").val();

		  return data;
		}

		function saveNewBloodTypingRule(data) {
			$.ajax({
			  url: "saveNewBloodTypingRule.html",
			  type: "POST",
			  data: {newBloodTypingRule : JSON.stringify(data)},
			  success: function(response) {
			    				 showMessage("New Blood Typing rule successfully created.");
			    				 $("#${tabContentId}").trigger("bloodTypingRuleEditDone");
			  				 },
			  error:   function() {
			    				 showErrorMessage("Unable to create new blood typing rule.");
			  				 }
			});
		}

	}

});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<div style="margin-left: 20px; margin-right: 20px; margin-top: 50px; border-radius: 5px;">

			<div style="font-weight: bold; margin: 15px;">Blood Typing rules</div>

			<div class="tipsBox ui-state-highlight">
				<p>
					The following rules are used for inferring blood group from the test results.
					You can create new rules. You can also delete rules which are no longer useful or incorrect.
				</p> 
			</div>

			<div>
				<button class="newRuleButton">New blood typing rule</button>
			</div>

			<br />

			<table class="bloodTypingTestsTable">	
				<thead>
					<tr style="height: 30px;">
						<th>Rule #</th>
						<!--  column for rule id should be hidden. It cannot be part of the fixed columns as
									fixed columns form a separate table.
						  -->
						<th style="display: none;"></th>
						<c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
							<th style="width: 20px;">${bloodTypingTest.testName}</th>
						</c:forEach>
						<th style="width: 300px;">
							Result
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="bloodTypingRule" items="${bloodTypingRules}">
						<tr style="cursor: pointer; height: 30px;">
							<td style="text-align: center;">${bloodTypingRule.id}</td>
							<td style="display: none;" class="bloodTypingRuleId">${bloodTypingRule.id}</td>
							<c:set var="ruleNum" value="${ruleNum + 1}" />
							<c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
								<td style="text-align: center; width: 20px;">
								<c:if test="${not empty bloodTypingRule.patternMap[bloodTypingTest.id]}">
									${bloodTypingRule.patternMap[bloodTypingTest.id]}
								</c:if>
								</td>
							</c:forEach>
							<td style="width: 300px; text-align: center;">
								<c:if test="${not empty bloodTypingRule.newInformation}">
									<div>
										<span style="text-align:left; display: inline-block; width: 45%; margin-left: 10px;">
											${bloodTypingRule.collectionFieldChanged}
										</span>
										<span style="text-align: right; display: inline-block; width: 45%; margin-right: 10px;">
											${bloodTypingRule.newInformation}
										</span>
									</div>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div class="ruleSummarySection">
			</div>

		</div>

	</div>

	<div id="${childContentId}">
	</div>

	<div id="${newBloodTypingRuleDialogId}" style="display: none;">
		<form class="formInTabPane">
			<div>
				<label>This is a test for</label>
				<select id="${subCategorySelectorId}"
								name="subCategory" class="subCategory">
					<option value="BLOODABO">
						Blood ABO
					</option>
					<option value="BLOODRH">
						Blood Rh
					</option>
				</select>
			</div>

			<div>
				<label><b>Pattern</b></label>
			</div>

			<c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
				<div>
					<label>${bloodTypingTest.testNameShort}</label>
					<select id="$patternSelector-${bloodTypingTest.id}_${unique_page_id}"
									name="patternInput-${bloodTypingTest.id}_${unique_page_id}"
									class="newRuleBloodTestPattern"
									data-testid="${bloodTypingTest.id}">
						<option value="" />
						<c:forEach var="validResult" items="${bloodTypingTest.validResults}" >
							<option value="${validResult}">${validResult}</option>
						</c:forEach>
					</select>
				</div>
			</c:forEach>

			<div>
				<label>More tests required if rule matches</label>
				<select id="${pendingTestsIdsSelectorId}"
								name="pendingTestsIds-${unique_page_id}" class="pendingTestsIds">
					<c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
						<option value="${bloodTypingTest.id}">${bloodTypingTest.testNameShort}</option>
					</c:forEach>
				</select>
			</div>

			<div>
				<label><b>Result</b></label>
			</div>
			<div>
				<label>
					Result type
				</label>
				<select id="${collectionFieldChangedSelectorId}"
								name="collectionFieldChanged" class="collectionFieldChanged">
					<option value="NOCHANGE">This rule does not generate a final result</option>
					<option value="BLOODABO">Blood ABO</option>
					<option value="BLOODRH">Blood Rh</option>
					<option value="EXTRA">Extra Information</option>
				</select>
			</div>
			<div>
				<label>Result value</label>
				<input type="text" name="result" class="result" />
			</div>

		</form>

	</div>

</div>