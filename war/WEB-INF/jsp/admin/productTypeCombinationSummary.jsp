<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
  <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<c:set var="deactivateProductTypeCombinationConfirmDialogId">deactivateProductTypeCombinationConfirmDialogId-${unique_page_id}</c:set>
<c:set var="activateProductTypeCombinationConfirmDialogId">activateProductTypeCombinationConfirmDialogId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".editButton")
                        .button({icons: {primary: 'ui-icon-pencil'}})
                        .click(function() {
                          $("#${tabContentId}").parent().trigger("editProductTypeCombination");
                        });

  $("#${mainContentId}").find(".doneButton")
                        .button()
                        .click(
                            function() {
                              $("#${tabContentId}").parent().trigger("productTypeCombinationCancel");
                            });

  $("#${mainContentId}").find(".deactivateButton")
                        .button()
                        .click(
                        function() {

                          $("#${deactivateProductTypeCombinationConfirmDialogId}").dialog({
                            modal: true,
                            title: "Disable",
                            width: "400px",
                            resizable: false,
                            buttons: {
                              "Disable": function() {
                                deactivateProductTypeCombination();
                                $(this).dialog("close");
                              },
                              "Cancel": function() {
                                $(this).dialog("close");
                              }
                            }
                          });
                        });

  function deactivateProductTypeCombination() {
    $.ajax({
      url: "deactivateProductTypeCombination.html",
      type: "POST",
      data: {productTypeCombinationId : '${productTypeCombination.id}'},
      success: function() {
                 showMessage("Blood Test successfully deactivated");
                 $("#${tabContentId}").parent().trigger("productTypeCombinationEditDone");
               },
       error:   function() {
                  showErrorMessage("Something went wrong. Please try again");
                   $("#${tabContentId}").parent().trigger("productTypeCombinationEditError");
                }
    });
  }

  $("#${mainContentId}").find(".activateButton")
  .button()
  .click(
  function() {

    $("#${activateProductTypeCombinationConfirmDialogId}").dialog({
      modal: true,
      title: "Activate",
      width: "400px",
      resizable: false,
      buttons: {
        "Activate": function() {
          activateProductTypeCombination();
          $(this).dialog("close");
        },
        "Cancel": function() {
          $(this).dialog("close");
        }
      }
    });
  });

  function activateProductTypeCombination() {

    $.ajax({
        url : "activateProductTypeCombination.html",
        type : "POST",
        data : {
          productTypeCombinationId : '${productTypeCombination.id}'
        },
        success : function() {
          showMessage("Blood Test successfully activated");
          $("#${tabContentId}").parent().trigger("productTypeCombinationEditDone");
        },
        error : function() {
          showErrorMessage("Something went wrong. Please try again");
          $("#${tabContentId}").parent().trigger("productTypeCombinationEditError");
        }
      });
  }

});
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_COMPONENT_COMBINATIONS)">
<div id="${tabContentId}">
  <div id="${mainContentId}">
    <div style="  border: thin solid #1075A1;  border-radius: 5px;  margin: 20px;">

      <div style="margin-left: 20px; padding-top: 10px; font-weight: bold;">Selected product type combination</div>

      <div class="summaryPageButtonSection" style="text-align: right;">
        <!-- No edit functionality required for product type combinations -->
        <!-- button class="editButton">Edit</button-->
        <button class="doneButton">Done</button>
        <c:if test="${not productTypeCombination.isDeleted}">
          <button class="deactivateButton">Disable</button>
        </c:if>
        <c:if test="${productTypeCombination.isDeleted}">
          <button class="activateButton">Enable</button>
        </c:if>
      </div>
  
      <div class="productTypeCombinationDetails">
        <div class="formFormatClass">

          <div>
            <label>Combination name</label>
            <label style="width: auto;">${productTypeCombination.combinationName}</label>
          </div>

          <div>
            <label><b>Product types</b></label>
            <ul>
              <c:forEach var="productType" items="${productTypeCombination.productTypes}">
                <li>${productType.productType}</li>
              </c:forEach>
            </ul>
          </div>

        </div>
      </div>

    </div>

  </div>
</div>

<div id="${deactivateProductTypeCombinationConfirmDialogId}" style="display: none;">
  Are you sure you want to deactivate this product type combination?
</div>

<div id="${activateProductTypeCombinationConfirmDialogId}" style="display: none;">
  Are you sure you want to activate this product type combination?
</div>
</sec:authorize>