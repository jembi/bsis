<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">donorCommunicationsTable-${unique_page_id}</c:set>
<c:set var="childContentId">childContentId-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>
<script>
$(document).ready(
		
    function() {
   	 $("#${tabContentId}").find(".cancelDonorCommButton")
        .button({
        	icons : {}
        })
        .click(function() {
                 notifyParentDone();
               });
	$("#${tabContentId}").find(".backDonorCommButton")
	    .button({
	    	icons : {}
	    })
	    .click(function() {
	             notifyParentDone();
	           });
   	 function notifyParentDone() {
        $("#${tabContentId}").parent().trigger("donorFindSuccess");
      }
   	  textName = getDynamicCSVFileName();
      var donorCommunicationsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>',
        "bServerSide" : true,
        "sPaginationType" : "full_numbers",
        "sAjaxSource" : "${model.nextPageUrl}",
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]}
                         ],
        "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
                           oSettings.jqXHR = $.ajax({
                             "datatype": "json",
                             "type": "GET",
                             "url": sSource,
                             "data": aoData,
                             "success": function(jsonResponse) {
                            	 		   if (jsonResponse.iTotalRecords == 0) {
                            	 			  $('#backButtonId').show();
                            	 			  $('#newPosition').hide();
                            	 		   }
                                           fnCallback(jsonResponse);
                                         }
                             });
                           },
        "oTableTools" : {
          "sSwfPath": "plugins/DataTables/extras/TableTools/media/swf/copy_csv_xls_pdf.swf",
          "sRowSelect" : "single",
          "aButtons" : [{
              "sExtends":    "csv",
              "sButtonText": "Export List",
              "mColumns": [1,2,3,4,5,6,7,8,9,10,11 ],
              "sFileName": textName
              }],
          "fnRowSelected" : function(node) {
                              $("#${tabContentId}").parent().trigger("donorSummaryView");
                              var elements = $(node).children();
                              if (elements[0].getAttribute("class") === "dataTables_empty") {
                                return;
                              }
                              var selectedRowId = elements[0].innerHTML;
                              var donorNumber = elements[1].innerHTML;
                              $("#${tabContentId}").parent().trigger("donorSelected", {donorNumber: donorNumber});
                              
                              if ("${createDonorSummaryView}" === "true") {
                                createDonorSummary("${donorRowClickUrl}",  {donorId: selectedRowId});
                              }
                            },
          "fnRowDeselected" : function(node) {
                            },
        }
      });
	$("ZeroClipboard_TableToolsMovie_1").hide();
      function createDonorSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
                     $("#${tabContentId}").trigger("donorSummaryView", response);
                  }
        });
      }

      function refreshResults() {
        showLoadingImage($("#${tabContentId}"));
        $.ajax({url: "${model.refreshUrl}",
                data: {},
                type: "GET",
                success: function(response) {
                           $("#${tabContentId}").html(response);
                         }
        });
      }

      $("#${tabContentId}").find(".donorCommunicationsTable").bind("refreshResults", refreshResults);

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });
      
      var csvButton = $(".DTTT_button_csv").detach();
      $("#newPosition").append( csvButton );
      
      function getDynamicCSVFileName()
      {
    	  var timestamp = $.now();
    	  var d = new Date(timestamp);
    	  var dd = d.getDate();
    	  var mm = d.getMonth() + 1;
    	  var h = d.getHours();
    	  var m = d.getMinutes();
    	  var s = d.getSeconds();
    	  var dateAndTime = (dd < 10 ? "0" : "") + dd + (mm < 10 ? "0" : "") + mm + "" + d.getFullYear() + "-" + (h < 10 ? "0" : "") + h + (m < 10 ? "0" : "") + m + (s < 10 ? "0" : "") + s;
    	  return "ExportList_" + dateAndTime + ".csv";
      }
   });
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONOR)">
<div id="${tabContentId}">
  <c:choose>
    <c:when test="${fn:length(model.allDonors) eq -1}">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
    </c:when>

    <c:otherwise>
      <table id="${table_id}" class="dataTable donorCommunicationsTable">
        <thead>
          <tr>
            <th style="display: none"></th>
            <c:if test="${model.donorFields.donorNumber.hidden != true}">
              <th>${model.donorFields.donorNumber.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.firstName.hidden != true}">
              <th>${model.donorFields.firstName.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.lastName.hidden != true}">
              <th>${model.donorFields.lastName.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.mobileNumber.hidden != true}">
              <th>${model.donorFields.mobileNumber.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.homeNumber.hidden != true}">
              <th>${model.donorFields.homeNumber.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.workNumber.hidden != true}">
              <th>${model.donorFields.workNumber.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.email.hidden != true}">
              <th>${model.donorFields.email.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.contactMethodType.hidden != true}">
              <th>${model.donorFields.contactMethodType.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.dateOfLastDonation.hidden != true}">
              <th>${model.donorFields.dateOfLastDonation.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.bloodGroup.hidden != true}">
              <th>${model.donorFields.bloodGroup.displayName}</th>
            </c:if>
            <c:if test="${model.donorFields.donorPanel.hidden != true}">
              <th>${model.donorFields.donorPanel.displayName}</th>
            </c:if>
          </tr>
        </thead>
        <tbody>
            <c:forEach var="donor" items="${model.allDonors}">
                <tr>
                    <td style="display: none">${donor.id}</td>
                    <c:if test="${model.donorFields.donorNumber.hidden != true}">
                        <td>${donor.donorNumber}</td>
                    </c:if>
                    <c:if test="${model.donorFields.firstName.hidden != true}">
                        <td>${donor.firstName}</td>
                    </c:if>
                    <c:if test="${model.donorFields.lastName.hidden != true}">
                        <td>${donor.lastName}</td>
                    </c:if>
                    <c:if test="${model.donorFields.mobileNumber.hidden != true}">
                        <td>${donor.mbileNumber}</td>
                    </c:if>
                    <c:if test="${model.donorFields.homeNumber.hidden != true}">
                        <td>${donor.contact.homeNumber}</td>
                     </c:if>
                     <c:if test="${model.donorFields.workNumber.hidden != true}">
                        <td>${donor.contact.workNumber}</td>
                     </c:if>
                     <c:if test="${model.donorFields.email.hidden != true}">
                        <td>${donor.contact.email}</td>
                     </c:if>
                     <c:if test="${model.donorFields.contactMethodType.hidden != true}">
                        <td>${donor.contactMethodType}</td>
                     </c:if>
                     <c:if test="${model.donorFields.dateOfLastDonation.hidden != true}">
                        <td>${donor.dateOfLastDonation}</td>
                    </c:if>
                    <c:if test="${model.donorFields.bloodGroup.hidden != true}">
                        <td>${donor.bloodGroup}</td>
                    </c:if>
                    <c:if test="${model.donorFields.donorPanel.hidden != true}">
                        <td>${donor.donorPanel}</td>
                    </c:if>
                </tr>
            </c:forEach>
        </tbody>
      </table>

    </c:otherwise>
  </c:choose>
   <div id="newPosition">     
        <label></label>
        <br>
        <button type="button" class="cancelDonorCommButton">
          Close
        </button>
    </div>
    <div id="backButtonId" style="display: none;">
    <label></label>
    <br>
    <button type="button" class="backDonorCommButton">
          Close
        </button>
    </div>
    <div id="${childContentId}"></div>
</div>
</sec:authorize>