<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div id="v2vHeading"><a href="/v2v">Vein-to-Vein</a></div>
<div class="headlink">
    <p class="topPanelUsername">Logged in as ${model.user.username}|
        <a
                <c:if test="${model.user.type=='admin'}">
                    href="admin-findUser.html?username=${model.user.username}"
                </c:if>
                <c:if test="${not model.user.type=='admin'}">
                    href="findUser.html?username=${model.user.username}"
                </c:if>
                >
            edit profile</a>
    </p>
    <a href="collections.html">Work As Technician</a>
    <a href="logout.html">Logout</a>
</div>
<div id="topPanelTabs">
    <ul id="topTabs" class="topPanelTabs adminTopPanelTabs">
        <li id="locationTypesTabOption"><a href="admin-locationTypesLandingPage.html">Location Types</a></li>
        <li id="locationsTabOption"><a href="admin-locationsLandingPage.html">Locations</a></li>
        <li id="usersTabOption"><a href="admin-userLandingPage.html">Users</a></li>
        <li id="displayNamesConfigTabOption"><a href="admin-displayNamesConfigLandingPage.html">Modify Language</a></li>
        <li id="displayFieldsConfigTabOption"><a href="admin-displayFieldsConfigLandingPage.html">Input Fields</a></li>
        <li id="reportConfigTabOption"><a href="admin-reportConfigLandingPage.html">Reports</a></li>
        <%--NOTE : Comment out for XAMPP version--%>
        <li id="createDataTabOption"><a href="admin-createData.html">Create Data</a></li>
    </ul>
</div>