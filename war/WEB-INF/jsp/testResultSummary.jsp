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
<c:set var="testResultSummaryBarcodeId">testResultSummaryBarcode-${unique_page_id}</c:set>
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        $("#${testResultSummaryBarcodeId}").barcode(
					  "${model.testResult.collectedSample.collectionNumber}",
						"code128",
						{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("testResultSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
          			primary : 'ui-icon-pencil'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editTestResultSuccess", editTestResultSuccess);
            $("#${tabContentId}").bind("editTestResultCancel", emptyChildContent);

  	        fetchContent("editTestResultFormGenerator.html",
              					 {testResultId: "${model.testResult.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".printButton").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${mainContentId}").printArea();
        });

        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-check'
          }
        }).click(function() {
          notifyParentDone();
        });

        $("#${tabContentId}").find(".deleteButton").button({
          icons : {
            primary : 'ui-icon-trash'
          }
        }).click(function() {
          $("#${deleteConfirmDialogId}").dialog(
              {
                modal : true,
                title : "Confirm Delete",
                buttons : {
                  "Delete" : function() {
                    deleteTestResult("${model.testResult.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editTestResultSuccess() {
          emptyChildContent();
          refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
        }

				function emptyChildContent() {
				  $("#${childContentId}").html("");
				}

      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane">

		<div class="summaryPageButtonSection" style="text-align: right;">
			<button type="button" class="cancelButton">
				Done
			</button>
			<button type="button" class="editButton">
				Edit
			</button>
			<button type="button" class="deleteButton">
				Delete
			</button>
			<button type="button" class="printButton">
				Print
			</button>
		</div>

		<br />
		<br />

		<div id="${testResultSummaryBarcodeId}"></div>

		<c:if test="${model.testResultFields.collectionNumber.hidden != true }">
			<div>
				<label>${model.testResultFields.collectionNumber.displayName}</label>
				<label>${model.testResult.collectedSample.collectionNumber}</label>
			</div>
		</c:if>
		<c:if test="${model.testResultFields.testedOn.hidden != true }">
			<div>
				<label>${model.testResultFields.testedOn.displayName}</label>
				<label style="width: auto;">${model.testResult.testedOn}</label>
			</div>
		</c:if>
		<c:if test="${model.testResultFields.bloodTest.hidden != true }">
			<div>
				<label>${model.testResultFields.bloodTest.displayName}</label>
				<label>${model.testResult.bloodTest.name}</label>
			</div>
		</c:if>
		<c:if test="${model.testResultFields.bloodTestResult.hidden != true }">
			<div>
				<label>${model.testResultFields.result.displayName}</label>
				<label>${model.testResult.result}</label>
			</div>
		</c:if>
	</div>

	<hr />
	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Test Result?</div>
