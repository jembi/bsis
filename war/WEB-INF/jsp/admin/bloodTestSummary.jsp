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

<c:set var="deactivateBloodTestConfirmDialogId">deactivateBloodTestConfirmDialogId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".editButton")
  											.button({icons: {primary: 'ui-icon-pencil'}})
  											.click(function() {
  											  console.log("edit button clicked");
  											});

  $("#${mainContentId}").find(".doneButton")
												.button({icons: {primary: 'ui-icon-check'}})
												.click(
												    function() {
												      $("#${tabContentId}").parent().trigger("bloodTestCancel");
												    });

  $("#${mainContentId}").find(".deactivateButton")
  											.button({icons: {primary: 'ui-icon-trash'}})
  											.click(
  											function() {

  											  $("#${deactivateBloodTestConfirmDialogId}").dialog({
  											    modal: true,
  											    title: "Deactivate",
  											    width: "400px",
  											    resizable: false,
  											    buttons: {
  											      "Deactivate": function() {
  											        deactivateBloodTest();
  											        $(this).dialog("close");
  											      },
  											      "Cancel": function() {
  											        $(this).dialog("close");
  											      }
  											    }
  											  });
  											});

  function deactivateBloodTest() {
    $.ajax({
      url: "deactivateBloodTest.html",
      type: "POST",
      data: {bloodTestId : '${bloodTest.id}'},
      success: function() {
        				 showMessage("Blood Test successfully deactivated");
        				 $("#${tabContentId}").parent().trigger("bloodTestEditDone");
      				 },
     	error:   function() {
     	  			   showErrorMessage("Something went wrong. Please try again");
     	  			  	$("#${tabContentId}").parent().trigger("bloodTestEditError");
     					 }
    });
  }
});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<div style="	border: thin solid #1075A1;	border-radius: 5px;	margin: 20px;">

			<div style="margin-left: 20px; padding-top: 10px; font-weight: bold;">Selected blood test</div>

			<div class="summaryPageButtonSection" style="text-align: right;">
				<!-- button class="editButton">Edit</button-->
				<button class="doneButton">Done</button>
				<button class="deactivateButton">Deactivate</button>
			</div>
	
			<div class="ruleDetails">
				<div class="formInTabPane">

					<div>
						<label>Blood test name</label>
						<label>${bloodTest.testName}</label>
					</div>

					<div>
						<label>Blood test short name</label>
						<label>${bloodTest.testNameShort}</label>
					</div>

					<div>
						<label><b>Included in worksheets</b></label>
					</div>

					<c:forEach var="worksheetType" items="${bloodTest.worksheetTypes}">
						<div>
							<label>${worksheetType.worksheetType}</label>
						</div>
					</c:forEach>

				</div>
			</div>

		</div>

	</div>
</div>

<div id="${deactivateBloodTestConfirmDialogId}" style="display: none;">
	Are you sure you want to deactivate this blood test?
</div>
