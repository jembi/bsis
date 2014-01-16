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
<c:set var="addDonorContentId">addDonorContent-${unique_page_id}</c:set>

<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>

<c:set var="findDonorFormId">findDonorForm-${unique_page_id}</c:set>
<c:set var="addDonorFormId">addDonorForm-${unique_page_id}</c:set>

<c:set var="findDonorFormBloodGroupSelectorId">findDonorFormBloodGroupSelector-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".findDonorButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findDonorFormData = $("#${findDonorFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findDonorResults");
    $.ajax({
      type : "GET",
      url : "findDonor.html",
      data : findDonorFormData,
      success : function(data) {
        animatedScrollTo(resultsDiv);
        resultsDiv.html(data);
      }
    });
  });

  $("#${tabContentId}").find(".clearFindFormButton").button({
    icons : {
      
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

  $("#${findDonorFormBloodGroupSelectorId}").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    },
    noneSelectedText: 'None Selected',
    selectedText: function(numSelected, numTotal, selectedValues) {
                    if (numSelected == numTotal) {
                      $("#${findDonorFormId}").find(".anyBloodGroupInput")
                                              .val("true");
                      return "Any Blood Group";
                    }
                    else {
                      $("#${findDonorFormId}").find(".anyBloodGroupInput")
                                              .val("false");
                      var checkedValues = $.map(selectedValues, function(input) { return input.title; });
                      return checkedValues.length ? checkedValues.join(', ') : 'Any Blood Group';
                    }
                      
                  }
  });

  $("#${findDonorFormBloodGroupSelectorId}").multiselect("checkAll");

  // child div shows donor information. bind this div to donorView event
  $("#${tabContentId}").bind("donorSummaryView",
      function(event, content) {
        $("#${mainContentId}").hide();
        $("#${addDonorContentId}").hide();
        $("#${childContentId}").html(content);
      });

  $("#${tabContentId}").bind("donorSummarySuccess",
      function(event, content) {
        $("#${mainContentId}").show();
        $("#${addDonorContentId}").show();
        $("#${childContentId}").html("");
        $("#${tabContentId}").find(".donorsTable").trigger("refreshResults");
      });
  
  
  
  
  function notifyParentSuccess() {
      // let the parent know we are done
      $("#${tabContentId}").parent().trigger("editDonorSuccess");
   }

   function notifyParentCancel() {
      // let the parent know we are done
      $("#${tabContentId}").parent().trigger("editDonorCancel");
    }

   $("#${addDonorContentId}").find(".addDonorButton").button({
     icons : {
       primary : 'ui-icon-plusthick'
     }
   }).click(
       function() {
    	   findAndAddNewDonor($("#${addDonorFormId}")[0], "${tabContentId}", notifyParentSuccess);
       });

   $("#${mainContentId}").find(".clearFormButton")
                         .button()
                         .click(refetchForm);

   $("#${addDonorFormDonorPanelsId}").multiselect({
     multiple : false,
     selectedList : 1,
     header : false
   });

   $("#${addDonorFormContactMethodTypesId}").multiselect({
     multiple : false,
     selectedList : 1,
     header : false
   });

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

   $("#${genderSelectorId}").multiselect({
     multiple : false,
     selectedList : 1,
     header : false
   });

   $("#${addDonorContentId}").find(".birthDate").datepicker({
     changeMonth : true,
     changeYear : true,
     minDate : -36500,
     maxDate : 0,
     dateFormat : "dd/mm/yy",
     yearRange : "c-100:c0",
   });

   function getDonorPanelSelector() {
     return $("#${tabContentId}").find('select[name="donorPanel"]').multiselect();
   }

   function getGenderSelector() {
     return $("#${mainContentId}").find('select[name="gender"]').multiselect();
   }

   function getPreferredContactMethodSelector() {
     return $("#${tabContentId}").find('select[name="preferredContactMethod"]').multiselect();
   }

   if ("${firstTimeRender}" == "true") {
     $("#${mainContentId}").find('textarea[name="address"]').html("${donorFields.address.defaultValue}");
     $("#${mainContentId}").find('textarea[name="notes"]').html("${donorFields.notes.defaultValue}");
     setDefaultValueForSelector(getDonorPanelSelector(), "${donorFields.donorPanel.defaultValue}");
     setDefaultValueForSelector(getGenderSelector(), "${donorFields.gender.defaultValue}");
     setDefaultValueForSelector(getPreferredContactMethodSelector(), "${donorFields.preferredContactMethod.defaultValue}");
   }
  
  
  

});
</script>

<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
  
  	<c:if test="${!empty success && !success}">
        <jsp:include page="../common/errorBox.jsp">
          <jsp:param name="errorMessage" value="${errorMessage}" />
        </jsp:include>
    </c:if>
  
  
    <b>Find Donors</b>
    <form:form method="GET" commandName="findDonorForm" id="${findDonorFormId}"
      class="formFormatClass">
      <form:hidden path="createDonorSummaryView" />
      <div>
        <form:label path="donorNumber">${model.donorFields.donorNumber.displayName}</form:label>
        <form:input path="donorNumber" />
      </div>
      <div>
        <form:label path="firstName">${model.donorFields.firstName.displayName}</form:label>
        <form:input path="firstName" />
      </div>
      <div>
        <form:label path="lastName">${model.donorFields.lastName.displayName}</form:label>
        <form:input path="lastName" />
      </div>
      <div>
        <form:label path="bloodGroups">${model.donorFields.bloodGroup.displayName}</form:label>
        <form:hidden path="anyBloodGroup" class="anyBloodGroupInput" value="true" />
        <form:select path="bloodGroups" id="${findDonorFormBloodGroupSelectorId}">
          <form:option value="Unknown" label="Unknown" />
          <form:option value="A+" label="A+" />
          <form:option value="A-" label="A-" />
          <form:option value="B+" label="B+" />
          <form:option value="B-" label="B-" />
          <form:option value="AB+" label="AB+" />
          <form:option value="AB-" label="AB-" />
          <form:option value="O+" label="O+" />
          <form:option value="O-" label="O-" />
        </form:select>
      </div>
    </form:form>

    <div class="formFormatClass">
      <div>
        <label></label>
        <button type="button" class="findDonorButton">
          Find donor
        </button>
        <button type="button" class="clearFindFormButton">
          Clear form
        </button>
      </div>
    </div>
    <div class="findDonorResults"></div>
  </div>

  <div id="${childContentId}"></div>
  
  <br/>
  <div id="${addDonorContentId}">
  	<b>Add Donor</b>
  	

  	   
  	<form:form id="${addDonorFormId}" method="POST" class="formFormatClass"
      commandName="addDonorForm">


      <c:if test="${model.donorFields.firstName.hidden != true }">
        <div>
          <form:label path="firstName">${model.donorFields.firstName.displayName}</form:label>
          <form:input path="firstName" value="${firstTimeRender ? model.donorFields.firstName.defaultValue : ''}" />
        </div>
      </c:if>
      <c:if test="${model.donorFields.lastName.hidden != true }">
        <div>
          <form:label path="lastName">${model.donorFields.lastName.displayName}</form:label>
          <form:input path="lastName" value="${firstTimeRender ? model.donorFields.lastName.defaultValue : ''}" />
        </div>
      </c:if>
      <c:if test="${model.donorFields.callingName.hidden != true }">
        <div>
          <form:label path="callingName">${model.donorFields.callingName.displayName}</form:label>
          <form:input path="callingName" value="${firstTimeRender ? model.donorFields.callingName.defaultValue : ''}" />
        </div>
      </c:if>
      <c:if test="${model.donorFields.gender.hidden != true }">
        <div>
          <form:label path="gender">${model.donorFields.gender.displayName}</form:label>
          <form:select path="gender" id="${genderSelectorId}">
          	<form:option value="" >Gender</form:option>
            <form:option value="male" label="Male" />
            <form:option value="female" label="Female" />
          </form:select>
        </div>
        </c:if>
      <c:if test="${model.donorFields.nationalID.hidden != true }">
        <div>
          <form:label path="nationalID">${model.donorFields.nationalID.displayName}</form:label>
          <form:input path="nationalID" value="${firstTimeRender ? model.donorFields.nationalID.defaultValue : ''}" />
        </div>
      </c:if>
     
        <div> 
       
       <form:label path="birthDate">${model.donorFields.birthDate.displayName}</form:label>
        <form:input  style="width:34px" placeholder="Day" path="dayOfMonth" alt="dayOfMonth"    maxlength="2"/>
       
        <form:select path="month"  name="Month">
        <form:option value="">Month</form:option> 
       <form:option value="01">January</form:option>
       <form:option value="02">February</form:option>
       <form:option value="03">March</form:option>
       <form:option value="04">April</form:option>
       <form:option value="05">May</form:option>
       <form:option value="06">June</form:option>
       <form:option value="07">July</form:option>
       <form:option value="08">August</form:option>
       <form:option value="09">September</form:option>
       <form:option value="10">Octobor</form:option>
       <form:option value="11">November</form:option>
       <form:option value="12">December</form:option>
       </form:select>
       
        <form:input style="width:46px" path="year" maxlength="4" alt="year" id="year" placeholder="Year" />
          <c:if test="${donorFields.birthDateEstimated.hidden != true }">
          	${donorFields.birthDateEstimated.displayName}
			<form:checkbox path="birthDateEstimated" class="birthDateEstimated" style="width: auto; position: relative;"/>
          </c:if>
 
        </div>
        
      <c:if test="${model.donorFields.notes.hidden != true }">
        <div>
          <form:label path="notes" class="labelForTextArea">${model.donorFields.notes.displayName}</form:label>
          <form:textarea path="notes" />
        </div>
      </c:if>
      
      <!-- include hidden additional donorFields to ensure values are empty not null -->
      <div style="display:none">
      	<form:input path="middleName" value="${firstTimeRender ? donorFields.middleName.defaultValue : ''}" />
        <form:textarea path="address" />
        <form:input path="city" value="${firstTimeRender ? donorFields.city.defaultValue : ''}" />
        <form:input path="province" value="${firstTimeRender ? donorFields.province.defaultValue : ''}" />
        <form:input path="district" value="${firstTimeRender ? donorFields.district.defaultValue : ''}" />
        <form:input path="state" value="${firstTimeRender ? donorFields.state.defaultValue : ''}" />
        <form:input path="country" value="${firstTimeRender ? donorFields.country.defaultValue : ''}" />
        <form:input path="zipcode" value="${firstTimeRender ? donorFields.zipcode.defaultValue : ''}" />
        <form:input path="phoneNumber" value="${firstTimeRender ? donorFields.phoneNumber.defaultValue : ''}" />
        <form:input path="otherPhoneNumber" value="${firstTimeRender ? donorFields.otherPhoneNumber.defaultValue : ''}" />
        <form:select path="preferredContactMethod" id="${addDonorFormContactMethodTypesId}"
                     class="addDonorFormPreferredContactMethods">
          <form:option value="" selected="selected">&nbsp;</form:option>
          <c:forEach var="preferredContactMethod" items="${preferredContactMethods}">
            <form:option value="${preferredContactMethod.id}">${preferredContactMethod.contactMethodType}</form:option>
          </c:forEach>
        </form:select>
        <form:select path="donorPanel" id="${addDonorFormDonorPanelsId}" class="addDonorFormDonorPanels">
          <form:option value="" selected="selected">&nbsp;</form:option>
          <c:forEach var="donorPanel" items="${donorPanels}">
            <form:option value="${donorPanel.id}">${donorPanel.name}</form:option>
          </c:forEach>
        </form:select>
      </div>
    </form:form>
    
    <div style="margin-left: 200px;">
      <label></label>
      <button type="button" class="addDonorButton autoWidthButton">
        Add Donor
      </button> 
    </div>

  </div>

</div>
