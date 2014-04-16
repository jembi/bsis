<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<c:set var="deferralReasonSelectorId">deferralReasonSelector-${unique_page_id}</c:set>
<c:set var="deferPermanentlyId">deferPermanently-${unique_page_id}</c:set>

<script>
$(document).ready(function(){
 	$("#${deferralReasonSelectorId}").val(${deferReasonId});
	
  function createDatePickerForUntilDate()
  {
	  $("#${tabContentId}").find(".deferUntil").datepicker({
		    changeMonth : true,
		    changeYear : true,
		    minDate : 1,
		    maxDate : 60,
		    dateFormat : "dd/mm/yy",
		    yearRange : "c-100:c+1",
		  }); 
  }
  $("#${deferralReasonSelectorId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });
  function toggleCheckboxPopulateDefaultValueForDeferUntil() {
	  var isChecked = $("#${deferPermanentlyId}").is(":checked");
      if (isChecked) {
    	  $("#${tabContentId}").find(".deferUntil").val('01/01/2100');
    	  $("#${tabContentId}").find(".deferUntil").attr("readOnly","true");    
    	  $("#${tabContentId}").find(".deferUntil").datepicker("destroy");
      }else{
    	  $("#${tabContentId}").find(".deferUntil").val('');
    	  $("#${tabContentId}").find(".deferUntil").removeAttr("readOnly");
    	  createDatePickerForUntilDate();
      }
  }
  $("#${deferPermanentlyId}").change(toggleCheckboxPopulateDefaultValueForDeferUntil);
  createDatePickerForUntilDate();
});
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_DEFERRAL)">
<div id="${tabContentId}">

  <form class="formFormatClass deferDonorForm" name="deferDonorForm">

    <input name="donorId" type="hidden" value="${donorId}"/>
    <input name="donorDeferralId" type="hidden" value="${donorDeferralId}"/>
    <div>
      <label>Defer until</label>
      <input name="deferUntil" class="deferUntil" value="${deferralUntilDate}" id="deferUntilId"/>
    </div>
	<div>
	  <label>Defer permanently</label>
	  <input type="checkbox" name="deferPermanently" id="${deferPermanentlyId}" class="deferPermanently" style="width:30px;">
	</div>
    <div>
      <label>Deferral reason</label>
      <select name="deferralReasonId" id="${deferralReasonSelectorId}">
        <c:forEach var="deferralReason" items="${deferralReasons}">
          <option value="${deferralReason.id}">${deferralReason.reason}</option>
        </c:forEach>
      </select>
    </div>
    <div>
      <label>Deferral details (Optional)</label>
      <textarea name="deferralReasonText">${deferReasonText}</textarea>
    </div>

  </form>

</div>
</sec:authorize>