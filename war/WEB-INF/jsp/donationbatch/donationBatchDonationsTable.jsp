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
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="table_id">collectionsTable-${unique_page_id}</c:set>
<c:set var="collectionsTableEditRowDivId">collectionsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var donationsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>',
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]}
                         ],
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
                              var elements = $(node).children();
                              if (elements[0].getAttribute("class") === "dataTables_empty") {
                                return;
                              }
                              var selectedRowId = elements[0].innerHTML;
                              createCollectionSummary("collectionSummary.html",
                                  {collectionId: selectedRowId});
                             },
        "fnRowDeselected" : function(node) {
                            },
        },
        "oColVis" : {
           "aiExclude": [0,1],
        }
      });
      
      function createCollectionSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
                     $("#${mainContentId}").hide();
                     $("#${childContentId}").html(response);
                   }
        });
      }

      function refreshResults() {
        showLoadingImage($("#${mainContentId}"));
        $.ajax({url: "${model.refreshUrl}",
                type: "GET",
                success: function(response) {
                           $("#${mainContentId}").html(response);
                         }
        });
      }

      $("#${mainContentId}").find(".donationsTable").bind("refreshResults", refreshResults);

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });
    
   $("#${tabContentId}").bind("collectionSummarySuccess",
      function(event, content) {
        $("#${mainContentId}").show();
        $("#${childContentId}").html("");
      });
    
    });
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONATION)">
<div id="${tabContentId}">

  <div id="${mainContentId}">

    <c:choose>

      <c:when test="${fn:length(model.allCollections) eq -1}">
        <span style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
          Sorry no results found matching your search request
        </span>
      </c:when>
  
      <c:otherwise>
  
        <table id="${table_id}" class="dataTable collectionsTable">
          <thead>
            <tr>
              <th style="display: none"></th>
              <c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
                <th>${model.collectedSampleFields.collectionNumber.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.collectedOn.hidden != true}">
                <th>${model.collectedSampleFields.collectedOn.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.bloodBagType.hidden != true}">
                <th>${model.collectedSampleFields.bloodBagType.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.collectionCenter.hidden != true}">
                <th>${model.collectedSampleFields.collectionCenter.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.collectionSite.hidden != true}">
                <th>${model.collectedSampleFields.collectionSite.displayName}</th>
              </c:if>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="collection" items="${model.allCollections}">
              <tr>
                <td style="display: none">${collection.id}</td>
                <c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
                  <td>${collection.collectionNumber}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.collectedOn.hidden != true}">
                  <td>${collection.collectedOn}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.bloodBagType.hidden != true}">
                  <td>${collection.bloodBagType.bloodBagType}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.collectionCenter.hidden != true}">
                  <td>${collection.collectionCenter}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.collectionSite.hidden != true}">
                  <td>${collection.collectionSite}</td>
                </c:if>
              </tr>
            </c:forEach>
          </tbody>
        </table>
  
      </c:otherwise>
    </c:choose>
  </div>

  <div id="${childContentId}">
  </div>

</div>

<div id="${noResultsFoundDivId}" style="display: none;">
  <span
    style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
    Sorry no results found matching your search request </span>
</div>
</sec:authorize>
