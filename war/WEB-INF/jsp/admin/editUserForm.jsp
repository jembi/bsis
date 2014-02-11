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
    <%-- <div>
      <form:label path="isAdmin">Admin User?</form:label>
      <form:checkbox path="isAdmin" style="width: auto;" />
      <form:errors class="formError" path="user.isAdmin" delimiter=", "></form:errors>
    </div>
    <div>
      <form:label path="modifyPassword">Modify Password?</form:label>
      <form:checkbox path="modifyPassword" style="width: auto;" />
    </div> --%>
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
    	<div>
	      <form:label path="password">Current Password</form:label>
	      <form:password path="currentPassword" />
	      <form:errors class="formError" path="user.isAdmin" delimiter=", "></form:errors>
	    </div>	
    	<div>
	      <form:label path="password">New Password</form:label>
	      <form:password path="password" />
	      <form:errors class="formError" path="user.password" delimiter=", "></form:errors>
	    </div>	
	   <div>
	      <label>Confirm Password</label>
	      <form:password path="userConfirPassword" id="userConfirPassword" name="userConfirPassword"/>
	    </div>
    </c:if>
    
    <div>
    	<form:hidden path="userRole" id="userRoles"/>
    	<table>
    		<tr>
    			<td  style="width:175px"><label>Roles</label></td>
    			<td>
    				<c:choose>
			   			 <c:when test="${fn:containsIgnoreCase(userRoles, '1')}">  <input type="checkbox" name="roleAdmin" id="roleAdmin" value="1" checked="checked"/></c:when>
						 <c:otherwise><input type="checkbox" name="roleAdmin" id="roleAdmin" value="1"/></c:otherwise>
		   			</c:choose>Administrator &nbsp; <form:errors class="formError" path="user.isStaff" delimiter=", "></form:errors> <br/>
    				
    				<c:choose>
			   			 <c:when test="${fn:containsIgnoreCase(userRoles, '2')}"><input type="checkbox" name="roleDonorLab" id="roleDonorLab" value="2" checked="checked"/></c:when>
						 <c:otherwise> <input type="checkbox" name="roleDonorLab" id="roleDonorLab" value="2"/></c:otherwise>
		   			</c:choose>Donor Staff<br/>
		   			
		   			<c:choose>
			   			 <c:when test="${fn:contains(userRoles, '3')}"> <input type="checkbox" name="roleTestLab" id="roleTestLab" value="3" checked="checked"/></c:when>
						 <c:otherwise> <input type="checkbox" name="roleTestLab" id="roleTestLab" value="3"/></c:otherwise>
		   			</c:choose>Testing Lab Staff<br/>
		   			
		   			<c:choose>
			   			 <c:when test="${fn:containsIgnoreCase(userRoles, '4')}"><input type="checkbox" name="roleUser" id="roleUser" value="4" checked="checked"/></c:when>
						 <c:otherwise><input type="checkbox" name="roleUser" id="roleUser" value="4"/></c:otherwise>
		   			</c:choose>Authenticated User
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
