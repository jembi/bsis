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
<c:set var="findDonorFormDivId">findDonorFormDiv-${unique_page_id}</c:set>
<c:set var="findDonorFormId">findDonorForm-${unique_page_id}</c:set>
<c:set var="findDonorFormBloodGroupSelectorId">findDonorFormBloodGroupSelector-${unique_page_id}</c:set>
<c:set var="findDonorFormResultId">findDonorFormResult-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${findDonorFormDivId}").find(".findDonorButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findDonorFormData = $("#${findDonorFormId}").serialize();
    showLoadingImage('${findDonorFormResultId}');
    $.ajax({
      type : "GET",
      url : "findDonor.html",
      data : findDonorFormData,
      success : function(data) {
        $('#${findDonorFormResultId}').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });

  $("#${findDonorFormDivId}").find(".clearFindFormButton").button({
    icons : {
      primary : 'ui-icon-grip-solid-horizontal'
    }
  }).click(clearFindDonorForm);
  
  function clearFindDonorForm() {
		$("#${findDonorFormId}").each(function() {
		  this.reset();
		});
		$("#${findDonorFormResultId}").html("");
  }

  $("#${findDonorFormBloodGroupSelectorId}").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    },
    noneSelectedText: 'None Selected',
    selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  return "Any Blood Group";
										}
										else {
										  var checkedValues = $.map(selectedValues, function(input) { return input.value; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Blood Group';
										}
										  
    							}
  });
  $("#${findDonorFormBloodGroupSelectorId}").multiselect("uncheckAll");

});
</script>

<div id="${findDonorFormDivId}" class="formDiv">
	<b><i>Find Donors</i></b>
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
				<form:select path="bloodGroups" id="${findDonorFormBloodGroupSelectorId}">
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
		<div>
			<label></label>
			<button type="button" class="findDonorButton">
				Find donor
			</button>
			<button type="button" class="clearFindFormButton">
				Clear form
			</button>
		</div>
	</form:form>
</div>

<div id="${findDonorFormResultId}"></div>