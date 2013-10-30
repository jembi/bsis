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

<c:set var="deactivateComponentProcessingConfirmDialogId">deactivateComponentProcessingConfirmDialogId-${unique_page_id}</c:set>
<c:set var="activateComponentProcessingConfirmDialogId">activateComponentProcessingConfirmDialogId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".editButton")
                        .button({icons: {primary: 'ui-icon-pencil'}})
                        .click(function() {
                        	 $("#${tabContentId}").parent().trigger("editcomponentProcessing");
                        });

  $("#${mainContentId}").find(".doneButton")
                        .button()
                        .click(
                            function() {
                              $("#${tabContentId}").parent().trigger("componentProcessingCancel");
                            });

  $("#${mainContentId}").find(".deactivateButton")
                        .button()
                        .click(
                        function() {

                          $("#${deactivateComponentProcessingConfirmDialogId}").dialog({
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
      url: "deactivateComponentProcessing.html",
      type: "POST",
      data: {componentProcessID : '${componentProcessing.id}'},
      success: function() {
                 showMessage("Blood Test successfully deactivated");
                 $("#${tabContentId}").parent().trigger("componentProcessingEditDone");
               },
       error:   function() {
                  showErrorMessage("Something went wrong. Please try again");
                   $("#${tabContentId}").parent().trigger("componentProcessingEditError");
                }
    });
  }

  $("#${mainContentId}").find(".activateButton")
  .button()
  .click(
  function() {

    $("#${activateComponentProcessingConfirmDialogId}").dialog({
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
        url : "activateComponentProcessing.html",
        type : "POST",
        data: {componentProcessID : '${componentProcessing.id}'},
        success : function() {
          showMessage("Blood Test successfully activated");
          $("#${tabContentId}").parent().trigger("componentProcessingEditDone");
        },
        error : function() {
          showErrorMessage("Something went wrong. Please try again");
          $("#${tabContentId}").parent().trigger("componentProcessingEditError");
        }
      });
  }

});
</script>

<div id="${tabContentId}">
  <div id="${mainContentId}">
    <div style="  border: thin solid #1075A1;  border-radius: 5px;  margin: 20px;">

      <div style="margin-left: 20px; padding-top: 10px; font-weight: bold;">Selected product type</div>

      <div class="summaryPageButtonSection" style="text-align: right;">
        <button class="editButton">Edit</button>
        <button class="doneButton">Done</button>
        <c:if test="${not componentProcessing.isDeleted}">
          <button class="deactivateButton">Disable</button>
        </c:if>
        <c:if test="${componentProcessing.isDeleted}">
          <button class="activateButton">Enable</button>
        </c:if>
      </div>
  
      <div class="productTypeDetails">
        <div class="formFormatClass">

          <div>
            <label>Source Product</label>
            <label>${componentProcessing.productType.productType}</label>
          </div>

          <div>
            <label>Units Min</label>
            <label>${componentProcessing.unitsMin}</label>
          </div>

          <div>
            <label>Units Max</label>
            <label>${componentProcessing.unitsMax}</label>
          </div>

        </div>
      </div>

    </div>

  </div>
</div>

<div id="${deactivateComponentProcessingConfirmDialogId}" style="display: none;">
  Are you sure you want to deactivate this Component Processing?
</div>

<div id="${activateComponentProcessingConfirmDialogId}" style="display: none;">
  Are you sure you want to activate this Component Processing?
</div>
