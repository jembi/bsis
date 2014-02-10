<%@page import="repository.bloodtesting.BloodTypingStatus"%>
<%@page import="model.util.BloodGroup"%>
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
<c:set var="donorCommunicationFormId">donorCommunicationForm-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	 $("#${addDonorFormDonorPanelsId}").multiselect({
	     multiple : true,
	     header : false
	   });
	  $("#${donorCommunicationFormId}").find(".clinicDate").datepicker({
		     changeMonth : true,
		     changeYear : true,
		     minDate : -36500,
		     maxDate : 0,
		     dateFormat : "dd/mm/yy",
		     yearRange : "c-100:c0",
		   });
	  $("#${donorCommunicationFormId}").find(".lastDonationToDate").datepicker({
		     changeMonth : true,
		     changeYear : true,
		     minDate : -36500,
		     maxDate : 0,
		     dateFormat : "dd/mm/yy",
		     yearRange : "c-100:c0",
		     onSelect : function(selectedDate) {
		          $("#lastDonationFromDate").datepicker("option", "minDate",
		              selectedDate);
		        }
		   });
	  $("#${donorCommunicationFormId}").find(".lastDonationFromDate").datepicker({
		     changeMonth : true,
		     changeYear : true,
		     minDate : -36500,
		     maxDate : 0,
		     dateFormat : "dd/mm/yy",
		     yearRange : "c-100:c0",
		     onSelect : function(selectedDate) {
		          $("#lastDonationToDate").datepicker("option", "maxDate",
		              selectedDate);
		        }
		   });
	  
	  // add multiple select / deselect functionality
	    $("#selectall").click(function () {
	          $('.case').attr('checked', this.checked);
	    });
	 
	    // if all checkbox are selected, check the selectall checkbox
	    // and viceversa
	    $(".case").click(function(){
	 
	        if($(".case").length == $(".case:checked").length) {
	            $("#selectall").attr("checked", "checked");
	        } else {
	            $("#selectall").removeAttr("checked");
	        }
	 
	    });
	    $("#${tabContentId}").find(".findDonorCommButton").button({
	        icons : {
	          primary : 'ui-icon-search'
	        }
	      });
	    $("#${tabContentId}").find(".clearDonorCommButton").button({
	        icons : {
	          
	        }
	      }).click(clearFindForm);
	      
	      function clearFindForm() {
	        refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
	        $("#${childContentId}").html("");
	      }
});
</script>

<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
  
  	<c:if test="${!empty success && !success}">
        <jsp:include page="../common/errorBox.jsp">
          <jsp:param name="errorMessage" value="${errorMessage}" />
        </jsp:include>
    </c:if>
    <b>Donor Communications</b>
    <form:form method="GET" commandName="donorCommunicationForm" id="${donorCommunicationFormId}"
      class="formFormatClass">
   
      <div>
      <div style="float: left;margin-left:13px;margin-right:72px;margin-top:2px;">
         <form:label path="donorPanel">${donorFields.donorPanel.displayName}</form:label>
      </div>
      <div>
         <form:select path="donorPanel" id="${addDonorFormDonorPanelsId}" multiple="multiple" class="addDonorFormDonorPanels">
          <c:forEach var="donorPanel" items="${donorPanels}">
            <form:option value="${donorPanel.id}">${donorPanel.name}</form:option>
          </c:forEach>
        </form:select>
      </div>
      </div>
      <div>
      <form:label path="bloodGroups">${model.donorFields.bloodGroup.displayName}</form:label>
      <input type="checkbox" value="all" id="selectall" style="width: 23px !important;" checked="checked">All Groups
      <form:checkboxes items="${bloodGroups}" path="bloodGroups" cssClass="case"/>
      </div>
      <div>
      <form:label path="clinicDate">Clinic Date</form:label>
      <form:input path="clinicDate" class="clinicDate"/>
      </div>
      <div>
      <form:label path="lastDonationFromDate">Last Donation between</form:label>
      <form:input path="lastDonationFromDate" class="lastDonationFromDate"/>
      <form:label path="lastDonationToDate" cssStyle="margin-left : 18px;">and</form:label>
      <form:input path="lastDonationToDate" cssStyle="margin-left :-115px;" class="lastDonationToDate" />
      
      </div>
    </form:form>
    <div class="formFormatClass">
      <div>
        <label></label>
        <button type="button" class="findDonorCommButton">
          Find Donors
        </button>
        <button type="button" class="clearDonorCommButton">
          Clear Form
        </button>
      </div>
    </div>
    <div class="findDonorResults"></div>
  </div>
<div id="${childContentId}"></div>
  <br/>
</div>
