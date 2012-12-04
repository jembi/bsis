<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<c:set var="editTestResultFormBarcodeId">editTestResultFormBarcode-${unique_page_id}</c:set>
<c:set var="updateTestResultButtonId">updateTestResultButton-${unique_page_id}</c:set>
<c:set var="bloodTestResultId">bloodTestResultId-${unique_page_id}</c:set>
<c:set var="deleteTestResultButtonId">deleteTestResultButton-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParent() {
						// let the parent know we are done
						$("#${editTestResultFormDivId}").parent().trigger("editTestResultSuccess");
				}

        $("#${updateTestResultButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingTestResult}" == "true")
                updateExistingTestResult($("#${editTestResultFormId}")[0],
                  												 "${editTestResultFormDivId}",
                  												 notifyParent);
              else
                addNewTestResult($("#${editTestResultFormId}")[0],
                    							 "${editTestResultFormDivId}",
                    							 notifyParent);
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
            primary : 'ui-icon-grip-solid-horizontal'
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

        // toggle test result selector based on selection in test result name
        $("#${editTestResultFormId}").find(".editTestNames").change(toggleTestResultSelector);

        function hideAllTestResults() {
          $("#${editTestResultFormId}").find(".testResultsDiv").hide();
        }

        function toggleTestResultSelector() {
          var testNamesSelector = $("#${editTestResultFormId}").find(".editTestNames");
          var testNameVal = testNamesSelector.val();
          hideAllTestResults();
          var testResultDivId = "${bloodTestResultId}-" + testNameVal;
          $("#" + testResultDivId).show();
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
					path="testResult.collectionNumber" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.testResultFields.testedOn.hidden != true }">
			<div>
				<form:label path="testedOn">${model.testResultFields.testedOn.displayName}</form:label>
				<form:input path="testedOn" />
				<form:errors class="formError"
					path="testResult.testedOn" delimiter=", "></form:errors>
			</div>
		</c:if>

		<div>
			<form:label path="name">${model.testResultFields.name.displayName}</form:label>
			<form:select path="name" class="editTestNames">
				<form:option value="" selected="selected">&nbsp;</form:option>
				<c:forEach var="bloodTest" items="${model.bloodTests}">
					<form:option value="${bloodTest.id}">${bloodTest.name}</form:option>
				</c:forEach>
			</form:select>
			<form:errors class="formError"
				path="testResult.name" delimiter=", "></form:errors>
		</div>

		<c:forEach var="bloodTest" items="${model.bloodTests}">
			<div id="${bloodTestResultId-bloodTest.id}" style="display: none;">
				<form:label path="result">${model.testResultFields.result.displayName}</form:label>
				<form:select path="result">
					<form:option value="" selected="selected">&nbsp;</form:option>
						<c:forEach var="allowedResult" items="${bloodTest.allowedResults}">
							<form:option value="${allowedResult.id}">${allowedResult.result}</form:option>
						</c:forEach>
				</form:select>
			</div>
		</c:forEach>
		
		<c:if test="${model.testResultFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.testResultFields.notes.displayName}</form:label>
				<form:textarea path="notes" maxlength="255" />
				<form:errors class="formError" path="testResultFields.notes"
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
			</c:if>

			<button type="button" id="${printButtonId}">
				Print
			</button>
		</div>
	</form:form>
</div>
