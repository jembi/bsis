<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">donorCodessTable-${unique_page_id}</c:set>
<c:set var="childContentId">childContentId-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>



<script>
$(document).ready(
    function() {

       $("#${table_id}").dataTable({"bJQueryUI" : true});
       
      
       $("#${tabContentId}").find(".deleteButton").button({
    	      icons : {
    	          primary : 'ui-icon-plusthick'
    	        }
    	      }).click(
    	  function() {
    	        	  
    	    var id = $(this).val();
    	    $.ajax({
    	      type: "GET",
    	      url: "deleteDonorCode.html",
    	      data: {id : id},
    	      success: function(jsonResponse) {
    	    	  $("#${tabContentId}").replaceWith(jsonResponse);
    	    	  
    	               },
    	      error: function(response) {
    	    	  showErrorMessage("Something went wrong. Please try again.");
    	             }
    	            });
       
    });
       
    });
       
       
     
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONOR_CODE)">
<div id="${tabContentId}" class="formDiv">
  <c:choose>

    <c:when test="${fn:length(donorDonorCodes) eq -1}">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
    </c:when>

    <c:otherwise>

      <table id="${table_id}" class="dataTable donorsTable" >
        <thead>
          <tr>
            <th style="display: none"></th>
         
              <th>Donor Code Group</th>
           
       
              <th>Donor Code</th>
          
            
              <th>Added By</th>
          
            
              <th>Date Added</th>
   
           
    
          </tr>
        </thead>
        <tbody>
          <c:forEach var="donorDonorCode" items="${donorDonorCodes}">
            <tr>
              
              
                <td>${donorDonorCode.donorCodeId.donorCodeGroup.donorCodeGroup}</td>        
             
                <td>${donorDonorCode.donorCodeId.donorCode}</td>
                    
                <td>${donorDonorCode.createdBy}</td>
         
                <td>${donorDonorCode.createdDate}</td>
                
                <td> <button type="button" id="button-${donorDonorCode.id}" value = "${donorDonorCode.id}" class="deleteButton">
							Remove</button></td>
                            
            </tr>
          </c:forEach>
        </tbody>
      </table>

    </c:otherwise>
  </c:choose>

</div>
</sec:authorize>

