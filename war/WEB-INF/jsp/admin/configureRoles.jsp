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
<c:set var="table_id">manageRolesTable-${unique_page_id}</c:set>
<c:set var="configureRolesFormId">configureRoles-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	
	 var manageRolesTable = $("#${table_id}").dataTable({
	        "bJQueryUI" : true,
	        "sDom" : '<"H"lfrT>t<"F"ip>',
	        "oTableTools" : {
	          "aButtons" : [],
	          "fnRowSelected" : function(node) {
	                            },
	          "fnRowDeselected" : function(node) {
	                            },
	        },
	        "bPaginate" : false
	      });
	
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
  
  <b>Manage Roles</b>
  <br/>
  <br/>
  
  <div id="${mainContentId}">

    <!-- <div class="tipsBox ui-state-highlight">
      <p>
        Select one of the roles below to edit or add a new role. 
      </p>
    </div>
    -->
    <div></div>
    
    <table id="${table_id}" class="bloodTestsTable">
    	<thead>
	    	<tr>
	    		<th style="display: none"></th>
	    		<th>Role</th>
	    		<th>Description</th>
		    </tr>
		  </thead>
		  <tbody> 
		  	<c:forEach var="role" items="${model.allRoles}">
		     	<tr>
		     		<td style="display: none">${role.id}</td>
		     		<td width="25%">
		     			<div class="roleDiv">
		        			${role.name}
		        			<input name="id" type="hidden" value="${role.id}">
		     			 </div>
		     		</td>
		     		<td>${role.description}</td>
		     	</tr>
		    </c:forEach>
	     </tbody>
    </table>
      
    <div>
      <br />
      <button class="addNewRoleButton">Add new role</button>
    </div>
  </div>

  <div id="${childContentId}"></div>

</div>
