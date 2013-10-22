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
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<c:set var="addDonorFormId">addDonorForm-${unique_page_id}</c:set>

<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>
<c:set var="addDonorFormDonorPanelsId">addDonorFormDonorPanels-${uniquePageId}</c:set>
<c:set var="addDonorFormContactMethodTypesId">addDonorFormContactMethodTypes-${uniquePageId}</c:set>
<c:set var="titleSelectorId">titleSelector-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
           // let the parent know we are done
           $("#${tabContentId}").parent().trigger("editDonorSuccess");
        }

        function notifyParentCancel() {
           // let the parent know we are done
           $("#${tabContentId}").parent().trigger("editDonorCancel");
         }

        $("#${mainContentId}").find(".addDonorButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
                addNewDonor($("#${addDonorFormId}")[0], "${tabContentId}", notifyParentSuccess);
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

        $("#${addDonorFormId}").find(".birthDate").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "dd/mm/yy",
          yearRange : "c-100:c0",
        });

        $("#${titleSelectorId}").multiselect({
            multiple : false,
            selectedList : 1,
            header : false
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

<div id="${tabContentId}">

  <div id="${mainContentId}">

    <c:if test="${!empty success && !success}">
        <jsp:include page="../common/errorBox.jsp">
          <jsp:param name="errorMessage" value="${errorMessage}" />
        </jsp:include>
    </c:if>

    <form:form id="${addDonorFormId}" method="POST" class="formFormatClass"
      commandName="addDonorForm">
      <c:if test="${!donorFields.donorNumber.autoGenerate}">
        <c:if test="${donorFields.donorNumber.hidden != true }">
          <div class="barcodeContainer"></div>
          <div>
            <form:label path="donorNumber">${donorFields.donorNumber.displayName}</form:label>
            <form:input path="donorNumber" value="${firstTimeRender ? donorFields.donorNumber.defaultValue : ''}" />
            <form:errors class="formError" path="donor.donorNumber" delimiter=", "></form:errors>
          </div>
        </c:if>
      </c:if>
      <c:if test="${donorFields.title.hidden != true }">
        <div>
          <form:label path="title">${donorFields.title.displayName}</form:label>
          <form:select path="title" id="${titleSelectorId}">
            <form:option value="Blank" label="" />
            <form:option value="Mr" label="Mr" />
            <form:option value="Ms" label="Ms" />
            <form:option value="Mrs" label="Mrs" />
            <form:option value="Dr" label="Dr" />
          </form:select>
        </div>
      </c:if>
      <c:if test="${donorFields.firstName.hidden != true }">
        <div>
          <form:label path="firstName">${donorFields.firstName.displayName}</form:label>
          <form:input path="firstName" value="${firstTimeRender ? donorFields.firstName.defaultValue : ''}" />
          <form:errors class="formError" path="donor.firstName" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.middleName.hidden != true }">
        <div>
          <form:label path="middleName">${donorFields.middleName.displayName}</form:label>
          <form:input path="middleName" value="${firstTimeRender ? donorFields.middleName.defaultValue : ''}" />
          <form:errors class="formError" path="donor.middleName"
            delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.lastName.hidden != true }">
        <div>
          <form:label path="lastName">${donorFields.lastName.displayName}</form:label>
          <form:input path="lastName" value="${firstTimeRender ? donorFields.lastName.defaultValue : ''}" />
          <form:errors class="formError" path="donor.lastName" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.callingName.hidden != true }">
        <div>
          <form:label path="callingName">${donorFields.callingName.displayName}</form:label>
          <form:input path="callingName" value="${firstTimeRender ? donorFields.callingName.defaultValue : ''}" />
          <form:errors class="formError" path="donor.callingName" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.gender.hidden != true }">
        <div>
          <form:label path="gender">${donorFields.gender.displayName}</form:label>
          <form:select path="gender" id="${genderSelectorId}">
            <form:option value="not_known" label="Not Known" />
            <form:option value="male" label="Male" />
            <form:option value="female" label="Female" />
            <form:option value="not_applicable" label="Not Applicable" />
          </form:select>
          <form:errors class="formError" path="donor.gender" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.nationalID.hidden != true }">
        <div>
          <form:label path="nationalID">${donorFields.nationalID.displayName}</form:label>
          <form:input path="nationalID" value="${firstTimeRender ? donorFields.nationalID.defaultValue : ''}" />
          <form:errors class="formError" path="donor.nationalID" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.birthDate.hidden != true }">
        <div>
          <form:label path="birthDate">${donorFields.birthDate.displayName}</form:label>
          <form:input path="birthDate" class="birthDate"
                      value="${firstTimeRender ? donorFields.birthDate.defaultValue : ''}" />
          <form:errors class="formError" path="donor.birthDate" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.age.hidden != true }">
        <div>
          <form:label path="age">${donorFields.age.displayName}</form:label>
          <form:input path="age"
                      value="${firstTimeRender ? donorFields.age.defaultValue : ''}" /> years
          <form:errors class="formError" path="age" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.address.hidden != true }">
        <div>
          <form:label path="address" class="labelForTextArea">${donorFields.address.displayName}</form:label>
          <form:textarea path="address" />
          <form:errors class="formError" path="donor.address" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.city.hidden != true }">
        <div>
          <form:label path="city">${donorFields.city.displayName}</form:label>
          <form:input path="city" value="${firstTimeRender ? donorFields.city.defaultValue : ''}" />
          <form:errors class="formError" path="donor.city" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.province.hidden != true }">
        <div>
          <form:label path="province">${donorFields.province.displayName}</form:label>
          <form:input path="province" value="${firstTimeRender ? donorFields.province.defaultValue : ''}" />
          <form:errors class="formError" path="donor.province" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.district.hidden != true }">
        <div>
          <form:label path="district">${donorFields.district.displayName}</form:label>
          <form:input path="district" value="${firstTimeRender ? donorFields.district.defaultValue : ''}" />
          <form:errors class="formError" path="donor.district" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.state.hidden != true }">
        <div>
          <form:label path="state">${donorFields.state.displayName}</form:label>
          <form:input path="state" value="${firstTimeRender ? donorFields.state.defaultValue : ''}" />
          <form:errors class="formError" path="donor.state" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.country.hidden != true }">
        <div>
          <form:label path="country">${donorFields.country.displayName}</form:label>
          <form:input path="country" value="${firstTimeRender ? donorFields.country.defaultValue : ''}" />
          <form:errors class="formError" path="donor.country" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.zipcode.hidden != true }">
        <div>
          <form:label path="zipcode">${donorFields.zipcode.displayName}</form:label>
          <form:input path="zipcode" value="${firstTimeRender ? donorFields.zipcode.defaultValue : ''}" />
          <ul>
            <form:errors class="formError" path="donor.zipcode" delimiter=", "></form:errors>
          </ul>
        </div>
      </c:if>
      <c:if test="${donorFields.phoneNumber.hidden != true }">
        <div>
          <form:label path="phoneNumber">${donorFields.phoneNumber.displayName}</form:label>
          <form:input path="phoneNumber" value="${firstTimeRender ? donorFields.phoneNumber.defaultValue : ''}" />
          <ul>
            <form:errors class="formError" path="donor.phoneNumber" delimiter=", "></form:errors>
          </ul>
        </div>
      </c:if>
      <c:if test="${donorFields.otherPhoneNumber.hidden != true }">
        <div>
          <form:label path="otherPhoneNumber">${donorFields.otherPhoneNumber.displayName}</form:label>
          <form:input path="otherPhoneNumber" value="${firstTimeRender ? donorFields.otherPhoneNumber.defaultValue : ''}" />
          <ul>
            <form:errors class="formError" path="donor.otherPhoneNumber" delimiter=", "></form:errors>
          </ul>
        </div>
      </c:if>

      <c:if test="${donorFields.preferredContactMethod.hidden != true }">
        <div>
          <form:label path="preferredContactMethod">${donorFields.preferredContactMethod.displayName}</form:label>
          <form:select path="preferredContactMethod" id="${addDonorFormContactMethodTypesId}"
                       class="addDonorFormPreferredContactMethods">
            <form:option value="" selected="selected">&nbsp;</form:option>
            <c:forEach var="preferredContactMethod" items="${preferredContactMethods}">
              <form:option value="${preferredContactMethod.id}">${preferredContactMethod.contactMethodType}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="donor.preferredContactMethod" delimiter=", "></form:errors>
        </div>
      </c:if>

      <c:if test="${donorFields.donorPanel.hidden != true }">
        <div>
          <form:label path="donorPanel">${donorFields.donorPanel.displayName}</form:label>
          <form:select path="donorPanel" id="${addDonorFormDonorPanelsId}" class="addDonorFormDonorPanels">
            <form:option value="" selected="selected">&nbsp;</form:option>
            <c:forEach var="donorPanel" items="${donorPanels}">
              <form:option value="${donorPanel.id}">${donorPanel.name}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="donor.donorPanel" delimiter=", "></form:errors>
        </div>
      </c:if>

      <c:if test="${donorFields.notes.hidden != true }">
        <div>
          <form:label path="notes" class="labelForTextArea">${donorFields.notes.displayName}</form:label>
          <form:textarea path="notes" />
          <form:errors class="formError" path="donor.notes"></form:errors>
        </div>
      </c:if>
    </form:form>
    
  
    <div style="margin-left: 200px;">
      <label></label>
      <button type="button" class="addDonorButton autoWidthButton">
        Add Donor
      </button>
      <button type="button" class="clearFormButton autoWidthButton">
        Clear form
      </button>        
    </div>
  </div>

  <div id="${childContentId}">
  </div>

</div>
