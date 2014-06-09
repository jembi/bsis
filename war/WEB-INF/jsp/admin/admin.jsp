<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/admin.css" />
<script type="text/javascript" src="js/admin/users.js"></script>
<script type="text/javascript" src="js/admin/roles.js"></script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_ADMIN_INFORMATION)">
<div id="adminTab" class="leftPanel">

  <ul>
  <sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_ADMIN_INFORMATION)">
    <li id="adminWelcomePage">
      <a href="adminWelcomePageGenerator.html">Admin Home</a>
    </li>
   </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_ADMIN_INFORMATION)">
    <li id="viewGeneralConfig">
      <a href="viewGeneralConfig.html">General Configuration</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_LAB_SETUP)">
    <li id="labSetupPage">
      <a href="labSetupPageGenerator.html">Lab setup</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_BLOOD_TESTS)">
    <li id="configureBloodTests">
      <a href="configureBloodTests.html">Blood tests</a>
    </li>
    </sec:authorize>
     <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_BLOOD_TYPING_RULES)">
    <li id="configureBloodTypingRules">
      <a href="configureBloodTypingRules.html">Blood typing rules</a>
    </li>
    </sec:authorize>
     <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_FORMS)">
    <li id="configureForms">
      <a href="configureForms.html">Configure Forms</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_BACKUP_DATA)">
    <li id="backupData">
      <a href="backupDataFormGenerator.html">Backup Data</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_DONATION_SITES )">
    <li id="locationForm">
      <a href="configureLocationsFormGenerator.html">Centers/Sites</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_COMPONENT_COMBINATIONS)">
    <li id="productTypesForm">
      <a href="configureProductTypes.html">Product Types</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_COMPONENT_COMBINATIONS)">
    <li id="productTypeCombinationsForm">
      <a href="configureProductTypeCombinations.html">Product Type <br /> Combinations</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_BLOOD_BAG_TYPES)">
    <li id="bloodBagTypesForm">
      <a href="configureBloodBagTypesFormGenerator.html">Blood Bag Types</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_DONATION_TYPES)">
    <li id="donorTypesForm">
      <a href="configureDonationTypesFormGenerator.html">Donation Types</a>
    </li>
    </sec:authorize>
      <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_REQUESTS)">
    <li id="requestTypesForm">
      <a href="configureRequestTypesFormGenerator.html">Request Types</a>
    </li>
    </sec:authorize>
      <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_BLOOD_BAG_TYPES)">
    <li id="crossmatchTypesForm">
      <a href="configureCrossmatchTypesFormGenerator.html">Crossmatch Types</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_USERS)">
    <li id="configureUsersForm">
      <a href="configureUsersFormGenerator.html">Users</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_ROLES)">
     <li id="configureRoleForm">
      <a href="configureRolesFormGenerator.html">Roles</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_TIPS)">
    <li id="configureTipsForm">
      <a href="configureTipsFormGenerator.html">Tips</a>
    </li>
    </sec:authorize>
    <!-- li id="configureWorksheet">
      <a href="configureWorksheetsFormGenerator.html">Worksheets</a>
    </li-->
    <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_DATA_SETUP)">
    <li id="createSampleData">
      <a href="createSampleDataFormGenerator.html">Sample Data</a>
    </li>
    </sec:authorize>
  </ul>
</div>
</sec:authorize>
