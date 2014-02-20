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
<c:set var="table_id">manageUsersTable-${unique_page_id}</c:set>
<c:set var="configureUsersFormId">configureUsers-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	
	 var manageUsersTable = $("#${table_id}").dataTable({
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

  $("#${tabContentId}").find(".userDiv").click(function() {
    $.ajax({
             url: "editUserFormGenerator.html",
             type: "GET",
             data: { userId: $(this).find("input[name=id]").val() },
             success: function (content) {
                         animatedScrollTo($("#${childContentId}"));
                        $("#${childContentId}").html(content);
                        $("#${mainContentId}").hide();
                       },
              error: function(response) {
                      showErrorMessage("Something went wrong.");          
                      }
           });
  });

  $("#${tabContentId}").bind("editUserSuccess", editUserDone);
  $("#${tabContentId}").bind("editUserCancel", editUserCancel);

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

  function editUserDone() {
    emptyChildContent();
    refetchForm();
  }

  function editUserCancel() {
    emptyChildContent();
    refetchForm();
  }

  $("#${tabContentId}").find(".addNewUserButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    $.ajax({
      url: "editUserFormGenerator.html",
      type: "GET",
      data: {},
      success: function (content) {
                  animatedScrollTo($("#${childContentId}"));
                   $("#${childContentId}").html(content);
                   $("#${mainContentId}").hide();
                },
       error: function(response) {
                showErrorMessage("Something went wrong.");          
               }
    });
  });
  
});
</script>

<div id="${tabContentId}" class="formDiv">
 <b>Manage Users</b>
 <br />
 <br/>
 
  <div id="${mainContentId}">
   <!--  <div class="tipsBox ui-state-highlight">
      <p>
        Select one of the users below to edit or add a new user. 
      </p>
    </div> -->
    <div></div>
    <table id="${table_id}" class="bloodTestsTable">
    	<thead>
	    	<tr>
	    		<th style="display: none"></th>
	    		<th>Username</th>
	    		<th>First Name</th>
	    		<th>Last Name</th>
	    		<th>Role(s)</th>
		    </tr>
		  </thead>
		  <tbody> 
		    <c:forEach var="user" items="${model.allUsers}">
		     	<tr>
		     		<td style="display: none">${user.id}</td>
		     		<td>
		     			<div class="userDiv">
		        			${user.username}
		        			<input name="id" type="hidden" value="${user.id}">
		     			 </div>
		     		</td>
		     		<td>${user.firstName}</td>
		     		<td>${user.lastName}</td>
		     		<td>${user.userRole}</td>
		     	</tr>
		    </c:forEach>
	     </tbody>
   	</table>
    <div>
      <br />
      <button class="addNewUserButton">Add new user</button>
    </div>
  </div>

  <div id="${childContentId}"></div>

</div>
