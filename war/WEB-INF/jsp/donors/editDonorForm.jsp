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
                                  //Hiding IDNumber button if ID type is not selected
                                                if ($('#idType').val().trim().length === 0)
                                                $('#idNumber').hide();   
                                                $('#idType').change(function(){
                                                              if ($('#idType').val().trim().length > 0)
                                                                     $('#idNumber').show();
                                                              else
                                                              {
                                                                     $('#idNumber').hide();         
                                                                     $('#idNumber').val('');           
                                                               }
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
                      <form:hidden path="contactId"/>
                      <form:hidden path="addressId"/>
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
                       <c:if test="${donorFields.title.hidden != true }">
                        <div>
                          <form:label path="title">${donorFields.title.displayName}</form:label>
                          <form:select path="title" id="${titleSelectorId}">
                            <form:option value="" label="" />
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
                                         <c:if test="${donorFields.preferredLanguage.hidden != true }">
                        <div>
                          <form:label path="preferredLanguage">${donorFields.preferredLanguage.displayName}</form:label>
                           <form:select path="preferredLanguage">
			             <form:option value="" selected="selected">Language</form:option>
					<c:forEach var="language" items="${languages}">
						<form:option value="${language.id}">${language.preferredLanguage}</form:option>
					</c:forEach>
			  </form:select>
			 <form:errors class="formError" path="donor.preferredLanguage" delimiter=", "></form:errors>
                         </div>
			</c:if>
                     <c:if test="${donorFields.idType.hidden != true }">
                            <div>
                          <form:label path="idType">${donorFields.idType.displayName}</form:label>
                           <form:select path="idType">
                             <form:option value="" selected="selected">Select ID Type</form:option>
                                 <c:forEach var="idType" items="${idTypes}">
                                    <form:option value="${idType.id}">${idType.idType}</form:option>
                                 </c:forEach>
                            </form:select>
                          <form:input path="idNumber" placeholder="ID Number"/>
                          <form:errors class="formError" path="donor.idNumber" delimiter=", "></form:errors>
                            </div>
                       </c:if>
			<c:if test="${donorFields.mobileNumber.hidden != true }">
				<div>
					<form:label path="mobileNumber">${donorFields.mobileNumber.displayName}</form:label>
					<form:input path="mobileNumber" />
				        <form:errors class="formError" path="mobileNumber" delimiter=", "></form:errors>
					
				</div>
			</c:if>
                        <c:if test="${donorFields.homeNumber.hidden != true }">
				<div>
					<form:label path="homeNumber">${donorFields.homeNumber.displayName}</form:label>
					<form:input path="homeNumber" />
					<form:errors class="formError" path="homeNumber" delimiter=", "></form:errors>
					
				</div>
			</c:if>
                        <c:if test="${donorFields.workNumber.hidden != true }">
				<div>
					<form:label path="workNumber">${donorFields.workNumber.displayName}</form:label>
					<form:input path="workNumber" />
					<form:errors class="formError" path="workNumber" delimiter=", "></form:errors>
					
				</div>
			</c:if>
			<c:if test="${donorFields.email.hidden != true }">
				<div>
					<form:label path="email">${donorFields.email.displayName}</form:label>
					<form:input path="email" />
					<form:errors class="formError" path="email" delimiter=", "></form:errors>
				</div>
			</c:if>
			
			<c:if test="${donorFields.preferredContactMethod.hidden != true }">
				<div>
					<form:label path="preferredContactMethod">${donorFields.preferredContactMethod.displayName}</form:label>
					<form:select path="preferredContactMethod"
						id="${addDonorFormContactMethodTypesId}"
						class="addDonorFormPreferredContactMethods">
						<form:option value="" selected="selected">&nbsp;</form:option>
						<c:forEach var="preferredContactMethod"
							items="${preferredContactMethods}">
							<form:option value="${preferredContactMethod.id}">${preferredContactMethod.contactMethodType}</form:option>
						</c:forEach>
					</form:select>
					<form:errors class="formError" path="preferredContactMethod"
						delimiter=", "></form:errors>
				</div>
			</c:if>

			<c:if test="${donorFields.donorPanel.hidden != true }">
				<div>
					<form:label path="donorPanel">${donorFields.donorPanel.displayName}</form:label>
					<form:select path="donorPanel" id="${addDonorFormDonorPanelsId}"
						class="addDonorFormDonorPanels">
						<form:option value="" selected="selected">&nbsp;</form:option>
						<c:forEach var="donorPanel" items="${donorPanels}">
							<form:option value="${donorPanel.id}">${donorPanel.name}</form:option>
						</c:forEach>
					</form:select>
					<form:errors class="formError" path="donor.donorPanel"
						delimiter=", "></form:errors>
				</div>
			</c:if>
                          <c:if test="${donorFields.homeAddress.hidden != true }">
                              	<div style="margin-left: 5px;">
                                    <b><label path="">${donorFields.homeAddress.displayName}</label></b>
				</div>
                          </c:if>
                        <c:if test="${donorFields.addressLine1.hidden != true }">
				<div>
					<form:label path="homeAddressLine1">${donorFields.addressLine1.displayName}</form:label>
					<form:input path="homeAddressLine1" />
					<form:errors class="formError" path="homeAddressLine1" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.addressLine2.hidden != true }">
				<div>
					<form:label path="homeAddressLine2">${donorFields.addressLine2.displayName}</form:label>
					<form:input path="homeAddressLine2" />
					<form:errors class="formError" path="homeAddressLine2" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.city.hidden != true }">
				<div>
					<form:label path="homeAddressCity">${donorFields.city.displayName}</form:label>
					<form:input path="homeAddressCity"
						value="${firstTimeRender ? donorFields.homeAddressCity.defaultValue : ''}"  delimiter=", "/>
					<form:errors class="formError" path="homeAddressCity" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.province.hidden != true }">
				<div>
					<form:label path="homeAddressProvince">${donorFields.province.displayName}</form:label>
					<form:input path="homeAddressProvince"
						value="${firstTimeRender ? donorFields.homeAddressProvince.defaultValue : ''}" />
					<form:errors class="formError" path="homeAddressProvince" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.district.hidden != true }">
				<div>
					<form:label path="homeAddressDistrict">${donorFields.district.displayName}</form:label>
					<form:input path="homeAddressDistrict"
						value="${firstTimeRender ? donorFields.homeAddressDistrict.defaultValue : ''}" />
					<form:errors class="formError" path="homeAddressDistrict" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.state.hidden != true }">
				<div>
					<form:label path="homeAddressState">${donorFields.state.displayName}</form:label>
					<form:input path="homeAddressState"
						value="${firstTimeRender ? donorFields.homeAddressState.defaultValue : ''}" />
					<form:errors class="formError" path="homeAddressState" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.country.hidden != true }">
				<div>
					<form:label path="homeAddressCountry">${donorFields.country.displayName}</form:label>
					<form:input path="homeAddressCountry"
						value="${firstTimeRender ? donorFields.homeAddressCountry.defaultValue : ''}" />
					<form:errors class="formError" path="homeAddressCountry" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.zipcode.hidden != true }">
				<div>
					<form:label path="homeAddressZipcode">${donorFields.zipcode.displayName}</form:label>
					<form:input path="homeAddressZipcode"
						value="${firstTimeRender ? donorFields.homeAddressZipcode.defaultValue : ''}" />
					<form:errors class="formError" path="homeAddressZipcode" delimiter=", "></form:errors>
				</div>
			</c:if>
                            <c:if test="${donorFields.workAddress.hidden != true }">
                              	<div style="margin-left: 5px;">
                                    <b><label path="">${donorFields.postalAddress.displayName}</label></b>
				</div>
                          </c:if>                                      
                        <c:if test="${donorFields.addressLine1.hidden != true }">
				<div>
					<form:label path="postalAddressLine1">${donorFields.addressLine1.displayName}</form:label>
					<form:input path="postalAddressLine1" />
					<form:errors class="formError" path="postalAddressLine1" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.addressLine2.hidden != true }">
				<div>
					<form:label path="postalAddressLine2">${donorFields.addressLine2.displayName}</form:label>
					<form:input path="postalAddressLine2" />
					<form:errors class="formError" path="postalAddressLine1" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.city.hidden != true }">
				<div>
					<form:label path="postalAddressCity">${donorFields.city.displayName}</form:label>
					<form:input path="postalAddressCity"
						value="${firstTimeRender ? donorFields.postalAddressCity.defaultValue : ''}"  delimiter=", "/>
					<form:errors class="formError" path="postalAddressCity" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.province.hidden != true }">
				<div>
					<form:label path="postalAddressProvince">${donorFields.province.displayName}</form:label>
					<form:input path="postalAddressProvince"
						value="${firstTimeRender ? donorFields.postalAddressProvince.defaultValue : ''}" />
					<form:errors class="formError" path="postalAddressProvince" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.district.hidden != true }">
				<div>
					<form:label path="postalAddressDistrict">${donorFields.district.displayName}</form:label>
					<form:input path="postalAddressDistrict"
						value="${firstTimeRender ? donorFields.postalAddressDistrict.defaultValue : ''}" />
					<form:errors class="formError" path="homeAddressDistrict" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.state.hidden != true }">
				<div>
					<form:label path="state">${donorFields.state.displayName}</form:label>
					<form:input path="postalAddressState"
						value="${firstTimeRender ? donorFields.postalAddressState.defaultValue : ''}" />
					<form:errors class="formError" path="postalAddressState" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.country.hidden != true }">
				<div>
					<form:label path="postalAddressCountry">${donorFields.country.displayName}</form:label>
					<form:input path="postalAddressCountry"
						value="${firstTimeRender ? donorFields.postalAddressCountry.defaultValue : ''}" />
					<form:errors class="formError" path="postalAddressCountry" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.zipcode.hidden != true }">
				<div>
					<form:label path="postalAddressZipcode">${donorFields.zipcode.displayName}</form:label>
					<form:input path="postalAddressZipcode"
						value="${firstTimeRender ? donorFields.postalAddressZipcode.defaultValue : ''}" />
				        <form:errors class="formError" path="postalAddressZipcode" delimiter=", "></form:errors>
				</div>
			</c:if>
                           <c:if test="${donorFields.workAddress.hidden != true }">
                              	<div style="margin-left: 5px;">
                                    <b><label path="">${donorFields.workAddress.displayName}</label></b>
				</div>
                          </c:if>                                       
                      <c:if test="${donorFields.addressLine1.hidden != true }">
				<div>
					<form:label path="workAddressLine1">${donorFields.addressLine1.displayName}</form:label>
					<form:input path="workAddressLine1" />
					<form:errors class="formError" path="address.homeAddressLine1" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.addressLine2.hidden != true }">
				<div>
					<form:label path="workAddressLine2">${donorFields.addressLine2.displayName}</form:label>
					<form:input path="workAddressLine2" />
					<form:errors class="formError" path="workAddressLine1" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.city.hidden != true }">
				<div>
					<form:label path="workAddressCity">${donorFields.city.displayName}</form:label>
					<form:input path="workAddressCity"
						value="${firstTimeRender ? donorFields.workAddressCity.defaultValue : ''}"  delimiter=", "/>
					<form:errors class="formError" path="workAddressCity" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.province.hidden != true }">
				<div>
					<form:label path="workAddressProvince">${donorFields.province.displayName}</form:label>
					<form:input path="workAddressProvince"
						value="${firstTimeRender ? donorFields.workAddressProvince.defaultValue : ''}" />
					<form:errors class="formError" path="workAddressProvince" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.district.hidden != true }">
				<div>
					<form:label path="workAddressDistrict">${donorFields.district.displayName}</form:label>
					<form:input path="workAddressDistrict"
						value="${firstTimeRender ? donorFields.workAddressDistrict.defaultValue : ''}" />
					<form:errors class="formError" path="workAddressDistrict" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.state.hidden != true }">
				<div>
					<form:label path="state">${donorFields.state.displayName}</form:label>
					<form:input path="workAddressState"
						value="${firstTimeRender ? donorFields.workAddressState.defaultValue : ''}" />
					<form:errors class="formError" path="workAddressState" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.country.hidden != true }">
				<div>
					<form:label path="workAddressCountry">${donorFields.country.displayName}</form:label>
					<form:input path="workAddressCountry"
						value="${firstTimeRender ? donorFields.workAddressCountry.defaultValue : ''}" />
					<form:errors class="formError" path="workAddressCountry" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.zipcode.hidden != true }">
				<div>
					<form:label path="workAddressZipcode">${donorFields.zipcode.displayName}</form:label>
					<form:input path="workAddressZipcode"
						value="${firstTimeRender ? donorFields.workAddressZipcode.defaultValue : ''}" />
				         <form:errors class="formError" path="workAddressZipcode" delimiter=", "></form:errors>
				</div>
			</c:if> 
                      <c:if test="${donorFields.preferredAddressType.hidden != true }">
                        <div>
                          <form:label path="preferredAddressType">${donorFields.preferredAddressType.displayName}</form:label>
                           <form:select path="preferredAddressType">
			             <form:option value="" selected="selected">Address Types</form:option>
					<c:forEach var="addressType" items="${addressTypes}">
						<form:option value="${addressType.id}">${addressType.preferredAddressType}</form:option>
					</c:forEach>
			  </form:select>
			 <form:errors class="formError" path="preferredAddressType" delimiter=", "></form:errors>
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
