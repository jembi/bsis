<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editTestResultFormDivId">editTestResultFormDiv-${unique_page_id}</c:set>
<c:set var="editTestResultFormId">editTestResultForm-${unique_page_id}</c:set>

<c:set var="editTestResultFormBloodTestSelectorId">editTestResultFormBloodTestSelectorId-${unique_page_id}</c:set>
<c:set var="editTestResultFormResultSelectorId">editTestResultFormResultSelectorId-${unique_page_id}</c:set>

<c:set var="updateTestResultButtonId">updateTestResultButton-${unique_page_id}</c:set>
<c:set var="bloodTestResultId">bloodTestResultId-${unique_page_id}</c:set>
<c:set var="deleteTestResultButtonId">deleteTestResultButton-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>
<c:set var="cancelButtonId">cancelButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
					 // let the parent know we are done
				   $("#${editTestResultFormDivId}").parent().trigger("editTestResultSuccess");
   		}

       function notifyParentCancel() {
					 // let the parent know we are done
				   $("#${editTestResultFormDivId}").parent().trigger("editTestResultCancel");
	   		}

        $("#${cancelButtonId}").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(
	           function() {
               notifyParentCancel();
        });

        $("#${updateTestResultButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingTestResult}" == "true")
                updateExistingTestResult($("#${editTestResultFormId}")[0],
                  												 "${editTestResultFormDivId}",
                  												 notifyParentSuccess);
              else
                addNewTestResult($("#${editTestResultFormId}")[0],
                    							 "${editTestResultFormDivId}",
                    							 notifyParentSuccess);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${editTestResultFormId}").printArea();
        });

        $("#${editTestResultFormId}").find(".clearFormButton").button({
          icons : {
            
          }
        }).click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${model.refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
              			 	 $("#${editTestResultFormDivId}").replaceWith(response);
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

        $("#${editTestResultFormId}").find(".testedOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        var testedOnDatePicker = $("#${editTestResultFormId}").find(".testedOn");
        if ("${model.existingTestResult}" == "false" && testedOnDatePicker.val() == "") {
          testedOnDatePicker.datepicker('setDate', new Date());
        }

        $("#${editTestResultFormId}").find(".editTestNames").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        // toggle test result selector based on selection in test result name
        $("#${editTestResultFormId}").find(".editTestNames").change(toggleTestResultSelector);

        if ("${model.existingTestResult}" == "true") {
          $("#${bloodTestResultId}-" + "${model.editTestResultForm.bloodTest}".replace(" ", "")).
          			find("select").val("${model.editTestResultForm.result}");
          console.log("#${bloodTestResultId}-" + "${model.editTestResultForm.bloodTest}".replace(" ", ""));
        }
        
        // trigger this event for the first time
        $("#${editTestResultFormId}").find(".editTestNames").trigger("change");

        console.log("${model.editTestResultForm.bloodTest}");
        console.log("${model.editTestResultForm.result}");

        function hideAllTestResults() {
          $("#${editTestResultFormId}").find(".testResultsDiv").each(function() {
          	$(this).find("select").multiselect("destroy");
          	// need to set the disabled property to prevent multiple values
          	// for bloodTestResult to be submitted.
          	// enable it only for the select required.
          	$(this).find("select").attr("disabled", "disabled");
          	$(this).hide();
	        });
        }

        function showTestResultsSelector(testResultDivId) {
          $("#" + testResultDivId).show();
          // remove the disabled property exclusively from this attribute
          $("#" + testResultDivId).find("select").removeAttr("disabled");
          $("#" + testResultDivId).find("select").multiselect({
            multiple : false,
            selectedList : 1,
            header : false
          });          
        }

        function toggleTestResultSelector() {
          var testNamesSelector = $("#${editTestResultFormId}").find(".editTestNames");
          var testNameVal = testNamesSelector.val().replace(" ", "");
          hideAllTestResults();
          var testResultDivId = "${bloodTestResultId}-" + testNameVal;
          console.log(testResultDivId);
          showTestResultsSelector(testResultDivId);
        }
        
        copyMirroredFields("${editTestResultFormId}", JSON.parse('${model.testResultFields.mirroredFields}'));

      });
</script>

<div id="${editTestResultFormDivId}">

	<form:form method="POST" commandName="editTestResultForm"
		class="formInTabPane" id="${editTestResultFormId}">
		<form:hidden path="id" />
		<c:if test="${model.testResultFields.collectionNumber.hidden != true }">
			<div>
				<form:label path="collectionNumber">${model.testResultFields.collectionNumber.displayName}</form:label>
				<form:input path="collectionNumber" />
				<form:errors class="formError"
					path="testResult.collectedSample" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.testResultFields.testedOn.hidden != true }">
			<div>
				<form:label path="testedOn">${model.testResultFields.testedOn.displayName}</form:label>
				<form:input path="testedOn" class="testedOn" />
				<form:errors class="formError"
					path="testResult.testedOn" delimiter=", "></form:errors>
			</div>
		</c:if>

		<div>
			<form:label path="bloodTest">${model.testResultFields.bloodTest.displayName}</form:label>
			<form:select path="bloodTest"
									 id="${editTestResultFormBloodTestSelectorId}"
									 class="editTestNames">
				<form:option value="" selected="selected">&nbsp;</form:option>
				<c:forEach var="bloodTest" items="${model.bloodTests}">
					<form:option value="${bloodTest.name}">${bloodTest.name}</form:option>
				</c:forEach>
			</form:select>
			<form:errors class="formError"
				path="testResult.bloodTest" delimiter=", "></form:errors>
		</div>

		<c:forEach var="bloodTest" items="${model.bloodTests}">
			<div id="${bloodTestResultId}-${fn:replace(bloodTest.name, ' ', '')}" class="testResultsDiv" style="display: none;">
				<form:label path="result">${model.testResultFields.result.displayName}</form:label>
				<form:select path="result"
										 id="${editTestResultFormResultSelectorId}">
					<!-- Auto incremented IDs begin with zero -->
					<form:option value="-1" selected="selected">&nbsp;</form:option>
						<c:forEach var="allowedResult" items="${bloodTest.allowedResults}">
							<form:option value="${allowedResult}">${allowedResult}</form:option>
						</c:forEach>
				</form:select>
				<form:errors class="formError"
				path="testResult.result" delimiter=", "></form:errors>
			</div>
		</c:forEach>
		
		<c:if test="${model.testResultFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.testResultFields.notes.displayName}</form:label>
				<form:textarea path="notes" maxlength="255" />
				<form:errors class="formError" path="testResult.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>

		<div>
			<label></label>
			<c:if test="${!(model.existingTestResult)}">
				<button type="button" id="${updateTestResultButtonId}">
					Save and add another
				</button>
				<button type="button" class="clearFormButton">
					Clear form
				</button>
			</c:if>
			<c:if test="${model.existingTestResult}">
				<button type="button" id="${updateTestResultButtonId}"
								class="autoWidthButton">Save</button>
				<button type="button" id="${cancelButtonId}">
					Cancel
				</button>
			</c:if>

			<button type="button" id="${printButtonId}">
				Print
			</button>
		</div>
	</form:form>
</div>
