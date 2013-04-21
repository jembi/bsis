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

<c:set var="deactivateProductTypeConfirmDialogId">deactivateProductTypeConfirmDialogId-${unique_page_id}</c:set>
<c:set var="activateProductTypeConfirmDialogId">activateProductTypeConfirmDialogId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".editButton")
  											.button({icons: {primary: 'ui-icon-pencil'}})
  											.click(function() {
  											  console.log("edit button clicked");
  											});

  $("#${mainContentId}").find(".doneButton")
												.button()
												.click(
												    function() {
												      $("#${tabContentId}").parent().trigger("productTypeCancel");
												    });

  $("#${mainContentId}").find(".deactivateButton")
  											.button()
  											.click(
  											function() {

  											  $("#${deactivateProductTypeConfirmDialogId}").dialog({
  											    modal: true,
  											    title: "Disable",
  											    width: "400px",
  											    resizable: false,
  											    buttons: {
  											      "Disable": function() {
  											        deactivateProductType();
  											        $(this).dialog("close");
  											      },
  											      "Cancel": function() {
  											        $(this).dialog("close");
  											      }
  											    }
  											  });
  											});

  function deactivateProductType() {
    $.ajax({
      url: "deactivateProductType.html",
      type: "POST",
      data: {productTypeId : '${productType.id}'},
      success: function() {
        				 showMessage("Blood Test successfully deactivated");
        				 $("#${tabContentId}").parent().trigger("productTypeEditDone");
      				 },
     	error:   function() {
     	  			   showErrorMessage("Something went wrong. Please try again");
     	  			  	$("#${tabContentId}").parent().trigger("productTypeEditError");
     					 }
    });
  }

  $("#${mainContentId}").find(".activateButton")
	.button()
	.click(
	function() {

	  $("#${activateProductTypeConfirmDialogId}").dialog({
	    modal: true,
	    title: "Activate",
	    width: "400px",
	    resizable: false,
	    buttons: {
	      "Activate": function() {
	        activateProductType();
	        $(this).dialog("close");
	      },
	      "Cancel": function() {
	        $(this).dialog("close");
	      }
	    }
	  });
	});

	function activateProductType() {

	  $.ajax({
        url : "activateProductType.html",
        type : "POST",
        data : {
          productTypeId : '${productType.id}'
        },
        success : function() {
          showMessage("Blood Test successfully activated");
          $("#${tabContentId}").parent().trigger("productTypeEditDone");
        },
        error : function() {
          showErrorMessage("Something went wrong. Please try again");
          $("#${tabContentId}").parent().trigger("productTypeEditError");
        }
      });
  }

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<div style="	border: thin solid #1075A1;	border-radius: 5px;	margin: 20px;">

			<div style="margin-left: 20px; padding-top: 10px; font-weight: bold;">Selected blood test</div>

			<div class="summaryPageButtonSection" style="text-align: right;">
				<!-- button class="editButton">Edit</button-->
				<button class="doneButton">Done</button>
				<c:if test="${not productType.isDeleted}">
					<button class="deactivateButton">Disable</button>
				</c:if>
				<c:if test="${productType.isDeleted}">
					<button class="activateButton">Enable</button>
				</c:if>
			</div>
	
			<div class="productTypeDetails">
				<div class="formInTabPane">

					<div>
						<label>Product type name</label>
						<label>${productType.productType}</label>
					</div>

					<div>
						<label>Short name</label>
						<label>${productType.productTypeNameShort}</label>
					</div>

					<div>
						<label>Expiry time</label>
						<label>${productType.expiresAfter} ${productType.expiresAfterUnits}</label>
					</div>

				</div>
			</div>

		</div>

	</div>
</div>

<div id="${deactivateProductTypeConfirmDialogId}" style="display: none;">
	Are you sure you want to deactivate this product type?
</div>

<div id="${activateProductTypeConfirmDialogId}" style="display: none;">
	Are you sure you want to activate this product type?
</div>
