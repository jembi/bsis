
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

<c:set var="configureDonorDeferralReasonId">configureDonorDeferralReasonForm-${unique_page_id}</c:set>
<c:set var="reasonId">reason-${unique_page_id}</c:set>
<c:set var="isDeletedId">isDeleted-${unique_page_id}</c:set>

<script>
  $(document).ready(function() {
    $("#${tabContentId}").find(".addDonorDeferralReasonButton").button({
        icons : {
          primary : 'ui-icon-plusthick'
        }
      }).click(function() {
          var div = $("#${configureDonorDeferralReasonId}").find(".statusDonorDeferralReasonDiv")[0];
          var newDiv = $($(div).clone());
          var statusDonorDeferralReasonDivs = $("#${configureDonorDeferralReasonId}").find(".statusDonorDeferralReasonDiv");
          console.log(newDiv);
          newDiv.find('input[name="id"]').val(statusDonorDeferralReasonDivs.size()+1);
          newDiv.find('input[name="reason"]').val("");
          newDiv.find('input[name="isDeleted"]').attr('checked',false);
          $("#${configureDonorDeferralReasonId}").append(newDiv);
      });
    
      $("#${tabContentId}").find(".saveDonorDeferralReasonButton").button({
          icons : {
            primary : 'ui-icon-disk'
          }
        }).click(function() {
          var dataId = {};
         
          var statusDonorDeferralReasonDivs = $("#${configureDonorDeferralReasonId}").find(".statusDonorDeferralReasonDiv");
          for (var index=0; index < statusDonorDeferralReasonDivs.length; index++) {
            var div = $(statusDonorDeferralReasonDivs[index]);
            var id = div.find('input[name="id"]').val();
            var reason = div.find('input[name="reason"]').val();
            var isDeleted = div.find('input[name="isDeleted"]').is(':checked');
               
            dataId[id] = id+"-"+reason+"-"+isDeleted;
          }
          $.ajax({
            url: "configureDonorDeferralReasons.html",
            data: {dataId: JSON.stringify(dataId)},
            type: "POST",
            success: function(response) {
                       $("#${tabContentId}").replaceWith(response);
                       showMessage("Donor Deferral Reason Updated Successfully!");
                     },
            error:    function(response) {
                       showErrorMessage("Something went wrong. Please try again later");
                       console.log(response);
                     },
          });
          return false;
      
        });

        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            
          }
        }).click(refetchForm);
        
        function refetchForm() {
          refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
          $("#${childContentId}").html("");
        }
  });
</script>

<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Donor Deferrals </b> <br /> <br />
    <div class="tipsBox ui-state-highlight">
      <p>Modify/Add Donor Deferral Reason</p>
    </div>
    <div>
      <label style="padding-left: 10px;">Reason</label>
      <label style="padding-left: 165px;">Enabled</label>
    </div>
    <form id="${configureDonorDeferralReasonId}">
        <c:forEach var="statusDonorDeferralReason" items="${allDonorDeferralReason}">
        <div id="statusDonorDeferralReasonDiv" class="statusDonorDeferralReasonDiv">
        <div>
          <input type="hidden" name="id" value="${statusDonorDeferralReason.id}" /> 
          <input type="text" name="reason" id="${statusDonorDeferralReason.id}" value="${statusDonorDeferralReason.reason}" size="25"/>
          
            <c:choose>
                <c:when test="${statusDonorDeferralReason.isDeleted eq true}">
                  <input type="checkbox" name="isDeleted" id="${isDeletedId}" value="${statusDonorDeferralReason.isDeleted}"  />
                </c:when>
                <c:otherwise>
                  <input type="checkbox" name="isDeleted" id="${isDeletedId}" value="${statusDonorDeferralReason.isDeleted}"  checked="checked"/>
                </c:otherwise>
            </c:choose>
            
        </div>
        </div>
        </c:forEach>
    </form>
    <br />
    <div>
      <label>&nbsp;</label>
      <button class="addDonorDeferralReasonButton">Add new Donor Deferral Reason</button>
      <button class="saveDonorDeferralReasonButton">Save</button>
      <button class="cancelButton">Cancel</button>
    </div>

  </div>

  <div id="${childContentId}"></div>

</div>