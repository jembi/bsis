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

<c:set var="configureDonorTypesFormId">configureDonorTypes-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addDonorTypeButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureDonorTypesFormId}").find(".donorTypeDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("");
    newDiv.find('input[name="donorTypeName"]').val("");
    $("#${configureDonorTypesFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveDonorTypesButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var donorTypeDivs = $("#${configureDonorTypesFormId}").find(".donorTypeDiv");
    for (var index=0; index < donorTypeDivs.length; index++) {
      var div = $(donorTypeDivs[index]);
      var donorType = div.find('input[name="id"]').val();
      var donorTypeName = div.find('input[name="donorTypeName"]').val();
      console.log(donorType);
      if (donorType == undefined || donorType == null || donorType === "")
        donorType = donorTypeName;
      data[donorType] = donorTypeName;
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureDonorTypes.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
        				 $("#${tabContentId}").replaceWith(response);
        				 showMessage("Donor Types Updated Successfully!");
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
		<b>Configure Donor Types</b>
		<br />
		<br />
		<div class="tipsBox ui-state-highlight">
			<p>
				Modify names of donor types. Add new donor types. 
			</p>
		</div>
		<form id="${configureDonorTypesFormId}">
				<c:forEach var="donorType" items="${model.allDonorTypes}">
					<div class="donorTypeDiv">
						<div>
							<input type="hidden" name="id" value="${donorType.donorType}" />
							<input type="text" name="donorTypeName" value="${donorType.donorTypeName}" />
						</div>
					</div>
			</c:forEach>
		</form>
			<br />
			<div>
				<label>&nbsp;</label>
				<button class="addDonorTypeButton">Add new donor type</button>
				<button class="saveDonorTypesButton">Save</button>
				<button class="cancelButton">Cancel</button>
			</div>

	</div>

	<div id="${childContentId}"></div>

</div>
