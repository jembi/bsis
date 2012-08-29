<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div id="v2vHeading"><a href="/v2v">Vein-to-Vein</a></div>
<div class="headlink">
    <p class="topPanelUsername">Logged in as ${model.user.username} |

        <c:choose>
            <c:when test="${model.user.type=='admin'}">
                <a href="admin-findUser.html?username=${model.user.username}">
                    edit profile</a>
            </c:when>
            <c:otherwise>
                <a href="findUser.html?username=${model.user.username}">
                    edit profile</a>
            </c:otherwise>
        </c:choose>

    </p>
    <c:if test="${model.user.type=='admin'}"><a href="admin-locationTypesLandingPage.html">Work As Admin</a></c:if>
    <a href="logout.html">Logout</a>
</div>
<div id="topPanelTabs">
    <ul id="topTabs" class="topPanelTabs">
        <li id="donorTabOption"><a href="donorsLandingPage.html">Donors</a></li>
        <li id="collectionTabOption"><a href="collectionsLandingPage.html">Collections</a></li>
        <li id="testResultsTabOption"><a href="testResultsLandingPage.html">Test Results</a></li>
        <li id="productsTabOption"><a href="productsLandingPage.html">Products</a></li>
        <li id="requestsTabOption"><a href="requestsLandingPage.html">Request</a></li>
        <li id="issueTabOption"><a href="issueLandingPage.html">Issue</a></li>
        <li id="usageTabOption"><a href="usageLandingPage.html">Usage</a></li>
        <li id="reportsTabOption"><a href="reports.html">Reports</a></li>
    </ul>
</div>