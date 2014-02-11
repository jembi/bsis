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

<c:set var="configureRolesFormId">configureRoles-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	
  $("#${tabContentId}").find(".roleDiv").click(function() {
    $.ajax({
             url: "editRoleFormGenerator.html",
             type: "GET",
             data: { roleId: $(this).find("input[name=id]").val() },
             success: function (content) {
                        $("#${tabContentId}").html(content);
                       },
              error: function(response) {
                      showErrorMessage("Something went wrong.");          
                      }
           });
  });

  $("#${tabContentId}").bind("editRoleSuccess", editRoleDone);
  $("#${tabContentId}").bind("editRoleCancel", editRoleCancel);

  $("#${tabContentId}").find(".cancelButton").button({
    icons : {
      
    }
  }).click(refetchForm);

  function refetchForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

  function emptyChildContent() {
    $("#${childContentId}").html("");
  }

  function editRoleDone() {
    emptyChildContent();
    refetchForm();
  }

  function editRoleCancel() {
    emptyChildContent();
    refetchForm();
  }

  $("#${tabContentId}").find(".addNewRoleButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    $.ajax({
      url: "editRoleFormGenerator.html",
      type: "GET",
      data: {},
      success: function (content) {
    	  		$("#${tabContentId}").html(content);
                },
       error: function(response) {
                showErrorMessage("Something went wrong.");          
               }
    });
  });
  
});
</script>

<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Manage Roles</b>
    <br />
    <div class="tipsBox ui-state-highlight">
      <p>
        Select one of the roles below to edit or add a new role. 
      </p>
    </div>
    <div style="overflow: hidden;">
    
    <table>
    <tr>
    <td width="45%" style="padding:5px;"><b>Roles</b></td>
    <td width="45%"><b>Description</b></td>
    <td width="10%"></td>
    </tr>
    <c:forEach var="role" items="${model.allRules}">
    <tr>
	    <td width="45%" class="roleDiv">${role.name}<input name="id" type="hidden" value="${role.id}"></td>
	    <td width="45%">${role.description}</td>
	    <td width="10%"></td>
    </tr>
    </c:forEach>
    </table>
      
    <div>
      <br />
      <button class="addNewRoleButton">Add new role</button>
    </div>
  </div>
</div>
  <br />
  <br />
  <br />

  <div id="${childContentId}"></div>

</div>
