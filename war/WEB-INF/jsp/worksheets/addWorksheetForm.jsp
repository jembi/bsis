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
<c:set var="addWorksheetFormId">addWorksheetForm-${unique_page_id}</c:set>
<c:set var="addWorksheetFormWorksheetTypeId">addWorksheetFormWorksheetType-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
						// let the parent know we are done
						$("#${tabContentId}").parent().trigger("editWorksheetSuccess");
				}
  
        function notifyParentCancel() {
	        $("#${tabContentId}").parent().trigger("editWorksheetCancel");
        }

        $("#${mainContentId}").find(".addWorksheetButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
                addNewWorksheet($("#${addWorksheetFormId}")[0],
                    									"${tabContentId}", notifyParentSuccess);
            });

        $("#${addWorksheetFormWorksheetTypeId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${mainContentId}").find(".clearFormButton").button({
          icons : {
            
          }
        }).click(refetchForm);

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

        if ("${disallowDonorChange}" == "true") {
	        $("#${addWorksheetFormId}").find('input[name="donorNumber"]').attr("readonly", "readonly");	
        }

        function updateBarcode(val) {
          if (val === null || val === undefined || val === "")
            val = "-";
	        $("#${addWorksheetFormId}").find(".barcodeContainer").barcode(
						  val,
							"code128",
							{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
        }
        updateBarcode("${addWorksheetForm.worksheet.worksheetNumber}");

        $("#${addWorksheetFormId}").find('input[name="worksheetNumber"]').keyup(function() {
          updateBarcode($(this).val());
        });
        
        if ("${firstTimeRender}" == "true") {
                  	$("#${tabContentId}").find('textarea[name="notes"]').html("${worksheetFields.notes.defaultValue}");
        	setDefaultValueForSelector(getWorksheetTypeSelector(), "${worksheetFields.worksheetType.defaultValue}");
        }

        function getWorksheetTypeSelector() {
          return $("#${tabContentId}").find('select[name="worksheetType"]').multiselect();
        }

		});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">
		<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
		</c:if>
	
		<form:form method="POST" commandName="addWorksheetForm"
			class="formInTabPane" id="${addWorksheetFormId}">
			<c:if test="${!worksheetFields.worksheetNumber.autoGenerate}">
				<c:if test="${worksheetFields.worksheetNumber.hidden != true }">
					<div class="barcodeContainer"></div>
					<div>
						<form:label path="worksheetNumber">${worksheetFields.worksheetNumber.displayName}</form:label>
						<form:input path="worksheetNumber" value="${firstTimeRender ? worksheetFields.worksheetNumber.defaultValue : ''}" />
						<form:errors class="formError"
							path="worksheet.worksheetNumber" delimiter=", "></form:errors>
					</div>
				</c:if>
			</c:if>
			<c:if test="${worksheetFields.worksheetType.hidden != true }">
				<div>
					<form:label path="worksheetType">${worksheetFields.worksheetType.displayName}</form:label>
					<form:select path="worksheetType"
						id="${addWorksheetFormWorksheetTypeId}">
						<form:option value="">&nbsp;</form:option>
						<c:forEach var="worksheetType" items="${worksheetTypes}">
							<form:option value="${worksheetType.id}">${worksheetType.worksheetType}</form:option>
						</c:forEach>
					</form:select>
					<form:errors class="formError" path="worksheet.worksheetType"
						delimiter=", "></form:errors>
				</div>
			</c:if>
	
			<c:if test="${worksheetFields.notes.hidden != true }">
				<div>
					<form:label path="notes" class="labelForTextArea">${worksheetFields.notes.displayName}</form:label>
					<form:textarea path="notes" />
					<form:errors class="formError" path="worksheet.notes"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			</form:form>
	
			<div style="margin-left: 200px;">
				<label></label>
				<button type="button" class="addWorksheetButton autoWidthButton">
					Add Worksheet
				</button>
				<button type="button" class="clearFormButton autoWidthButton">
					Clear form
				</button>				
			</div>
	</div>
	
</div>
