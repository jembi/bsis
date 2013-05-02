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

<c:set var="bloodTypingMechanismSelectorId">bloodTypingMechanismSelector-${unique_page_id}</c:set>
<c:set var="ttiMechanismSelectorId">ttiMechanismSelector-${unique_page_id}</c:set>
<c:set var="recordUsageSelectorId">recordUsageSelector-${unique_page_id}</c:set>
<c:set var="crossmatchProcedureSelectorId">crossmatchProcedureSelector-${unique_page_id}</c:set>

<script>
$(document).ready(function(){

  $("#${bloodTypingMechanismSelectorId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${ttiMechanismSelectorId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${recordUsageSelectorId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${crossmatchProcedureSelectorId}").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  setDefaultValueForSelectorUsingValue($("#${bloodTypingMechanismSelectorId}").multiselect(), "${labsetup['bloodTypingMechanism']}");
  setDefaultValueForSelectorUsingValue($("#${ttiMechanismSelectorId}").multiselect(), "${labsetup['ttiMechanism']}");
  setDefaultValueForSelectorUsingValue($("#${recordUsageSelectorId}").multiselect(), "${labsetup['recordUsage']}");
  setDefaultValueForSelectorUsingValue($("#${crossmatchProcedureSelectorId}").multiselect(), "${labsetup['crossmatchProcedure']}");

  $("#${mainContentId}").find(".updateLabSetupButton")
                        .button()
                        .click(function() {
                          var labSetupData = {};
                          var selects = $("#${mainContentId}").find("select");
                          for (var index = 0; index < selects.length; ++index) {
                            var select = $(selects[index]);
                            labSetupData[select.prop('name')] = select.val(); 
                          }

                          $.ajax({
                            url: "updateLabSetup.html",
                            data: {labSetupParams : JSON.stringify(labSetupData)},
                            type: "POST",
                            success: function(response) {
                                       showMessage("Lab Setup successfully updated. Please wait while the page is refreshed.");
                                       setTimeout(function() {
                                                     document.location.reload(true);
                                                   }, 3000);
                                     },
                            error:   function(response) {
                                       showErrorMessage("Something went wrong. Please try again.");                              
                                     }
                          });
                        });
  
});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">

    <form class="formFormatClass">

      <div>
        <label>
          <b>Lab setup</b>
        </label>
      </div>

      <div class="tipsBox ui-state-highlight">
        <p>
          Customize as per the workflow of your lab
        </p>
      </div>

      <div>
        <label>Blood typing mechanism</label>
        <select name="bloodTypingMechanism"
                id="${bloodTypingMechanismSelectorId}">
          <option value="BLOODTYPING_TEST_RESULTS_ELISA">Record blood typing results (ELISA plate)</option>
          <option value="BLOODTYPING_TEST_RESULTS_WORKSHEETS">Record blood typing results (Worksheets)</option>
          <option value="BLOODTYPING_OUTCOMES_WORKSHEETS">Only record blood typing outcomes (Worksheets)</option>
        </select>
      </div>

      <div>
        <label>TTI recording mechanism</label>
        <select name="ttiMechanism"
                id="${ttiMechanismSelectorId}">
          <option value="TTI_ELISA">Record on ELISA Plate (with Optical Densities)</option>
          <option value="TTI_WORKSHEETS">Record on worksheets</option>
        </select>
      </div>

      <div>
        <label>Record usage information</label>
        <select name="recordUsage"
                id="${recordUsageSelectorId}">
          <option value="true">Yes</option>
          <option value="false">No</option>
        </select>
      </div>

      <div>
        <label>Crossmatch procedure</label>
        <select name="crossmatchProcedure"
                id="${crossmatchProcedureSelectorId}">
          <option value="CROSSMATCH_NOT_DONE">Crossmatch testing is not done</option>
          <option value="CROSSMATCH_DONE_CAN_SKIP">Crossmatch testing is done but can be skipped</option>
          <option value="CROSSMATCH_DONE_CANNOT_SKIP">Crossmatch testing is mandatory</option>
        </select>
      </div>

    </form>

    <button class="updateLabSetupButton">
    Update lab setup
    </button>

  </div>

</div>