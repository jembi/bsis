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
<c:set var="table_id">donorDeferralsTable-${unique_page_id}</c:set>
<c:set var="childContentId">childContentId-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var donorDeferralsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lfrT>t<"F"ip>',
        "oTableTools" : {
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
                            },
          "fnRowDeselected" : function(node) {
                            },
        },
        "bPaginate" : false
      });

      $("#${tabContentId}").find(".doneButton").button({
        icons : {
          primary : 'ui-icon-check'
        }
      }).click(function() {
        $("#${tabContentId}").parent().trigger("donorDeferralsDone");
      });

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });

    });
</script>

<div id="${tabContentId}">

  <button class="doneButton">Done</button>
  <br />
  <br />
  <c:choose>

    <c:when test="${fn:length(allDonorDeferrals) eq 0}">
      <span
        style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
        Sorry no results found matching your search request </span>
    </c:when>

    <c:otherwise>

      <c:if test="${isDonorCurrentlyDeferred}">
        <div class="tipsBox ui-state-highlight" style="font-size: 18px; color: #a12020; padding: 10px;">
          Donor is currently deferred and cannot donate blood.
          <br />
        </div>
      </c:if>
      <c:if test="${!isDonorCurrentlyDeferred}">
        <div class="tipsBox ui-state-highlight" style="font-size: 18px; padding: 10px;">
          Donor is not deferred currently.
        </div>
      </c:if>

      <table id="${table_id}" class="dataTable donorDeferralsTable">
        <thead>
          <tr>
            <th style="display: none"></th>
            <c:if test="${donorDeferralFields.deferredOn.hidden != true}">
              <th>${donorDeferralFields.deferredOn.displayName}</th>
            </c:if>
            <c:if test="${donorDeferralFields.deferredUntil.hidden != true}">
              <th>${donorDeferralFields.deferredUntil.displayName}</th>
            </c:if>
            <c:if test="${donorDeferralFields.deferredBy.hidden != true}">
              <th>${donorDeferralFields.deferredBy.displayName}</th>
            </c:if>
            <c:if test="${donorDeferralFields.deferralReason.hidden != true}">
              <th>${donorDeferralFields.deferralReason.displayName}</th>
            </c:if>
            <c:if test="${donorDeferralFields.deferralReasonText.hidden != true}">
              <th>${donorDeferralFields.deferralReasonText.displayName}</th>
            </c:if>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="donorDeferral" items="${allDonorDeferrals}">
            <tr>
              <td style="display: none">${donorDeferral.id}</td>
              <c:if test="${donorDeferralFields.deferredOn.hidden != true}">
                <td>${donorDeferral.deferredOn}</td>
              </c:if>
              <c:if test="${donorDeferralFields.deferredUntil.hidden != true}">
                <td>${donorDeferral.deferredUntil}</td>
              </c:if>
              <c:if test="${donorDeferralFields.deferredBy.hidden != true}">
                <td>${donorDeferral.deferredBy}</td>
              </c:if>
              <c:if test="${donorDeferralFields.deferralReason.hidden != true}">
                <td>${donorDeferral.deferralReason.reason}</td>
              </c:if>
              <c:if test="${donorDeferralFields.deferralReasonText.hidden != true}">
                <td>${donorDeferral.deferralReasonText}</td>
              </c:if>
            </tr>
          </c:forEach>
        </tbody>
      </table>

    </c:otherwise>
  </c:choose>

</div>
