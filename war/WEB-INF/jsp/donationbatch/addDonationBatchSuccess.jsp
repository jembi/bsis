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
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
    <script>
        $(document).ready(function() {

            $("#${mainContentId}").find("#batchClosedOn").datetimepicker({
                changeMonth: true,
                changeYear: true,
                minDate: -36500,
                maxDate: 1,
                dateFormat: "dd/mm/yy",
                timeFormat: "hh:mm:ss tt",
                yearRange: "c-100:c0",
            });

            $("#closeBatchDiv").hide();

            $("#${tabContentId}").find(".printButton").button({
                icons: {
                    primary: 'ui-icon-print'
                }
            }).click(function() {
                $("#${mainContentId}").find(".printableArea").printArea();
            });

            $("#${tabContentId}").find(".closeButton").button({
                icons: {
                    primary: 'ui-icon-check'
                }
            }).click(function() {

                $("#closeBatchDiv").show();

            });
           
           $("#${tabContentId}").find(".confirmButton").button({
                icons: {
                    primary: 'ui-icon-plusthick'
                }
            }).click(function() {
                 $.ajax({
            url: "closeDonationBatch.html",
            data: {id : $('input[name="id"]').val(), batchClosedOn : $('input[name="batchClosedOn"]').val()},
            type: "GET",
            success: function (response) {
                        $("#${tabContentId}").replaceWith(response);
                     },
            error:   function (response) {
                       showErrorMessage("Something went wrong. Please try again.");
                     }
            
          });
            });
            $("#${tabContentId}").find(".cancelButton").button({
                icons: {
                    primary: 'ui-icon-plusthick'
                }
            }).click(function() {
                $("#closeBatchDiv").hide();

            });
        

        });
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_DONATION_BATCH)">
    <div id="${tabContentId}">

        <div id="${mainContentId}">
            <div class="successBox ui-state-highlight">
                <img src="images/check_icon.png"
                     style="height: 30px; padding-left: 10px; padding-right: 10px;" />
                <span class="successText">
                    Collection batch added successfully.
                    <br />
                    You can view the details below. Click "Add another collection batch" to add another collection batch.
                </span>
            </div>
            <div>
                <div class="summaryPageButtonSection" style="text-align: right;">
                    <button type="button" class="closeButton">
                        Close Batch
                    </button>
                    <button type="button" class="printButton">
                        Print
                    </button>
                </div>
            </div>
            <div class="formFormatClass">
                <br />
                <form:hidden path="donationBatch.id"/>
                <c:if test="${donationBatchFields.batchNumber.hidden != true }">
                    <div>
                        <label>${donationBatchFields.batchNumber.displayName}</label>
                        <label>${donationBatch.batchNumber}</label>
                    </div>
                </c:if>
                <c:if test="${donationBatchFields.collectionCenter.hidden != true }">
                    <div>
                        <label>${donationBatchFields.collectionCenter.displayName}</label>
                        <label>${donationBatch.donationCenter}</label>
                    </div>
                </c:if>
                <c:if test="${donationBatchFields.collectionSite.hidden != true }">
                    <div>
                        <label>${donationBatchFields.collectionSite.displayName}</label>
                        <label>${donationBatch.donationSite}</label>
                    </div>
                </c:if>
                <c:if test="${donationBatchFields.collectionSite.hidden != true }">
                    <div>
                        <label>${donationBatchFields.collectionSite.displayName}</label>
                        <label>${donationBatch.donationSite}</label>
                    </div>
                </c:if>
                <c:if test="${donationBatchFields.batchOpenedOn.hidden != true }">
                    <div>
                        <label>${donationBatchFields.batchOpenedOn.displayName}</label>
                        <label>${donationBatch.batchOpenedOn}</label>
                    </div>
                </c:if>
                <c:if test="${donationBatchFields.notes.hidden != true }">
                    <div>
                        <label>${donationBatchFields.notes.displayName}</label>
                        <label>${donationBatch.notes}</label>
                    </div>
                </c:if>
                <div>
                    <label>${donationBatchFields.lastUpdatedTime.displayName}</label>
                    <label style="width: auto;">${donationBatch.lastUpdated}</label>
                </div>
                <div>
                    <label>${donationBatchFields.lastUpdatedBy.displayName}</label>
                    <label style="width: auto;">${donationBatch.lastUpdatedBy}</label>
                </div>
                <div id="closeBatchDiv">
                    <div>
                        <label path="">${donationBatchFields.batchClosedOn.displayName}</label>
                        <form:input path="donationBatch.batchClosedOn" placeholder = "Closing Time" />
                    </div>

                    <div>
                        <button type="button" class="confirmButton">
                            Confirm
                        </button>
                        <button type="button" class="cancelButton">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>

        </div>
    </sec:authorize>