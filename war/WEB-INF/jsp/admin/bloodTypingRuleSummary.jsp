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

<c:set var="deleteBloodTypingRuleConfirmDialogId">deleteBloodTypingRuleConfirmDialogId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".editButton")
                        .button({icons: {primary: 'ui-icon-pencil'}})
                        .click(function() {
                          console.log("edit button clicked");
                        });

  $("#${mainContentId}").find(".doneButton")
                        .button({icons: {primary: 'ui-icon-check'}})
                        .click(
                            function() {
                              $("#${tabContentId}").parent().trigger("bloodTypingRuleCancel");
                            });

  $("#${mainContentId}").find(".deleteButton")
                        .button({icons: {primary: 'ui-icon-trash'}})
                        .click(
                        function() {

                          $("#${deleteBloodTypingRuleConfirmDialogId}").dialog({
                            modal: true,
                            title: "Confirm delete",
                            width: "400px",
                            resizable: false,
                            buttons: {
                              "Delete": function() {
                                deleteBloodTypingRule();
                                $(this).dialog("close");
                              },
                              "Cancel": function() {
                                $(this).dialog("close");
                              }
                            }
                          });
                        });

  function deleteBloodTypingRule() {
    $.ajax({
      url: "deleteBloodTypingRule.html",
      type: "POST",
      data: {bloodTypingRuleId : '${bloodTypingRule.id}'},
      success: function() {
                 showMessage("Blood Typing Rule successfully deleted");
                 $("#${tabContentId}").parent().trigger("bloodTypingRuleEditDone");
               },
       error:   function() {
                  showErrorMessage("Something went wrong. Please try again");
                   $("#${tabContentId}").parent().trigger("bloodTypingRuleEditError");
                }
    });
  }
});
</script>

<div id="${tabContentId}">
  <div id="${mainContentId}">
    <div style="  border: thin solid #1075A1;  border-radius: 5px;  margin: 20px;">

      <div style="margin-left: 20px; padding-top: 10px; font-weight: bold;">Selected rule</div>

      <div class="summaryPageButtonSection" style="text-align: right;">
        <!-- button class="editButton">Edit</button-->
        <button class="doneButton">Done</button>
        <button class="deleteButton">Delete</button>
      </div>
  
      <div class="ruleDetails">
        <div class="formFormatClass">

          <div>
            <label>Rule no.</label>
            <label>${bloodTypingRule.id}</label>
          </div>

          <c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
            <c:if test="${not empty bloodTypingRule.patternMap[bloodTypingTest.id]}">
              <div>
                <label>
                  ${bloodTypingTest.testNameShort}
                </label>
                <label>
                  ${bloodTypingRule.patternMap[bloodTypingTest.id]}
                </label>
              </div>
            </c:if>
          </c:forEach>

          <div>
            <label>${bloodTypingRule.collectionFieldChanged}</label>
            <label>${bloodTypingRule.newInformation}</label>
          </div>

          <c:if test="${fn:length(bloodTypingRule.pendingTestsIds) gt 0}">
            <div>
              <label><b>Pending tests</b></label>
            </div>
            <c:forEach var="pendingTestId" items="${bloodTypingRule.pendingTestsIds}">
              <div>
                <label>${bloodTypingTestsMap[pendingTestId].testNameShort}</label>
              </div>
            </c:forEach>
          </c:if>
  
        </div>
      </div>

    </div>

  </div>
</div>

<div id="${deleteBloodTypingRuleConfirmDialogId}" style="display: none;">
  Are you sure you want to delete this rule?
</div>
