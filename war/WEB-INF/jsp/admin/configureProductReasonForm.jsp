<%@page import="model.productmovement.ProductStatusChangeReasonCategory"%>
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

<c:set var="configureProductReasonFormId">configureProductReasonForm-${unique_page_id}</c:set>
<c:set var="productCategoryId">productCategory-${unique_page_id}</c:set>
<c:set var="statusChangeReasonId">statusChangeReason-${unique_page_id}</c:set>
<c:set var="isDeletedId">isDeleted-${unique_page_id}</c:set>

<script>
	$(document).ready(function() {
		$("#${tabContentId}").find(".addProductReasonButton").button({
		    icons : {
		      primary : 'ui-icon-plusthick'
		    }
		  }).click(function() {
			    var div = $("#${configureProductReasonFormId}").find(".statusChangeReasonDiv")[0];
			    var newDiv = $($(div).clone());
			    console.log(newDiv);
			    newDiv.find('input[name="id"]').val("");
			    newDiv.find('input[name="statusChangeReason"]').val("");
			    newDiv.find('input[name="isDeleted"]').attr('checked',false);
			    $("#${configureProductReasonFormId}").append(newDiv);
		  });
		
		  $("#${tabContentId}").find(".saveProductReasonButton").button({
			    icons : {
			      primary : 'ui-icon-disk'
			    }
			  }).click(function() {
			    var dataId = {};
			   
			    var statusChangeReasonDivs = $("#${configureProductReasonFormId}").find(".statusChangeReasonDiv");
			    for (var index=0; index < statusChangeReasonDivs.length; index++) {
			      var div = $(statusChangeReasonDivs[index]);
			      var id = div.find('input[name="id"]').val();
			      var statusChangeReason = div.find('input[name="statusChangeReason"]').val();
			      var productCategory = div.find('.productCategorySelect option:selected').text();
			      var isDeleted = div.find('input[name="isDeleted"]').is(':checked');
			         
			      if (id == undefined || id == null || id === "")
			      {
			    	  id = statusChangeReason;
			      }
			      dataId[id] = id+"~"+statusChangeReason+"~"+productCategory+"~"+isDeleted;
			    }
			    $.ajax({
			      url: "configureProductStatusChangeReasons.html",
			      data: {dataId: JSON.stringify(dataId)},
			      type: "POST",
			      success: function(response) {
			                 $("#${tabContentId}").replaceWith(response);
			                 showMessage("Product Reason Updated Successfully!");
			               },
			      error:    function(response) {
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
		<b>Configure Product Discard & Return Reasons</b> <br /> <br />
		<div class="tipsBox ui-state-highlight">
			<p>Modify/Add Product discard & return reasons</p>
		</div>
		<div>
			<label style="padding-left: 10px;">Reason</label>
			<label style="padding-left: 140px;">Category</label>
			<label style="padding-left: 60px;">Enabled</label>
		</div>
		<form id="${configureProductReasonFormId}">
				<c:forEach var="statusChangeReasonVar" items="${allProductStatusChangeReasons}">
				<div id="statusChangeReasonDiv" class="statusChangeReasonDiv">
				<div>
					<input type="hidden" name="id" value="${statusChangeReasonVar.id}" /> 
					<input type="text" name="statusChangeReason" id="${statusChangeReasonId}" value="${statusChangeReasonVar.statusChangeReason}" />
					<select name="productCategory" id="${productCategoryId}" class="productCategorySelect">
						<c:forEach items="${productStatusChangeCategories}" var="statusChangeReasonCategory">
									<c:choose>
										<c:when test="${statusChangeReasonCategory eq statusChangeReasonVar.category}">
											<option value="${statusChangeReasonVar}" selected="selected">${statusChangeReasonVar.category}</option>
										</c:when>
										<c:otherwise>
											<option value="${statusChangeReasonVar}">${statusChangeReasonCategory}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select>
						<c:choose>
								<c:when test="${statusChangeReasonVar.isDeleted eq true}">
									<input type="checkbox" name="isDeleted" id="${isDeletedId}" value="${statusChangeReasonVar.isDeleted}"  />
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="isDeleted" id="${isDeletedId}" value="${statusChangeReasonVar.isDeleted}"  checked="checked"/>
								</c:otherwise>
						</c:choose>
						
				</div>
				</div>
				</c:forEach>
		</form>
		<br />
		<div>
			<label>&nbsp;</label>
			<button class="addProductReasonButton">Add new Product Reason</button>
			<button class="saveProductReasonButton">Save</button>
			<button class="cancelButton">Cancel</button>
		</div>

	</div>

	<div id="${childContentId}"></div>

</div>
