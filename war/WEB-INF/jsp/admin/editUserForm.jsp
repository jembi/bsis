<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="editUserFormId">editUserFormDiv-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {
    	  
    	  	  $('#${editUserFormId} :checkbox').click(function() {
    		    var $this = $(this);
    		    // $this will contain a reference to the checkbox   
    		    if ($this.is(':checked')) {
    		        // the checkbox was checked 
    		    	document.getElementById("password").disabled=false;
    			    document.getElementById("userConfirmPassword").disabled=false;
    		    } else {
    		        // the checkbox was unchecked
    		    	document.getElementById("password").disabled=true;
    			    document.getElementById("userConfirmPassword").disabled=true;
    		    }
    		});
   
    	  	  
        function notifyParentSuccess() {
           // let the parent know we are done
           $("#${tabContentId}").parent().trigger("editUserSuccess");
        }

        function notifyParentCancel() {
           // let the parent know we are done
           $("#${tabContentId}").parent().trigger("editUserCancel");
         }

        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(
             function() {
               notifyParentCancel();
        });

        $("#${tabContentId}").find(".saveUserButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingUser}" == "true")
                updateExistingUser($("#${editUserFormId}")[0],
                                    "${tabContentId}", notifyParentSuccess);
              else
                addNewUser($("#${editUserFormId}")[0], "${tabContentId}", notifyParentSuccess);
            });
      });
</script>

<div id="${tabContentId}">
  <form:form method="POST" class="formFormatClass" id="${editUserFormId}"
    commandName="editUserForm" autocomplete="off">
    
      <c:if test="${!empty success && !success}">
        <jsp:include page="../common/errorBox.jsp">
          <jsp:param name="errorMessage" value="${errorMessage}" />
        </jsp:include>
    </c:if>
     
    <form:hidden path="id" />
    <c:if test="${model.existingUser  ne true}">
	    <div>
	      <form:label path="username">Username</form:label>
	      <form:input path="username" />
	      <form:errors class="formError" path="user.username" delimiter=", "></form:errors>
	    </div>
    </c:if>
    <c:if test="${model.existingUser eq true}">
	    <div>
	      <form:label path="username">Username</form:label>
	      <form:input path="username" disabled="true" />
	      <form:hidden path="username"/>
	    </div>
    </c:if>
    <div>
      <form:label path="firstName">First name</form:label>
      <form:input path="firstName" />
      <form:errors class="formError" path="user.firstName" delimiter=", "></form:errors>
    </div>
    <div>
      <form:label path="lastName">Last name</form:label>
      <form:input path="lastName" />
      <form:errors class="formError" path="user.lastName" delimiter=", "></form:errors>
    </div>
   
    <c:if test="${model.existingUser  ne true}">
	    <div>
	      <form:label path="password">Password</form:label>
	      <form:password path="password" />
	      <form:errors class="formError" path="user.password" delimiter=", "></form:errors>
	    </div>	
	   <div>
	      <label>Confirm Password</label>
	      <form:password path="userConfirPassword" />
	    </div>
    </c:if> 
    <c:if test="${model.existingUser  eq true}">
   
   
    <b>Update Password</b>
    <form:checkbox path="modifyPassword" id="modifyPassword" onclick="checkPasswordStatus()"/>
    
   
      <form:hidden  path="currentPassword"/>
    	<div>
	      <form:label path="password">New Password</form:label>
	      <form:password path="password" id="password"  disabled="true"/>
	      <form:errors class="formError" path="user.password" delimiter=", "></form:errors>
	    </div>	
	   <div>
	      <label>Confirm Password</label>
	      <form:password path="userConfirPassword" id="userConfirmPassword"  disabled="true"/>
	    </div>
    </c:if>
    
    <div>
    
    	<table>
    		<tr>
    			<td  style="width:175px"><label>Roles</label></td>
                		
    			<td>
    			 <form:errors class="formError" path="userRoles" delimiter=", "></form:errors>	<br>
     <!--  When editing the user--> 
          <c:choose>
          <c:when test="${userRoles!=null}">
    			   
            <c:forEach var="role" items="${allRoles}">  
                 <c:set var="idFound" value="false"></c:set>
                 
                   <c:forEach   var="userRole" items="${userRoles}"> 
                        <c:if test="${role.id eq userRole.id}">    
                           <c:set var="idFound" value="true"></c:set>
                         </c:if>
                  </c:forEach>
                  
               <c:if test="${idFound eq 'true'}">
     		    <form:checkbox path="userRoles" value="${role.id}" label="${role.name}" checked="checked" /><br>
            	</c:if>
              	<c:if test="${idFound eq 'false'}">
      	         <form:checkbox path="userRoles" value="${role.id}" label="${role.name}" /><br>
              	</c:if>
                  
             </c:forEach>
                       
        </c:when>
            
           <c:otherwise>
                       
                 <c:forEach   var="role" items="${allRoles}"> 
                       <form:checkbox path="userRoles" value="${role.id}" label="${role.name}" /><br>
                     
                 </c:forEach>
            </c:otherwise>
        </c:choose>
                </td>
                
    			         
    		    </tr>
    	</table>
    </div>
    <div>
      <label></label>
      <button type="button" class="saveUserButton autoWidthButton">
        Save
      </button>
      <button type="button" class="cancelButton">
        Cancel
      </button>
    </div>
  </form:form>
</div>
