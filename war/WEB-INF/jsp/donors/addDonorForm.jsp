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

<c:set var="addDonorFormId">addDonorForm-${unique_page_id}</c:set>

<c:set var="genderSelectorId">genderSelector-${unique_page_id}</c:set>
<c:set var="addDonorFormDonorPanelsId">addDonorFormDonorPanels-${uniquePageId}</c:set>
<c:set var="addDonorFormContactMethodTypesId">addDonorFormContactMethodTypes-${uniquePageId}</c:set>

<script>
	$(document)
			.ready(
					function() {

						function notifyParentSuccess() {
							// let the parent know we are done
							$("#${tabContentId}").parent().trigger(
									"editDonorSuccess");
						}

						function notifyParentCancel() {
							// let the parent know we are done
							$("#${tabContentId}").parent().trigger(
									"editDonorCancel");
						}

						$("#${mainContentId}")
								.find(".addDonorButton")
								.button({
									icons : {
										primary : 'ui-icon-plusthick'
									}
								})
								.click(
										function() {

											if ("${refreshUrl}" == "findDonorFormGenerator.html") {
												findAndAddNewDonor(
														$("#${addDonorFormId}")[0],
														"${tabContentId}",
														notifyParentSuccess);

											} else {
												addNewDonor(
														$("#${addDonorFormId}")[0],
														"${tabContentId}",
														notifyParentSuccess);
											}
										});

						$("#${mainContentId}").find(".clearFormButton")
								.button().click(refetchForm);

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
							$
									.ajax({
										url : "${refreshUrl}",
										data : {},
										type : "GET",
										success : function(response) {
											$("#${tabContentId}").replaceWith(
													response);
										},
										error : function(response) {
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

						function getDonorPanelSelector() {
							return $("#${tabContentId}").find(
									'select[name="donorPanel"]').multiselect();
						}

						function getGenderSelector() {
							return $("#${mainContentId}").find(
									'select[name="gender"]').multiselect();
						}

						function getPreferredContactMethodSelector() {
							return $("#${tabContentId}").find(
									'select[name="preferredContactMethod"]')
									.multiselect();
						}

						if ("${firstTimeRender}" == "true") {
							$("#${mainContentId}").find(
									'textarea[name="homeAddress"]').html(
									"${donorFields.homeAddress.defaultValue}");
							$("#${mainContentId}").find(
									'textarea[name="notes"]').html(
									"${donorFields.notes.defaultValue}");
							setDefaultValueForSelector(getDonorPanelSelector(),
									"${donorFields.donorPanel.defaultValue}");
							setDefaultValueForSelector(getGenderSelector(),
									"${donorFields.gender.defaultValue}");
							setDefaultValueForSelector(
									getPreferredContactMethodSelector(),
									"${donorFields.preferredContactMethod.defaultValue}");
						}
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
 <sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_DONOR)">
<div id="${tabContentId}">

	<div id="${mainContentId}">

		<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
		</c:if>

		<form:form id="${addDonorFormId}" method="POST"
			class="formFormatClass" commandName="addDonorForm">
			<c:if test="${!donorFields.donorNumber.autoGenerate}">
				<c:if test="${donorFields.donorNumber.hidden != true }">
					<div class="barcodeContainer"></div>
					<div>
						<form:label path="donorNumber">${donorFields.donorNumber.displayName}</form:label>
						<form:input path="donorNumber"
							value="${firstTimeRender ? donorFields.donorNumber.defaultValue : ''}" />
						<form:errors class="formError" path="donor.donorNumber"
							delimiter=", "></form:errors>
					</div>
				</c:if>
			</c:if>
			<c:if test="${donorFields.firstName.hidden != true }">
				<div>
					<form:label path="firstName">${donorFields.firstName.displayName}</form:label>
					<form:input path="firstName"
						value="${firstTimeRender ? donorFields.firstName.defaultValue : ''}" />
					<form:errors class="formError" path="donor.firstName"
						delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.lastName.hidden != true }">
				<div>
					<form:label path="lastName">${donorFields.lastName.displayName}</form:label>
					<form:input path="lastName"
						value="${firstTimeRender ? donorFields.lastName.defaultValue : ''}" />
					<form:errors class="formError" path="donor.lastName"
						delimiter=", "></form:errors>
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
						<form:option value="" label="Gender"/>
						<form:option value="male" label="Male" />
						<form:option value="female" label="Female" />
					</form:select>
					<form:errors class="formError" path="donor.gender" delimiter=", "></form:errors>
				</div>
			</c:if>
			
                   
               <div>
               
               <c:if test="${donorFields.birthDate.hidden != true }">
               <form:label path="birthDate">${donorFields.birthDate.displayName}</form:label>
				<form:input style="width:34px" placeholder="Day" path="dayOfMonth"
					alt="dayOfMonth" title="dayOfMonth" maxlength="2" />


				<form:select path="month" name="Month">
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
					<form:input path="age"
						value="${firstTimeRender ? donorFields.age.defaultValue : ''}" />
					years
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
                        
			<c:if test="${donorFields.email.hidden != true }">
				<div>
					<form:label path="email">${donorFields.email.displayName}</form:label>
					<form:input path="email" />
					<form:errors class="formError" path="donor.email" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.homeAddress.hidden != true }">
				<div>
					<form:label path="homeAddress" class="labelForTextArea">${donorFields.homeAddress.displayName}</form:label>
					<form:textarea path="homeAddress" />
					<form:errors class="formError" path="donor.homeAddress" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.city.hidden != true }">
				<div>
					<form:label path="city">${donorFields.city.displayName}</form:label>
					<form:input path="city"
						value="${firstTimeRender ? donorFields.city.defaultValue : ''}"  delimiter=", "/>
					<form:errors class="formError" path="donor.city" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.province.hidden != true }">
				<div>
					<form:label path="province">${donorFields.province.displayName}</form:label>
					<form:input path="province"
						value="${firstTimeRender ? donorFields.province.defaultValue : ''}" />
					<form:errors class="formError" path="donor.province" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.district.hidden != true }">
				<div>
					<form:label path="district">${donorFields.district.displayName}</form:label>
					<form:input path="district"
						value="${firstTimeRender ? donorFields.district.defaultValue : ''}" />
					<form:errors class="formError" path="donor.district" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.state.hidden != true }">
				<div>
					<form:label path="state">${donorFields.state.displayName}</form:label>
					<form:input path="state"
						value="${firstTimeRender ? donorFields.state.defaultValue : ''}" />
					<form:errors class="formError" path="donor.state" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.country.hidden != true }">
				<div>
					<form:label path="country">${donorFields.country.displayName}</form:label>
					<form:input path="country"
						value="${firstTimeRender ? donorFields.country.defaultValue : ''}" />
					<form:errors class="formError" path="donor.country" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.zipcode.hidden != true }">
				<div>
					<form:label path="zipcode">${donorFields.zipcode.displayName}</form:label>
					<form:input path="zipcode"
						value="${firstTimeRender ? donorFields.zipcode.defaultValue : ''}" />
					<ul>
						<form:errors class="formError" path="donor.zipcode" delimiter=", "></form:errors>
					</ul>
				</div>
			</c:if>
                        <c:if test="${donorFields.postalAddress.hidden != true }">
				<div>
					<form:label path="postalAddress" class="labelForTextArea">${donorFields.postalAddress.displayName}</form:label>
					<form:textarea path="postalAddress" />
					<form:errors class="formError" path="donor.postalAddress" delimiter=", "></form:errors>
				</div>
			</c:if>
                        <c:if test="${donorFields.city.hidden != true }">
				<div>
					<form:label path="postalAddressCity">${donorFields.city.displayName}</form:label>
					<form:input path="postalAddressCity"
						value="${firstTimeRender ? donorFields.postalAddressCity.defaultValue : ''}" />
					<form:errors class="formError" path="donor.postalAddressCity" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.province.hidden != true }">
				<div>
					<form:label path="postalAddressProvince">${donorFields.province.displayName}</form:label>
					<form:input path="postalAddressProvince"
						value="${firstTimeRender ? donorFields.province.defaultValue : ''}" />
					<form:errors class="formError" path="donor.postalAddressProvince" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.district.hidden != true }">
				<div>
					<form:label path="postalAddressDistrict">${donorFields.district.displayName}</form:label>
					<form:input path="postalAddressDistrict"
						value="${firstTimeRender ? donorFields.postalAddressDistrict.defaultValue : ''}" />
					<form:errors class="formError" path="donor.postalAddressDistrict" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.state.hidden != true }">
				<div>
					<form:label path="postalAddressState">${donorFields.state.displayName}</form:label>
					<form:input path="postalAddressState"
						value="${firstTimeRender ? donorFields.postalAddressState.defaultValue : ''}" />
					<form:errors class="formError" path="donor.postalAddressState" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.country.hidden != true }">
				<div>
					<form:label path="postalAddressCountry">${donorFields.country.displayName}</form:label>
					<form:input path="postalAddressCountry"
						value="${firstTimeRender ? donorFields.country.defaultValue : ''}" />
					<form:errors class="formError" path="donor.postalAddressCountry" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.zipcode.hidden != true }">
				<div>
					<form:label path="postalAddressZipcode">${donorFields.zipcode.displayName}</form:label>
					<form:input path="postalAddressZipcode"
						value="${firstTimeRender ? donorFields.postalAddressZipcode.defaultValue : ''}" />
					<ul>
						<form:errors class="formError" path="donor.postalAddressZipcode" delimiter=", "></form:errors>
					</ul>
				</div>
			</c:if>
                        <c:if test="${donorFields.workAddress.hidden != true }">
				<div>
					<form:label path="workAddress" class="labelForTextArea">${donorFields.workAddress.displayName}</form:label>
					<form:textarea path="workAddress" />
					<form:errors class="formError" path="donor.workAddress" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.city.hidden != true }">
				<div>
					<form:label path="workAddressCity">${donorFields.city.displayName}</form:label>
					<form:input path="workAddressCity"/>
					<form:errors class="formError" path="donor.workAddressCity"></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.workAddressProvince.hidden != true }">
				<div>
					<form:label path="workAddressProvince">${donorFields.province.displayName}</form:label>
					<form:input path="workAddressProvince"
						value="${firstTimeRender ? donorFields.workAddressProvince.defaultValue : ''}" />
					<form:errors class="formError" path="donor.workAddressProvince" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.district.hidden != true }">
				<div>
					<form:label path="workAddressDistrict">${donorFields.district.displayName}</form:label>
					<form:input path="workAddressDistrict"
						value="${firstTimeRender ? donorFields.workAddressDistrict.defaultValue : ''}" />
					<form:errors class="formError" path="donor.workAddressDistrict" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.state.hidden != true }">
				<div>
					<form:label path="workAddressState">${donorFields.state.displayName}</form:label>
					<form:input path="workAddressState"
						value="${firstTimeRender ? donorFields.state.defaultValue : ''}" />
					<form:errors class="formError" path="donor.state" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.country.hidden != true }">
				<div>
					<form:label path="workAddressCountry">${donorFields.country.displayName}</form:label>
					<form:input path="workAddressCountry"
						value="${firstTimeRender ? donorFields.workAddressCountry.defaultValue : ''}" />
					<form:errors class="formError" path="donor.workAddressCountry" delimiter=", "></form:errors>
				</div>
			</c:if>
			<c:if test="${donorFields.zipcode.hidden != true }">
				<div>
					<form:label path="workAddressZipcode">${donorFields.zipcode.displayName}</form:label>
					<form:input path="workAddressZipcode"
						value="${firstTimeRender ? donorFields.workAddressZipcode.defaultValue : ''}" />
					<ul>
						<form:errors class="formError" path="donor.workAddressZipcode" delimiter=", "></form:errors>
					</ul>
				</div>
			</c:if>
			<c:if test="${donorFields.mobileNumber.hidden != true }">
				<div>
					<form:label path="mobileNumber">${donorFields.mobileNumber.displayName}</form:label>
					<form:input path="mobileNumber" />
					<ul>
						<form:errors class="formError" path="donor.mobileNumber"
							delimiter=", "></form:errors>
					</ul>
				</div>
			</c:if>
                        <c:if test="${donorFields.homeNumber.hidden != true }">
				<div>
					<form:label path="homeNumber">${donorFields.homeNumber.displayName}</form:label>
					<form:input path="homeNumber" />
					<ul>
						<form:errors class="formError" path="donor.homeNumber"
							delimiter=", "></form:errors>
					</ul>
				</div>
			</c:if>
                        <c:if test="${donorFields.workNumber.hidden != true }">
				<div>
					<form:label path="workNumber">${donorFields.workNumber.displayName}</form:label>
					<form:input path="workNumber" />
					<ul>
						<form:errors class="formError" path="donor.workNumber"
							delimiter=", "></form:errors>
					</ul>
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
					<form:errors class="formError" path="donor.preferredContactMethod"
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
				Add Donor</button>
			<button type="button" class="clearFormButton autoWidthButton">
				Clear form</button>
		</div>
	</div>

	<div id="${childContentId}"></div>

</div>
</sec:authorize>
