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
<c:set var="editCompatibilityTestFormId">editCompatibilityTestForm-${unique_page_id}</c:set>
<c:set var="editCompatibilityTestFormCrossmatchTypesId">editCompatibilityTestFormCrossmatchTypes-${unique_page_id}</c:set>
<c:set var="editCompatibilityTestCompatibilityResultId">editCompatibilityTestCompatibilityResultId-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
						// let the parent know we are done
						$("#${tabContentId}").parent().trigger("editCompatibilityTestSuccess");
				}
  
        function notifyParentCancel() {
	        $("#${tabContentId}").parent().trigger("editCompatibilityTestCancel");
        }

        $("#${mainContentId}").find(".saveButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
               addCompatibilityTest($("#${editCompatibilityTestFormId}")[0],
                    									"${tabContentId}", notifyParentSuccess);
            });

        $("#${mainContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(notifyParentCancel);

        $("#${mainContentId}").find(".printButton").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${editCompatibilityTestFormId}").printArea();
        });

        $("#${mainContentId}").find(".compatibilityResult").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${mainContentId}").find(".crossmatchType").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editCompatibilityTestFormId}").find(".compatibilityTestDate").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 1,
          dateFormat : "dd/mm/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        var compatibilityTestDatePicker = $("#${editCompatibilityTestFormId}").find(".compatibilityTestDate");
        compatibilityTestDatePicker.datepicker('setDate', new Date());

        $("#${tabContentId}").find(".clearFormButton").button().click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${model.refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
              			 	 $("#${tabContentId}").replaceWith(response);
              			 	 notifyParentSuccess();
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

        function addCompatibilityTest() {
          var data = $("#${editCompatibilityTestFormId}").serialize();
          showLoadingImage($("#${tabContentId}"));
          $.ajax({
            url: "addCompatibilityTestForRequest.html",
            data: data,
            type: "POST",
            success: function(response) {
              				 notifyParentSuccess();
              				 $("#${tabContentId}").replaceWith(response);
              				 showMessage("Crossmatch Test updated successfully.");
            				 },
          	error: function(response) {
										 $("#${tabContentId}").replaceWith(response.responseText);
										 showErrorMessage("Something went wrong. Please try again.");
          				 }
          });
        }
		});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['requests.addcompatibilityresult']}
			</p>
		</div>
		<form:form method="POST" commandName="editCompatibilityTestForm"
			class="formInTabPane" id="${editCompatibilityTestFormId}">
			<div>
				<label style="width: auto;"><b>Crossmatch Testing for Request Number
				${model.editCompatibilityTestForm.compatibilityTest.forRequest.requestNumber}</b></label>
			</div>
			<div>
			  <form:hidden path="requestNumber" value="${model.editCompatibilityTestForm.compatibilityTest.forRequest.requestNumber}"></form:hidden>
			</div>
				<c:if test="${model.compatibilityTestFields.collectionNumber.hidden != true }">
					<div>
						<form:label path="collectionNumber">${model.compatibilityTestFields.collectionNumber.displayName}</form:label>
						<form:input path="collectionNumber" />
						<form:errors class="formError"
							path="compatibilityTest.collectionNumber" delimiter=", "></form:errors>
						<form:errors class="formError"
							path="compatibilityTest.testedProduct" delimiter=", "></form:errors>
					</div>
				</c:if>
				<c:if test="${model.compatibilityTestFields.compatibilityTestDate.hidden != true }">
					<div>
						<form:label path="compatibilityTestDate">${model.compatibilityTestFields.compatibilityTestDate.displayName}</form:label>
						<form:input path="compatibilityTestDate" class="compatibilityTestDate" />
						<form:errors class="formError" path="compatibilityTest.compatibilityTestDate"
							delimiter=", "></form:errors>
					</div>
				</c:if>
				<c:if test="${model.crossmatchFields.crossmatchType.hidden != true }">
					<div>
						<form:label path="crossmatchType">${model.compatibilityTestFields.crossmatchType.displayName}</form:label>
						<form:select path="crossmatchType" id="${editCompatibilityTestFormCrossmatchTypesId}" class="crossmatchType">
							<form:option value="">&nbsp;</form:option>
							<c:forEach var="crossmatchType" items="${model.crossmatchTypes}">
								<form:option value="${crossmatchType.id}">${crossmatchType.crossmatchType}</form:option>
							</c:forEach>
						</form:select>
						<form:errors class="formError" path="compatibilityTest.crossmatchType"
							delimiter=", "></form:errors>
					</div>
				</c:if>
				<c:if test="${model.compatibilityTestFields.compatibilityResult.hidden != true }">
					<div>
						<form:label path="compatibilityResult">${model.compatibilityTestFields.compatibilityResult.displayName}</form:label>
						<form:select path="compatibilityResult"
							id="${editCompatibilityTestCompatibilityResultId}"
							class="compatibilityResult">
							<form:option value="">&nbsp;</form:option>
							<form:option value="COMPATIBLE">Compatible</form:option>
							<form:option value="NOT_COMPATIBLE">Not compatible</form:option>
						</form:select>
						<form:errors class="formError" path="compatibilityTest.compatibilityResult"
							delimiter=", "></form:errors>
					</div>
				</c:if>
				<c:if test="${model.compatibilityTestFields.notes.hidden != true }">
					<div>
						<form:label path="notes" class="labelForTextArea">${model.compatibilityTestFields.notes.displayName}</form:label>
						<form:textarea path="notes" />
						<form:errors class="formError" path="compatibilityTest.notes"
							delimiter=", "></form:errors>
					</div>
				</c:if>
			</form:form>
	
			<div style="margin-left: 200px;">
				<button type="button" class="saveButton autoWidthButton">
					Save
				</button>
				<button type="button" class="cancelButton autoWidthButton">
					Cancel
				</button>
				<button type="button" class="clearFormButton autoWidthButton">
					Clear form
				</button>				
				<button type="button" class="printButton">
					Print
				</button>
		</div>
	</div>
</div>
