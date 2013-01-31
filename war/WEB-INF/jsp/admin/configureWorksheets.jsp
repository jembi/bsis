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

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".saveButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var worksheetConfigFormData = $("#${mainContentId}").find(".worksheetConfig").serialize();
    $.ajax({
      type : "GET",
      url : "configureWorksheets.html",
      data : worksheetConfigFormData,
      success: function(data) {
        				 showMessage("Worksheet configuration successfully updated.");
				         refetchForm();
      				 },
      error: function(data) {
							 showErrorMessage("Something went wrong. Please try again.");
							 refetchForm();
      			 }
    });
  });

  $("#${mainContentId}").find(".cancelButton").button({
    icons : {
      primary : 'ui-icon-closethick'
    }
  }).click(refetchForm);

  function refetchForm() {
    $.ajax({
      url: "${model.refreshUrl}",
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


});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">
		<div class="formDiv">
			<b>Configure worksheet properties</b>
			<form class="worksheetConfig formInTabPane">
				<div>
					<label>Row height</label>
					<input name="rowHeight" type="number" value="${model.worksheetConfig.rowHeight}" min="10" max="200" />
				</div>
				<div>
					<label>Column width</label>
					<input name="columnWidth" type="number" value="${model.worksheetConfig.columnWidth}" min="10" max="600" />
				</div>
			</form>
			<div>
				<label></label>
				<button class="saveButton">Save</button>
				<button class="cancelButton">Cancel</button>
			</div>
		</div>
	</div>

	<div id="${childContentId}"></div>
</div>