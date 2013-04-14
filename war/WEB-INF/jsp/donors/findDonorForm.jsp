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

<c:set var="findDonorFormId">findDonorForm-${unique_page_id}</c:set>
<c:set var="findDonorFormBloodGroupSelectorId">findDonorFormBloodGroupSelector-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".findDonorButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findDonorFormData = $("#${findDonorFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findDonorResults");
    $.ajax({
      type : "GET",
      url : "findDonor.html",
      data : findDonorFormData,
      success : function(data) {
        animatedScrollTo(resultsDiv);
        resultsDiv.html(data);
      }
    });
  });

  $("#${tabContentId}").find(".clearFindFormButton").button({
    icons : {
      
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

  $("#${findDonorFormBloodGroupSelectorId}").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    },
    noneSelectedText: 'None Selected',
    selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  $("#${findDonorFormId}").find(".anyBloodGroupInput")
										  												.val("true");
										  return "Any Blood Group";
										}
										else {
										  $("#${findDonorFormId}").find(".anyBloodGroupInput")
																							.val("false");
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Blood Group';
										}
										  
    							}
  });

  $("#${findDonorFormBloodGroupSelectorId}").multiselect("checkAll");

  // child div shows donor information. bind this div to donorView event
  $("#${tabContentId}").bind("donorSummaryView",
      function(event, content) {
    		$("#${mainContentId}").hide();
    		$("#${childContentId}").html(content);
  		});

  $("#${tabContentId}").bind("donorSummarySuccess",
      function(event, content) {
    		$("#${mainContentId}").show();
    		$("#${childContentId}").html("");
    		$("#${tabContentId}").find(".donorsTable").trigger("refreshResults");
  		});

});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Find Donors</b>
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['donors.finddonor']}
			</p>
		</div>
		<form:form method="GET" commandName="findDonorForm" id="${findDonorFormId}"
			class="formInTabPane">
			<div>
				<form:label path="donorNumber">${model.donorFields.donorNumber.displayName}</form:label>
				<form:input path="donorNumber" />
			</div>
			<div>
				<form:label path="firstName">${model.donorFields.firstName.displayName}</form:label>
				<form:input path="firstName" />
			</div>
			<div>
				<form:label path="lastName">${model.donorFields.lastName.displayName}</form:label>
				<form:input path="lastName" />
			</div>
			<div>
				<form:label path="bloodGroups">${model.donorFields.bloodGroup.displayName}</form:label>
				<form:hidden path="anyBloodGroup" class="anyBloodGroupInput" value="true" />
				<form:select path="bloodGroups" id="${findDonorFormBloodGroupSelectorId}">
					<form:option value="Unknown" label="Unknown" />
					<form:option value="A+" label="A+" />
					<form:option value="A-" label="A-" />
					<form:option value="B+" label="B+" />
					<form:option value="B-" label="B-" />
					<form:option value="AB+" label="AB+" />
					<form:option value="AB-" label="AB-" />
					<form:option value="O+" label="O+" />
					<form:option value="O-" label="O-" />
				</form:select>
			</div>
		</form:form>

		<div class="formInTabPane">
			<div>
				<label></label>
				<button type="button" class="findDonorButton">
					Find donor
				</button>
				<button type="button" class="clearFindFormButton">
					Clear form
				</button>
			</div>
		</div>
		<div class="findDonorResults"></div>
	</div>

	<div id="${childContentId}"></div>

</div>
