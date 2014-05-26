<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<c:set var="findTestResultFormId">findTestResultForm-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".findTestResultButton")
                       .button({ icons : {primary : 'ui-icon-search'}})
                       .click(
                          function() {
                            var findTestResultFormData = $("#${findTestResultFormId}").serialize();
                            var resultsDiv = $("#${mainContentId}").find(".findTestResults");
                            showLoadingImage(resultsDiv);
                            $.ajax({
                              type : "GET",
                              url : "findTestResult.html",
                              data : findTestResultFormData,
                              success : function(data) {
                                         resultsDiv.html(data);
                                         window.scrollTo(0, document.body.scrollHeight);
                                        },
                              error : function(data) {
                                       showErrorMessage("Something went wrong. Please try again later.");        
                                      }
                            });
                          });

  $("#${tabContentId}").find(".clearFindFormButton").button({
    icons : {
      
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

  $("#${findTestResultFormId}").submit(function(event) {
    event.preventDefault();
  });

});
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_TEST_OUTCOME)">
<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Find Test Results</b>
    <div class="tipsBox ui-state-highlight" style="display:none;">
      <p>
        ${tips['testResults.find']}
      </p>
    </div>
    <form:form method="GET" commandName="findTestResultForm" id="${findTestResultFormId}"
      class="formFormatClass">
  
      <div class="collectionNumberInput">
        <form:label path="collectionNumber">${collectedSampleFields.collectionNumber.displayName}</form:label>
        <!-- Spring supports dynamic attributes so placeholder can be added
             http://stackoverflow.com/questions/4232983/adding-html5-placeholder-attribute-to-spring-3-0-form-input-elements
         -->
        <form:input path="collectionNumber" placeholder="Donation Identification Number"/>
      </div>
    </form:form>

    <div class="formFormatClass">
      <div>
        <label></label>
        <button type="button" class="findTestResultButton">
          Find test results
        </button>
        <button type="button" class="clearFindFormButton">
          Clear form
        </button>
      </div>
    </div>
    
    <div class="findTestResults"></div>
  </div>

  <div id="${childContentId}"></div>
  
</div>
</sec:authorize>