<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="formFormatClass printableArea">
  <br />
  <div class="donorBarcode"></div>
  <c:if test="${donorFields.donorNumber.hidden != true }">
    <div>
      <label>${donorFields.donorNumber.displayName}</label>
      <label>${donor.donorNumber}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.firstName.hidden != true }">
    <div>
      <label>${donorFields.firstName.displayName}</label>
      <label>
      <c:if test="${donor.title != 'Blank' }">
           ${donor.title}
      </c:if>
      ${donor.firstName}</label>
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
  <c:if test="${donorFields.nationalID.hidden != true }">
    <div>
      <label>${donorFields.nationalID.displayName}</label>
      <label>${donor.nationalID}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.bloodGroup.hidden != true }">
    <div>
      <label>${donorFields.bloodGroup.displayName}</label>
      <label>${donor.bloodGroup}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.address.hidden != true }">
    <div>
      <label>${donorFields.address.displayName}</label>
      <label>${donor.address}</label>
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
  <c:if test="${donorFields.phoneNumber.hidden != true }">
    <div>
      <label>${donorFields.phoneNumber.displayName}</label>
      <label>${donor.phoneNumber}</label>
    </div>
  </c:if>
  <c:if test="${donorFields.otherPhoneNumber.hidden != true }">
    <div>
      <label>${donorFields.otherPhoneNumber.displayName}</label>
      <label>${donor.otherPhoneNumber}</label>
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
