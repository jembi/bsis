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

<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>
<c:set var="deferDonorDialogId">deferDonorDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

       // showBarcode($("#${tabContentId}").find(".donorBarcode"), "${donor.donorNumber}");
       // showDonorDeferrals();  display deferral history by default

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("donorSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
                primary : 'ui-icon-pencil'
              }
            }).click(function() {

            hideMainContent();
            $("#${tabContentId}").bind("editDonorSuccess", editDonorDone);
            $("#${tabContentId}").bind("editDonorCancel", editDonorDone);

            fetchContent("editDonorFormGenerator.html",
                         {donorId: "${donor.id}"},
                         $("#${tabContentId}")
                        );
        });

        $("#${tabContentId}").find(".printButton").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${mainContentId}").find(".printableArea").printArea();
        });

        $("#${tabContentId}").find(".createCollectionButton").button({
          icons : {
            primary : 'ui-icon-disk'
          }
        }).click(function() {
          $("#${tabContentId}").bind("editCollectionSuccess", editCollectionDone);
          $("#${tabContentId}").bind("editCollectionCancel", editCollectionDone);
          //hideMainContent();
          fetchContent("addCollectionFormForDonorGenerator.html",
                       {donorId: "${donor.id}"},
                       $("#${childContentId}")
                      );
        });

        $("#${tabContentId}").find(".donorHistoryButton").button({
          icons : {
            primary : 'ui-icon-document'
          }
        }).click(function() {
          //hideMainContent();
          $("#${tabContentId}").bind("donorHistoryDone", editDonorDone);
          fetchContent("viewDonorHistory.html",
                       {donorId: "${donor.id}"},
                       $("#${childContentId}")
                      );
        });

        $("#${mainContentId}").find(".doneButton")
                              .button({icons : {primary : 'ui-icon-check'}})
                              .click(function() {
                                       notifyParentDone();
                                     });

        $("#${mainContentId}").find(".deleteButton")
                              .button({icons : {primary : 'ui-icon-trash'}})
                              .click(function() {
                                      $("#${deleteConfirmDialogId}").dialog(
                                          {
                                            modal : true,
                                            title : "Confirm Delete",
                                            buttons : {
                                              "Delete" : function() {
                                                deleteDonor("${donor.id}", notifyParentDone);
                                                $(this).dialog("close");
                                              },
                                              "Cancel" : function() {
                                                $(this).dialog("close");
                                              }
                                            }
                                          });
                                    });

        $("#${mainContentId}").find(".deferDonorButton")
                              .button({icons: {primary : 'ui-icon-closethick'}})
                              .click(generateDeferDonorDialog);

        function generateDeferDonorDialog() {
          $("<div>").dialog({
            modal: true,
            open:  function() {
                     // extra parameters passed as string otherwise POST request sent
                     $(this).load("deferDonorFormGenerator.html?" + $.param({donorId : "${donor.id}"}));
                    },
            close: function(event, ui) {
                     $(this).remove();
                    },
            title: "Defer donor",
            height: 400,
            width: 600,
            buttons:
              {
                 "Defer Donor": function() {
                                 $(this).dialog("close");
                                 deferDonor($(this).find(".deferDonorForm"), deferDonorDone);
                                },
                 "Cancel": function() {
                              $(this).dialog("close");                                                        
                            }
              }
          });
         }

        function editDonorDone() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function editCollectionDone() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function emptyChildContent() {
          $("#${childContentId}").remove();
        }

        function hideMainContent() {
          $("#${mainContentId}").remove();
        }

        function deferDonorDone() {
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        $("#${mainContentId}").find(".donorDeferralsButton")
                              .button({icons : {primary : 'ui-icon-document'}})
                              .click(showDonorDeferrals);

        function showDonorDeferrals() {
          $("#${tabContentId}").bind("donorDeferralsDone", editDonorDone);
          fetchContent("viewDonorDeferrals.html",
                       {donorId: "${donor.id}"},
                       $("#${childContentId}")
                      );
        }
        
        function showDonorDeferralStatus() {
        	
        }

      });
</script>

<div id="${tabContentId}">
  <div id="${mainContentId}">

    <div class="summaryPageButtonSection" style="text-align: right;">
      <button class="doneButton">
        Done
      </button>
      <sec:authorize access="hasRole('PERM_EDIT_INFORMATION')">
      <button class="editButton">
        Edit
      </button>
      </sec:authorize>
      <button class="donorDeferralsButton">
        Show all deferrals
      </button>
      <button class="donorHistoryButton">
        View Donation History
      </button>
      <button class="deferDonorButton">
        Defer Donor
      </button>
      <sec:authorize access="hasRole('PERM_EDIT_INFORMATION')">
      <button type="button" class="deleteButton">
        Delete
      </button>
      </sec:authorize>
      <button class="printButton">
        Print
      </button>
    </div>

    <!-- not needed  
    <div class="tipsBox ui-state-highlight">
      <p>
        ${tips['donors.finddonor.donorsummary']}
      </p>
    </div>
    -->

	  <c:if test="${isDonorCurrentlyDeferred}">
        <div id="deferralStatus" class="tipsBox ui-state-highlight" style="font-size: 18px; color: #a12020; padding: 10px;">
          Donor is currently deferred until ${donorLatestDeferredUntilDate}.
          <br />
        </div>
      </c:if>

  	<br />
  	<div id="${childContentId}"></div>
    <br />

    <jsp:include page="donorDetails.jsp" />

  </div>


</div>

<div id="${deleteConfirmDialogId}" style="display: none;">
  Are you sure you want to delete this Donor?
</div>
