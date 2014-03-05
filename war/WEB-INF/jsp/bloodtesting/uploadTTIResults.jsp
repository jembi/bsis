<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

<c:set var="ttiTestSelectorId">ttiTestSelectorId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	
  $("#${mainContentId}").find(".uploadeTtiResultsButton")
                        .button()
                        .click(uploadTtiResults);


  function uploadTtiResults() {
	  
    var tsvFile = $("#${mainContentId}").find('input[name="fileName"]').val();
    $("#${tabContentId}").find("#errorDiv").html("");
    console.log(tsvFile);
    emptyChildContent();
    
    var oMyForm = new FormData();
	var file2 = $("#${tabContentId}").find("#fileName").val();
	
	jQuery.each($("#${tabContentId}").find("#fileName")[0].files, function(i, file) {
		oMyForm.append('fileData'+i, file);
	});
	
	
	/* var file3=file2.files[0];
	alert(file3);
	alert(":::::::::::::::::::::::::::::"+file2+"::::::::::::::;tsvFile:::::::::"+tsvFile)
	oMyForm.append("fileData",file3); */
    
    
  $.ajax({
      url: "uploadTTIResultsGenerator.html",
      data: oMyForm,
      dataType: 'text',
      processData: false,
      contentType: false,
      type: "POST",
     
      success: function (response) {
    		  $("#${mainContentId}").hide();  
    	 
          $("#${childContentId}").html(response);       
          },
      error: function(response) {
               showErrorMessage("Something went wrong. Please try again.");
             }
    });
  }
  
  $("#${tabContentId}").bind("ttiResultSuccess",
	      function(event, content) {
	        $("#${mainContentId}").show();
	        $("#${childContentId}").html("");
	      });


  

  function refetchForm() {
    $("#${tabContentId}").load("${refreshUrl}");
  }

  function emptyChildContent() {
    $("#${childContentId}").html("");
  }
});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">
  	<div id="errorDiv">
	<c:if test="${!empty success && !success}">
        <jsp:include page="../common/errorBox.jsp">
          <jsp:param name="errorMessage" value="${errorMessage}" />
        </jsp:include>
    </c:if>
	</div>
    <form class="formFormatClass" id="formUplod" method="post" enctype="multipart/form-data">
      <div>
        <label style="width: auto;">
          <b>Import TTI Results</b>
        </label>
      </div>
      <div id="refreshFileUpload">
        <label style="width: auto;">Select File to Upload:</label>
        <input type="file" name="fileName" id="fileName" size="150"/>
      </div>
    </form>

    <div class="formFormatClass">
      <div>
        <label>&nbsp;</label>
        <button class="uploadeTtiResultsButton">
          Upload Results
        </button>
      </div>
    </div>

  </div>

  <div id="${childContentId}">
  </div>

</div>