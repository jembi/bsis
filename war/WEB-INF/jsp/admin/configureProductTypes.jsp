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

<c:set var="configureProductTypesFormId">configureProductTypes-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addProductTypeButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureProductTypesFormId}").find(".productTypeDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("");
    newDiv.find('input[name="productType"]').val("");
    $("#${configureProductTypesFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveProductTypesButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var productTypeDivs = $("#${configureProductTypesFormId}").find(".productTypeDiv");
    for (var index=0; index < productTypeDivs.length; index++) {
      var div = $(productTypeDivs[index]);
      var id = div.find('input[name="id"]').val();
      var productType = div.find('input[name="productType"]').val();
      console.log(productType);
      if (id == undefined || id == null || id === "")
        id = productType;
      data[id] = productType;
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureProductTypes.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
        				 $("#${tabContentId}").replaceWith(response);
        				 showMessage("Product Types Updated Successfully!");
      				 },
      error: 	 function(response) {
        				 showErrorMessage("Something went wrong. Please try again later");
        				 console.log(response);
      				 },
    });
    return false;
  });

  $("#${tabContentId}").find(".cancelButton").button({
    icons : {
      
    }
  }).click(refetchForm);
  
  function refetchForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Configure Product Types</b>
		<br />
		<br />
		<div class="tipsBox ui-state-highlight">
			<p>
				Modify names of product types. Add new product types. 
			</p>
		</div>
		<form id="${configureProductTypesFormId}">
				<c:forEach var="productType" items="${model.allProductTypes}">
					<div class="productTypeDiv">
						<div>
							<input type="hidden" name="id" value="${productType.productType}" />
							<input type="text" name="productType" value="${productType.productType}" />
						</div>
					</div>
			</c:forEach>
		</form>
			<br />
			<div>
				<label>&nbsp;</label>
				<button class="addProductTypeButton">Add new product type</button>
				<button class="saveProductTypesButton">Save</button>
				<button class="cancelButton">Cancel</button>
			</div>

	</div>

	<div id="${childContentId}"></div>

</div>
