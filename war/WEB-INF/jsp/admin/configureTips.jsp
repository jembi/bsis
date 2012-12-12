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

<c:set var="configureTipsFormId">findDonorForm-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".saveTipsButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var tipsDivs = $("#${configureTipsFormId}").find(".tipsDiv");
    for (var index=0; index < tipsDivs.length; index++) {
      var div = $(tipsDivs[index]);
      var tipsKey = div.find('input[name="tipsKey"]').val();
      var tipsContent = div.find('textarea[name="tipsContent"]').val();
      data[tipsKey] = tipsContent;
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureTips.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
        				 $("#${tabContentId}").replaceWith(response);
        				 showMessage("Tips Updated Successfully!");
      				 },
      error: 	 function(response) {
        				 showErrorMessage("Something went wrong. Please try again later");
        				 console.log(response);
      				 },
    });
    return false;
  });

  $("#${tabContentId}").find(".cancelButton").button({
    icons : {
      primary : 'ui-icon-grip-solid-horizontal'
    }
  }).click(refetchForm);
  
  function refetchForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Configure Tips</b>
		<div class="tipsBox ui-state-highlight">
			<p>
				Customize the text of the tips and click on the save button at the bottom of the page when done.
				Click on cancel if you wish to cancel your changes.
			</p>
		</div>
		<br />
		<br />
		<form id="${configureTipsFormId}">
				<c:forEach var="tips" items="${model.allTips}">
					<div class="tipsDiv">
						<div>
							<input type="hidden" name="tipsKey" value="${tips.tipsKey}" />
							<label style="position: relative; bottom: 40px; width: 150px; display: inline-block;">${tips.tipsName}</label>
							<textarea name="tipsContent" rows="4" cols="60">${tips.tipsContent}</textarea>
						</div>
						<br />
					</div>
			</c:forEach>
		</form>
			<br />
			<div>
				<label>&nbsp;</label>
				<button class="saveTipsButton">Save</button>
				<button class="cancelButton">Cancel</button>
			</div>

	</div>

	<div id="${childContentId}"></div>

</div>
