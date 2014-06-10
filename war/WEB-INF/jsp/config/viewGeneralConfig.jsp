<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
	              
	          $("#${tabContentId}").find(".saveConfigButton").button({
		       icons : {
		        primary : 'ui-icon-plusthick'
		      }
                  })
		  .click(function() {
			  var names = $("#generalConfigForm").serialize();
			  $.ajax({
				  url: "updateGeneralConfigProps.html",
				  data:   names,
				  type: "POST",
				  success: function (response) {
					  $("#${tabContentId}").replaceWith(response);
					  
				  },
				  error: function () {
				  showErrorMessage("Something went wrong. Please try again");
				  }
				  }); 
		        
		         });
                  $("#${tabContentId}").find(".cancelConfigButton").button({
		       icons : {
		        primary : 'ui-icon-plusthick'
		      }
                  })
		  .click(function() {
			  $.ajax({
				  url: "viewGeneralConfig.html",
				  type: "GET",
				  success: function (response) {
					  $("#${tabContentId}").replaceWith(response);
					  
				  },
				  error: function () {
				  showErrorMessage("Something went wrong. Please try again");
				  }
				  }); 
		        
		         });

      });



</script>

<div id="${tabContentId}" class="formDiv">


    <form:form commandName="generalConfigForm" class="formFormatClass" method="POST">
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

                        <td><form:input path="values" value="${config.value}"/>
                        
                        </td>

                        <td>${config.description}</td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div style="margin-left: 200px;">
            <label></label>
            <button type="button" class="saveConfigButton autoWidthButton">
                Save</button>
            <button type="button" class="cancelConfigButton autoWidthButton">
                Cancel</button>
        </div>
    </div>

</form:form>  
</div>

