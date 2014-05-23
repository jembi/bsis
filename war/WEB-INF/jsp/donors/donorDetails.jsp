<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONOR)">
<div class="formFormatClass printableArea">
  <!-- <br />
   <div class="donorBarcode"></div> -->
  <c:if test="${donorFields.donorNumber.hidden != true }">
    <div>
      <label>${donorFields.donorNumber.displayName}</label>
      <label>${donor.donorNumber}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.firstName.hidden != true }">
    <div>
      <label>${donorFields.firstName.displayName}</label>
      <label>${donor.firstName}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.middleName.hidden != true }">
    <div>
      <label>${donorFields.middleName.displayName}</label>
      <label>${donor.middleName}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.lastName.hidden != true }">
    <div>
      <label>${donorFields.lastName.displayName}</label>
      <label>${donor.lastName}</label>
    </div>
  </c:if>   
  <c:if test="${donorFields.birthDate.hidden != true }">
    <div>
      <label>${donorFields.birthDate.displayName}</label>
      <label style="width:300px">${donor.birthDate}
      <c:if test="${donorFields.birthDateEstimated.hidden != true }">
      	 <c:if test="${donor.birthDateEstimated == true}">
      	 	(${donorFields.birthDateEstimated.displayName})
      	 </c:if>
      </c:if>
      </label>
    </div>
  </c:if>
  <c:if test="${donorFields.age.hidden != true }">
    <div>
      <label>${donorFields.age.displayName}</label>
      <c:if test="${not empty donor.age}">
        <label>${donor.age} years</label>
      </c:if>
      <c:if test="${empty donor.age}">
        <label>${donor.age}</label>
      </c:if>
    </div>
  </c:if>
  
  <c:if test="${donorFields.gender.hidden != true }">
    <div>
      <label>${donorFields.gender.displayName}</label>
      <label>${donor.gender}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.idType.hidden != true }">
    <div>
      <label>${donorFields.idType.displayName}</label>
      <label>${donor.idType.idType}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.idNumber.hidden != true }">
    <div>
      <label>${donorFields.idNumber.displayName}</label>
      <label>${donor.idNumber}</label>
    </div>
  </c:if>
   <c:if test="${donorFields.preferredLanguage.hidden != true }">
    <div>
      <label>${donorFields.preferredLanguage.displayName}</label>
      <label>${donor.preferredLanguage.preferredLanguage}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.mobileNumber.hidden != true }">
    <div>
      <label>${donorFields.mobileNumber.displayName}</label>
      <label>${donor.mobileNumber}</label>
    </div>
  </c:if>
 
  <c:if test="${donorFields.workNumber.hidden != true }">
    <div>
      <label>${donorFields.workNumber.displayName}</label>
      <label>${donor.workNumber}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.homeNumber.hidden != true }">
    <div>
      <label>${donorFields.homeNumber.displayName}</label>
      <label>${donor.homeNumber}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.homeAddress.hidden != true }">
    <div>
      <label>${donorFields.homeAddress.displayName}</label>
      <label>${donor.homeAddress}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.postalAddress.hidden != true }">
    <div>
      <label>${donorFields.postalAddress.displayName}</label>
      <label>${donor.postalAddress}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.workAddress.hidden != true }">
    <div>
      <label>${donorFields.workAddress.displayName}</label>
      <label>${donor.workAddress}</label>
    </div>
  </c:if>
   <c:if test="${donorFields.mobileNumber.hidden != true }">
    <div>
      <label>${donorFields.mobileNumber.displayName}</label>
      <label>${donor.mobileNumber}</label>
    </div>
  </c:if>
  
  <c:if test="${donorFields.city.hidden != true }">
    <div>
      <label>${donorFields.city.displayName}</label>
      <label>${donor.city}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.province.hidden != true }">
    <div>
      <label>${donorFields.province.displayName}</label>
      <label>${donor.province}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.district.hidden != true }">
    <div>
      <label>${donorFields.district.displayName}</label>
      <label>${donor.district}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.state.hidden != true }">
    <div>
      <label>${donorFields.state.displayName}</label>
      <label>${donor.state}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.country.hidden != true }">
    <div>
      <label>${donorFields.country.displayName}</label>
      <label>${donor.country}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.zipcode.hidden != true }">
    <div>
      <label>${donorFields.zipcode.displayName}</label>
      <label>${donor.zipcode}</label>
    </div>
  </c:if>

  <c:if test="${donorFields.email.hidden != true }">
    <div>
      <label>${donorFields.email.displayName}</label>
      <label>${donor.email}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.preferredContactMethod.hidden != true }">
    <div>
      <label>${donorFields.preferredContactMethod.displayName}</label>
      <label>${donor.preferredContactMethod}</label>
    </div>
  </c:if>
  <c:if test="donorFields.donorPanel.hidden != true}">
  	<c:if test="${not empty donorFields.donorPanel.hidden}">
    <div>
      <label>${donorFields.donorPanel.displayName}</label>
      <label>${donor.donorPanel}</label>
    </div></c:if>
  </c:if>
  <c:if test="${donorFields.dateOfLastDonation.hidden != true }">
    <div>
      <label>${donorFields.dateOfLastDonation.displayName}</label>
      <label>${donor.dateOfLastDonation}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.notes.hidden != true }">
    <div>
      <label>${donorFields.notes.displayName}</label>
      <label>${donor.notes}</label>
    </div>
  </c:if>
  <br />
  <div>
    <label>${donorFields.lastUpdatedTime.displayName}</label>
    <label style="width: auto;">${donor.lastUpdated}</label>
  </div>
  <div>
    <label>${donorFields.lastUpdatedBy.displayName}</label>
    <label style="width: auto;">${donor.lastUpdatedBy}</label>
  </div>
    <hr />
  </div>
  </sec:authorize>
