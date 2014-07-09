<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="addDonationBatchFormId">addDonationBatchForm-${unique_page_id}</c:set>
<c:set var="addDonationBatchFormCentersId">addDonationBatchFormCenters-${unique_page_id}</c:set>
<c:set var="addDonationBatchFormSitesId">addDonationBatchFormSites-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>


<script>
  $(document).ready(
      function() {
          
       $("#${addDonationBatchFormId}").find("#batchOpenedOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 1,
          dateFormat : "dd/mm/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        function notifyParentSuccess() {
            // let the parent know we are done
            $("#${tabContentId}").parent().trigger("editDonationBatchSuccess");
        }
  
        function notifyParentCancel() {
          $("#${tabContentId}").parent().trigger("editDonationBatchCancel");
        }

        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(notifyParentCancel);

        $("#${tabContentId}").find(".addDonationBatchButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
                addNewDonationBatch($("#${addDonationBatchFormId}")[0],
                                      "${tabContentId}", notifyParentSuccess);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${addDonationBatchFormId}").printArea();
        });

        $("#${addDonationBatchFormCentersId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${addDonationBatchFormSitesId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${tabContentId}").find(".clearFormButton").button({
          icons : {            
          }
        }).click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
                        $("#${tabContentId}").replaceWith(response);
                     },
            error:   function (response) {
                       showErrorMessage("Something went wrong. Please try again.");
                     }
            
          });
        }

        if ("${firstTimeRender}" == "true") {
          // just set the default values for the new donation  
          $("#${tabContentId}").find('textarea[name="notes"]').html("${donationBatchFields.notes.defaultValue}");
          setDefaultValueForSelector(getDonationCenterSelector(), "${donationBatchFields.donationCenter.defaultValue}");
          setDefaultValueForSelector(getDonationSiteSelector(), "${donationBatchFields.donationSite.defaultValue}");
        }

        function getDonationCenterSelector() {
          return $("#${tabContentId}").find('select[name="donationCenter"]').multiselect();
        }

        function getDonationSiteSelector() {
          return $("#${tabContentId}").find('select[name="donationSite"]').multiselect();
        }

    });
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_DONATION_BATCH)">
<div id="${tabContentId}">

  <c:if test="${!empty success && !success}">
      <jsp:include page="../common/errorBox.jsp">
        <jsp:param name="errorMessage" value="${errorMessage}" />
      </jsp:include>
  </c:if>

  <form:form method="POST" commandName="addDonationBatchForm"
    class="formFormatClass" id="${addDonationBatchFormId}">
    <c:if test="${!donationBatchFields.batchNumber.autoGenerate}">
      <c:if test="${donationBatchFields.batchNumber.hidden != true }">
        <div>
          <form:label path="batchNumber">${donationBatchFields.batchNumber.displayName}</form:label>
          <form:input path="batchNumber" value="${firstTimeRender ? donationBatchFields.batchNumber.defaultValue : ''}" />
          <form:errors class="formError"
            path="donationBatch.batchNumber" delimiter=", "></form:errors>
        </div>
      </c:if>
    </c:if>
    <c:if test="${donationBatchFields.donationCenter.hidden != true }">
      <div>
        <form:label path="donationCenter">${donationBatchFields.collectionCenter.displayName}</form:label>
        <form:select path="donationCenter" id="${addDonationBatchFormCentersId}" class="addDonationBatchFormCenters">
          <form:option value="" selected="selected">&nbsp;</form:option>
          <c:forEach var="center" items="${centers}">
            <form:option value="${center.id}">${center.name}</form:option>
          </c:forEach>
        </form:select>
        <form:errors class="formError" path="donationBatch.donationCenter" delimiter=", "></form:errors>
      </div>
    </c:if>
    <c:if test="${donationBatchFields.donationSite.hidden != true }">
      <div>
        <form:label path="donationSite">${donationBatchFields.collectionSite.displayName}</form:label>
        <form:select path="donationSite" id="${addDonationBatchFormSitesId}"
          class="addDonationBatchFormSites">
          <form:option value="" selected="selected">&nbsp;</form:option>
          <c:forEach var="site" items="${sites}">
            <form:option value="${site.id}">${site.name}</form:option>
          </c:forEach>
        </form:select>
        <form:errors class="formError" path="donationBatch.donationSite" delimiter=", "></form:errors>
      </div>
    </c:if>
    <c:if test="${donationBatchFields.batchOpenedOn.hidden != true }">
      <div>
        <form:label path="batchOpenedOn">${donationBatchFields.batchOpenedOn.displayName}</form:label>
        <form:input path="batchOpenedOn" placeholder = "Opening Time"/>
        <form:errors class="formError" path="batchOpenedOn"
          delimiter=", "></form:errors>
      </div>
    </c:if>
    <c:if test="${donationBatchFields.notes.hidden != true }">
      <div>
        <form:label path="notes" class="labelForTextArea">${donationBatchFields.notes.displayName}</form:label>
        <form:textarea path="notes" />
        <form:errors class="formError" path="donationBatch"
          delimiter=", "></form:errors>
      </div>
    </c:if>
    </form:form>

    <div style="margin-left: 200px;">
      <label></label>
      <button type="button" class="addDonationBatchButton autoWidthButton">
        Add Donation Batch
      </button>
      <button type="button" class="clearFormButton autoWidthButton">
        Clear form
      </button>        
    </div>

</div>
</sec:authorize>
