<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script>
  $("#findCollectionButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findCollectionFormData = $("#findCollectionForm").serialize();
    $.ajax({
      type : "GET",
      url : "findCollection.html",
      data : findCollectionFormData,
      success : function(data) {
        $('#findCollectionResult').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });
  $("#findCollectionFormCenters").multiselect({
    position : {
      my : 'left top',
      at : 'right center'

    },
    selectedList: 4
  });
  $("#dateCollectedFrom").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateCollectedTo").datepicker("option", "minDate", selectedDate);
    }
  });
  $("#dateCollectedTo").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      $("#dateCollectedFrom").datepicker("option", "maxDate", selectedDate);
    }
  });
</script>

<div id="${findDonorFormDivId}" class="formDiv">
	<b><i>Find Collections</i></b>
	<form:form method="GET" commandName="findCollectionForm" id="findDonorForm"
		class="formInTabPane">
		<div>
				<form:label path="collectionNumber">${model.collectedSampleFields.collectionNumber.displayName}</form:label>
				<form:input path="collectionNumber" />
		</div>
		<div>
				<form:label path="sampleNumber">${model.collectedSampleFields.sampleNumber.displayName}</form:label>
				<form:input path="sampleNumber" />
		</div>
		<div>
				<form:label path="shippingNumber">${model.collectedSampleFields.shippingNumber.displayName}</form:label>
				<form:input path="shippingNumber" />
		</div>
		<div>
			<form:label path="centers">${model.collectedSampleFields.center.displayName}</form:label>
			<form:select path="centers" id="findCollectionFormCenters">
					<c:forEach var="center" items="${model.centers}">
						<form:option value="${center.id}" label="${center.name}" />
					</c:forEach>
				</form:select>
		</div>
		<div>&nbsp;</div>
		<div>
			<b><i>Filter Collections</i></b>
		</div>
		<div>
			Having Date Collected Between
		</div>
		<div>
			<div>
				<form:input path="dateCollectedFrom" id="dateCollectedFrom" />
					and
				<form:input path="dateCollectedTo" id="dateCollectedTo" />
			</div>
			<button type="button" id="findCollectionButton">
				Find collection
			</button>
		</div>
</form:form>
</div>

<div id="findCollectionResult"></div>