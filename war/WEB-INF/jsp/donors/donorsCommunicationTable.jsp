<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">donorsTable-${unique_page_id}</c:set>
<c:set var="childContentId">childContentId-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var donorsTable = $("#${table_id}").dataTable({
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
                                             $("#${tabContentId}").html($("#${noResultsFoundDivId}").html());
                                           }
                                           fnCallback(jsonResponse);
                                         }
                             });
                           },
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
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

      $("#${tabContentId}").find(".donorsTable").bind("refreshResults", refreshResults);

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });

    });
</script>

<div id="${tabContentId}">

  <c:choose>

    <c:when test="${fn:length(model.allDonors) eq -1}">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
    </c:when>

    <c:otherwise>

      <table id="${table_id}" class="dataTable donorsTable">
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
            <c:if test="${model.donorFields.phoneNumber.hidden != true}">
              <th>${model.donorFields.phoneNumber.displayName}</th>
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
              <c:if test="${model.donorFields.phoneNumber.hidden != true}">
                <td>${donor.contactInformation.phoneNumber}</td>
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

</div>

<div id="${noResultsFoundDivId}" style="display: none;">
  <span
    style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
    Sorry no results found matching your search request </span>
</div>
