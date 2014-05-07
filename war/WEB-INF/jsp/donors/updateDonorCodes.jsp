<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
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
<c:set var="tableDiv">tableDiv-${unique_page_id}</c:set>
<c:set var="donorCodes">donoeCodes-${unique_page_id}</c:set>
<c:set var="updateDonorCodesFormId">updateDonorCodes-${unique_page_id}</c:set>
<c:set var="addDonorCodes">AddDonorCodes-${unique_page_id}</c:set>

<script>


$(document).ready(
	      
	      function() {
	       
	        function notifyParentDone() {
	              $("#${tabContentId}").parent().trigger("donorSummarySuccess");
	            }
	        
	        fetchContent("donorCodesTable.html",  {donorId: "${donor.id}"},$("#${tableDiv}")) ; 
	   
	        
	   var donorCode = new Array();
	   <c:forEach items="${donorCodeGroups}" var="donorCodeGroup" varStatus="status">
	              donorCode[${status.index}] = new Array();
	            
	        <c:forEach items="${donorCodeGroup.donorCodes}" var="donorCode" varStatus="innerStatus">
	            donorCode[${status.index}] [${innerStatus.index}]= new Array();
	            donorCode[${status.index}][${innerStatus.index}][0]= "${donorCode.id}";
	            donorCode[${status.index}][${innerStatus.index}][1]= "${donorCode.donorCode}";     
	         </c:forEach>
	            
	    </c:forEach>

	      $('#donorCodeGroupId').change(function(){
	          
	          $('#donorCodeId').empty();
	          var id =  $('#donorCodeGroupId').val()-1;
	          
	             for(var index=0 ; index < donorCode[id].length; index++ )
	              $('#donorCodeId').append( new Option( donorCode[id][index][1],  donorCode[id][index][0]) );
	          
	        });
	       
	      
	  
	  
	  $("#${donorCodes}").find(".addDonorCodeButton").button({
	      icons : {
	        primary : 'ui-icon-plusthick'
	      }
	    }).click(
	        function() {
	  var donorCode = $("#${updateDonorCodesFormId}").serialize();
	  $.ajax({
	    type: "POST",
	    url: "updateDonorCodes.html",
	    data: donorCode,
	    success: function(jsonResponse) {
	    	  $("#${tableDiv}").replaceWith(jsonResponse);
	      
	             },
	    error: function(response) {
	       showErrorMessage("Something went wrong. Please try again.");
	           }
	  });

	  
	}); 

	    $("#${donorCodes}").find(".doneButton").button(
	      {
	        icons : {primary : 'ui-icon-check'}}
	      )
	  .click(function() {
		 
		  $.ajax({
			  url: "donorSummary.html",
			  data:   {donorId: "${donor.id}"},
			  type: "GET",
			  success: function (response) {
				  $("#${donorCodes}").html(response);
				  
			  },
			  error: function (response) {
			  showErrorMessage("Something went wrong. Please try again.");
			  }
			  }); 
	        
	         });

	});
	
	</script>

<sec:authorize
	access="hasRole(T(utils.PermissionConstants).EDIT_DONOR_CODE)">
	
     <div id="${donorCodes}" class="formFormatClass">
			<c:if test="${donorFields.donorNumber.hidden != true }">
				<div>
					<label>${donorFields.donorNumber.displayName}</label>	
				    <label>${donor.donorNumber}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.firstName.hidden != true }">
				<div>
					<label>${donorFields.firstName.displayName}</label>
					 <label>${donor.firstName}</label>
				</div>
			</c:if>
			  <c:if test="${donorFields.lastName.hidden != true }">
    <div>
      <label>${donorFields.lastName.displayName}</label>
      <label>${donor.lastName}</label>
    </div>
    <br>
  </c:if> 
     <div style="margin-left: 12px;">
        <b><label>Donor Codes</label></b>
        </div>
        <div>
        <div id="${tableDiv}">   
        </div>
  
         	
             <div>
             <div>
				<form:form id="${updateDonorCodesFormId}" method="POST"
					 commandName="addDonorCodeForm" class="formFormatClass">
                     
                    <form:label path="donorId">Add Donor Code</form:label>
					<input name="donorId" type="hidden" value="${donor.id}" />
					<form:select path="donorCodeGroupId">
						<c:forEach var="donorCodeGroup" items="${donorCodeGroups}">
							<form:option value="${donorCodeGroup.id}">${donorCodeGroup.donorCodeGroup}</form:option>
						</c:forEach>
					</form:select>

					<form:select path="donorCodeId">
						<c:forEach var="donorCode"
							items="${donorCodeGroups[0].donorCodes}">
							<form:option value="${donorCode.id}">${donorCode.donorCode}</form:option>
						</c:forEach>
					</form:select>        
					
				     

				</form:form>
			</div>	
				   <div style="margin-left: 145px;">
                     <label></label>
					<button type="button" class="addDonorCodeButton autoWidthButton">
							Add Donor Code</button>
					</div>
				     <br>
				 <div style="margin-left: 150px;">
			
				
				<button type="button" class="doneButton">
							Done</button>
					
				</div>

			</div>

</div>


</sec:authorize>
