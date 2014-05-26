
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
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="updateDonorCodesFormId">updateDonorCodes-${unique_page_id}</c:set>
<script>
   
 $(document).ready(
   function() {
	       
	   
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
	   });
    
    
</script>
 <div id = "${childContentId}">             
    <form:form id="addDonorCodeForm" method="POST"
					 commandName="addDonorCodeForm" class="formFormatClass">
                     
                    <form:label path="donorId">Add Donor Code</form:label>
					<input name="donorId" type="hidden" value="${donor.id}" />
			<form:select path="donorCodeGroupId">
                                            <form:option value="">Donor Code Group</form:option>
                                            <c:forEach var="donorCodeGroup" items="${donorCodeGroups}">
	                    	<form:option value="${donorCodeGroup.id}">${donorCodeGroup.donorCodeGroup}</form:option>
						</c:forEach>
					</form:select>

					<form:select path="donorCodeId">
                                            <form:option value="">Donor Codes</form:option>
						<c:forEach var="donorCode"	items="${donorCodeGroups[0].donorCodes}">
							<form:option value="${donorCode.id}">${donorCode.donorCode}</form:option>
						</c:forEach>
					</form:select>        
                                      <form:errors class="formError" path="donorCodeId"
						delimiter=", "></form:errors>
				</form:form>
                                        
</div>	
		