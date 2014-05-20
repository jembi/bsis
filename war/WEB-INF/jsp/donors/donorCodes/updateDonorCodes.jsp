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
<c:set var="addDonorCodeForm">AddDonorCodeForm-${unique_page_id}</c:set>

<script>


$(document).ready(
	      
        function() {
	       
	   fetchContent("donorCodesTable.html",  {donorId: "${donor.id}"},$("#${tableDiv}")) ; 
           fetchContent("addDonorCodeFormGenerator.html",  {donorId: "${donor.id}"},$("#${addDonorCodeForm}")) ;
	   
	   var donorCode = new Array();
	   <c:forEach items="${donorCodeGroups}" var="donorCodeGroup" varStatus="status">
	              donorCode[${status.index}] = new Array();
	            
	        <c:forEach items="${donorCodeGroup.donorCodes}" var="donorCode" varStatus="innerStatus">
	            donorCode[${status.index}] [${innerStatus.index}]= new Array();
	            donorCode[${status.index}][${innerStatus.index}][0]= "${donorCode.id}";
	            donorCode[${status.index}][${innerStatus.index}][1]= "${donorCode.donorCode}";     
	         </c:forEach>
	            
	    </c:forEach>           
                $( ".addDonorCodeButton" ).attr('disabled','disabled');
                $( ".addDonorCodeButton" ).button( "refresh" );
	      $('#donorCodeGroupId').change(function(){
	          if ($('#donorCodeGroupId').val().trim().length >  0)
                  {
                
                    $( ".addDonorCodeButton" ).button( "option", { disabled: false } );
	            $( ".addDonorCodeButton" ).button( "refresh" ); 
                 $('#donorCodeId').empty();
	          var id =  $('#donorCodeGroupId').val()-1;
	          for(var index=0 ; index < donorCode[id].length; index++ )
	              $('#donorCodeId').append( new Option( donorCode[id][index][1],  donorCode[id][index][0]) );
                  }else{
                    $( ".addDonorCodeButton" ).button( "option", { disabled: true } );        $( ".selector" ).button( "refresh" );         
                     $('#donorCodeId').empty();
                      $('#donorCodeId').append( new Option( "Donor Codes",""));
                      
                  }
                      
	        });
	       
	      
	  
	  
	  $("#${donorCodes}").find(".addDonorCodeButton").button({
	      icons : {
	        primary : 'ui-icon-plusthick'
	      }
	    }).click(
	        function() {
	  var donorCode = $("#addDonorCodeForm").serialize();
	  $.ajax({
	    type: "POST",
	    url: "addDonorCodeForm.html",
	    data: donorCode,
    statusCode: {
       200: function(response) {
            $("#${tableDiv}").empty();
            $("#${tableDiv}").html(response);
       },
       400 :function(response){
            showErrorMessage("Donor Code is Already Assigned");
//            $("#${addDonorCodeForm}").empty();       
//            $("#${addDonorCodeForm}").html(response);   // Can't load the response here     
       }
       },
	    error: function() {       
	         showErrorMessage("OOPS! Something Went Wrong");
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
			  error: function () {
			  showErrorMessage("Something went wrong. Please try again");
			  }
			  }); 
	        
	         });

	});
	
	</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).EDIT_DONOR_CODE)">
	
     <div id="${donorCodes}" class="formFormatClass">
       <div>
        <label><b>Donor Codes</b></label>
      </div>
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

     <div id="${addDonorCodeForm}">
     </div>
         	
     <div>
        <div style="margin-left: 145px;">
        <label></label>
        <button name="addDonorCodeButton" type="button" class="addDonorCodeButton">Add Donor Code</button>
	 </div>
     <div>
        <div id="${tableDiv}">   
     </div>
	 <br>
	 <div style="margin-left: 150px;">
		<button type="button" class="doneButton">Done</button>
	 </div>
	</div>
</div>	
</sec:authorize>
