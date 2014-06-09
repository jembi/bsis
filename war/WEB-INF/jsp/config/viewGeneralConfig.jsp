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
<c:set var="table_id">GeneralCOnfig-${unique_page_id}</c:set>
<c:set var="childContentId">childContentId-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>



<script>
$(document).ready(
    function() {

       $("#${table_id}").dataTable({"bJQueryUI" : true});
       
    });
       
     
</script>

<div id="${tabContentId}" class="formDiv">
  <c:choose>

    <c:when test="${fn:length(config) eq -1}">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
    </c:when>

    <c:otherwise>
        <div>
            <b>
              General Configuration  
            </b>
        </div>
     <div>        
      <table id="${table_id}" class="dataTable donorsTable" >
        <thead>
          <tr>
              <th>${donorFields.name.displayName}</th>
       
              <th>${donorFields.value.displayName}</th>
            
              <th>${donorFields.description.displayName}</th>
           
          </tr>
        </thead>
        <tbody>
          <c:forEach var="config" items="${config}">
            <tr>
              
                <td>${config.name}</td>        
             
                <td>${config.value}</td>
                    
                <td>${config.description}</td>
                                           
            </tr>
          </c:forEach>
        </tbody>
      </table>

    </c:otherwise>
  </c:choose>
 </div>
</div>

