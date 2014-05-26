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

<c:set var="editDonorFormId">editDonorForm-${unique_page_id}</c:set>

<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>
<c:set var="editDonorFormDonorPanelsId">editDonorFormDonorPanels-${uniquePageId}</c:set>
<c:set var="editDonorFormContactMethodTypesId">editDonorFormContactMethodTypes-${uniquePageId}</c:set>

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

        $("#${mainContentId}").find(".saveDonorButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
                updateExistingDonor($("#${editDonorFormId}")[0], "${tabContentId}", notifyParentSuccess);
            });

        $("#${mainContentId}").find(".clearFormButton")
                              .button()
                              .click(refetchForm);

        $("#${mainContentId}").find(".cancelButton")
                              .button({icons : {primary : 'ui-icon-closethick'}})
                              .click(function() { $("#${tabContentId}").trigger("editDonorCancel"); });

        $("#${editDonorFormDonorPanelsId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editDonorFormContactMethodTypesId}").multiselect({
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

        $("#${editDonorFormId}").find(".birthDate").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "dd/mm/yy",
          yearRange : "c-100:c0",
        });
	
        $("#${editDonorFormId}").find(".dateOfFirstDonation").datepicker({
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

        function updateBarcode(val) {
            if (val === null || val === undefined || val === "")
              val = "-";
            $("#${editDonorFormId}").find(".barcodeContainer").barcode(
                val,
                "code128",
                {barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
          }
          updateBarcode("${editDonorForm.donor.donorNumber}");

          $("#${editDonorFormId}").find('input[name="donorNumber"]').keyup(function() {
            updateBarcode($(this).val());
          });

      });
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).EDIT_DONOR)">
<div id="${tabContentId}">

  <div id="${mainContentId}">

    <c:if test="${!empty success && !success}">
        <jsp:include page="../common/errorBox.jsp">
          <jsp:param name="errorMessage" value="${errorMessage}" />
        </jsp:include>
    </c:if>

    <form:form id="${editDonorFormId}" method="POST" class="formFormatClass"
      commandName="editDonorForm">
      <form:hidden path="id" />
      <div>
        <label><b>Edit donor</b></label>
      </div>
      <c:if test="${donorFields.donorNumber.hidden != true }">
        <div class="barcodeContainer"></div>
        <div>
          <form:label path="donorNumber">${donorFields.donorNumber.displayName}</form:label>
          <form:label path="donorNumber" >${editDonorForm.donor.donorNumber}</form:label>
          <form:input path="donorNumber" style="display:none; disabled:true;"/>
          <form:errors class="formError" path="donor.donorNumber" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.firstName.hidden != true }">
        <div>
          <form:label path="firstName">${donorFields.firstName.displayName}</form:label>
          <form:input path="firstName" />
          <form:errors class="formError" path="donor.firstName" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.middleName.hidden != true }">
        <div>
          <form:label path="middleName">${donorFields.middleName.displayName}</form:label>
          <form:input path="middleName" />
          <form:errors class="formError" path="donor.middleName"
            delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.lastName.hidden != true }">
        <div>
          <form:label path="lastName">${donorFields.lastName.displayName}</form:label>
          <form:input path="lastName" />
          <form:errors class="formError" path="donor.lastName" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.callingName.hidden != true }">
        <div>
          <form:label path="callingName">${donorFields.callingName.displayName}</form:label>
          <form:input path="callingName" />
          <form:errors class="formError" path="donor.callingName" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.gender.hidden != true }">
        <div>
          <form:label path="gender">${donorFields.gender.displayName}</form:label>
          <form:select path="gender" id="${genderSelectorId}">
            <form:option value="male" label="Male" />
            <form:option value="female" label="Female" />
          </form:select>
          <form:errors class="formError" path="donor.gender" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.bloodAbo.hidden != true }">
	    <div>
	      <form:label path="bloodAbo">${donorFields.bloodGroup.displayName}</form:label>
          <form:select path="bloodAbo" name="BloodABO" title="ABO">
			<form:option value="" label=""/>
			<form:option value="A" label="A"/>
			<form:option value="B" label="B"/>
			<form:option value="O" label="O"/>
			<form:option value="AB" label="AB"/>
		  </form:select>
		  <form:select path="bloodRh" name="BloodRh" title="Rh">
			<form:option value="" label=""/>
			<form:option value="+" label="+"/>
			<form:option value="-" label="-"/>
		  </form:select>
          <form:errors class="formError" path="donor.bloodAbo" delimiter=", "></form:errors>
          <form:errors class="formError" path="donor.bloodRh" delimiter=", "></form:errors>
	    </div>
	  </c:if>
      <c:if test="${donorFields.nationalID.hidden != true }">
        <div>
          <form:label path="nationalID">${donorFields.nationalID.displayName}</form:label>
          <form:input path="nationalID" />
          <form:errors class="formError" path="donor.nationalID" delimiter=", "></form:errors>
        </div>
      </c:if>
      
      <div>
          <c:if test="${donorFields.birthDate.hidden != true }">
               <form:label path="birthDate">${donorFields.birthDate.displayName}</form:label>
				<form:input style="width:34px" placeholder="Day" path="dayOfMonth"
					alt="dayOfMonth" title="dayOfMonth" maxlength="2" />


				<form:select path="month" name="Month">
					<form:option value="" label="Month"/>
					<form:option value="01" label="January"/>
					<form:option value="02" label="February"/>
					<form:option value="03" label="March"/>
					<form:option value="04" label="April"/>
					<form:option value="05" label="May"/>
					<form:option value="06" label="June"/>
					<form:option value="07" label="July"/>
					<form:option value="08" label="August"/>
					<form:option value="09" label="September"/>
					<form:option value="10" label="Octobor"/>
					<form:option value="11" label="November"/>
					<form:option value="12" label="December"/>
				</form:select>
            
            <form:input style="width:46px" path="Year" maxlength="4" alt="year"
					 placeholder="Year" />
					 
		      </c:if>   

		   <c:if test="${donorFields.birthDateEstimated.hidden != true }">
          	${donorFields.birthDateEstimated.displayName}
			<form:checkbox path="birthDateEstimated" class="birthDateEstimated"
						style="width: auto; position: relative;" />
					<form:errors class="formError" path="donor.birthDateEstimated"
						delimiter=", "></form:errors>
				</c:if>
				
				<form:errors class="formError" path="donor.birthDate">
				</form:errors>
		</div>
				
      <c:if test="${donorFields.age.hidden != true }">
        <div>
          <form:label path="age">${donorFields.age.displayName}</form:label>
          <form:input path="age" /> years
          <form:errors class="formError" path="age" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.dateOfFirstDonation.hidden != true }">
        <div>
          <form:label path="dateOfFirstDonation">${donorFields.dateOfFirstDonation.displayName}</form:label>
          <form:input path="dateOfFirstDonation" class="dateOfFirstDonation"/>
          <form:errors class="formError" path="donor.dateOfFirstDonation" delimiter=", "></form:errors>
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
          <form:input path="city" />
          <form:errors class="formError" path="donor.city" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.province.hidden != true }">
        <div>
          <form:label path="province">${donorFields.province.displayName}</form:label>
          <form:input path="province" />
          <form:errors class="formError" path="donor.province" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.district.hidden != true }">
        <div>
          <form:label path="district">${donorFields.district.displayName}</form:label>
          <form:input path="district" />
          <form:errors class="formError" path="donor.district" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.state.hidden != true }">
        <div>
          <form:label path="state">${donorFields.state.displayName}</form:label>
          <form:input path="state" />
          <form:errors class="formError" path="donor.state" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.country.hidden != true }">
        <div>
          <form:label path="country">${donorFields.country.displayName}</form:label>
          <form:input path="country" />
          <form:errors class="formError" path="donor.country" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${donorFields.zipcode.hidden != true }">
        <div>
          <form:label path="zipcode">${donorFields.zipcode.displayName}</form:label>
          <form:input path="zipcode" />
          <ul>
            <form:errors class="formError" path="donor.zipcode" delimiter=", "></form:errors>
          </ul>
        </div>
      </c:if>
      <c:if test="${donorFields.phoneNumber.hidden != true }">
        <div>
          <form:label path="phoneNumber">${donorFields.phoneNumber.displayName}</form:label>
          <form:input path="phoneNumber" />
          <ul>
            <form:errors class="formError" path="donor.phoneNumber" delimiter=", "></form:errors>
          </ul>
        </div>
      </c:if>
      <c:if test="${donorFields.otherPhoneNumber.hidden != true }">
        <div>
          <form:label path="otherPhoneNumber">${donorFields.otherPhoneNumber.displayName}</form:label>
          <form:input path="otherPhoneNumber" />
          <ul>
            <form:errors class="formError" path="donor.otherPhoneNumber" delimiter=", "></form:errors>
          </ul>
        </div>
      </c:if>

      <c:if test="${donorFields.preferredContactMethod.hidden != true }">
        <div>
          <form:label path="preferredContactMethod">${donorFields.preferredContactMethod.displayName}</form:label>
          <form:select path="preferredContactMethod" id="${editDonorFormContactMethodTypesId}"
                       class="editDonorFormPreferredContactMethods">
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
          <form:select path="donorPanel" id="${editDonorFormDonorPanelsId}" class="editDonorFormDonorPanels">
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
      <button type="button" class="saveDonorButton autoWidthButton">
        Save Donor
      </button>
      <!-- button type="button" class="clearFormButton autoWidthButton">
        Clear form
      </button-->        
      <button type="button" class="cancelButton autoWidthButton">
        Cancel
      </button>        
    </div>
  </div>

  <div id="${childContentId}">
  </div>

</div>
</sec:authorize>
