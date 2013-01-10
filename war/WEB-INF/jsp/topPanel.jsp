<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script src="js/topPanel.js" type="text/javascript"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div id="v2vHeading">
	<a href="/v2v">VEIN-TO-VEIN</a>
</div>
<div class="headlink">
	<p class="topPanelUsername">Logged in as ${model.user.username}</p>
	<a href="logout.html"><span
				class="ui-icon ui-icon-locked" style="display: inline-block;"></span>Log out</a>
</div>

<div id="topPanelTabs" class="topPanelTabs tabs">
	<ul id="topTabs">
		<li class="topPanelTab"><a href="#homeLandingPageContent"><span
				class="ui-icon ui-icon-home" style="display: inline-block;"></span>Home</a></li>
		<li class="topPanelTab"><a href="#donorsLandingPageContent"><span
				class="ui-icon ui-icon-person" style="display: inline-block;"></span>Donors</a></li>

		<!--  Hide the collections tab for now as we can create collection from the Donor Tab -->		
		<!-- li class="topPanelTab"><a href="#collectionsLandingPageContent"><span
				class="ui-icon ui-icon-disk" style="display: inline-block;"></span>Collections</a></li-->

		<li class="topPanelTab"><a href="#productsLandingPageContent"><span
				class="ui-icon ui-icon-cart" style="display: inline-block;"></span>Products</a></li>
		<li class="topPanelTab"><a href="#testResultsLandingPageContent"><span
				class="ui-icon ui-icon-bookmark" style="display: inline-block;"></span>Test
				Results</a></li>
		<li class="topPanelTab"><a href="#requestsLandingPageContent"><span
				class="ui-icon ui-icon-tag" style="display: inline-block;"></span>Requests</a></li>
		<li class="topPanelTab"><a href="#usageLandingPageContent"><span
				class="ui-icon ui-icon-transferthick-e-w" style="display: inline-block;"></span>Usage</a></li>
		<li class="topPanelTab"><a href="#reportsLandingPageContent"><span
				class="ui-icon ui-icon-clipboard" style="display: inline-block;"></span>Reports</a></li>
		<c:if test="${model.user.isAdmin}">
		<li class="topPanelTab"><a href="#adminLandingPageContent"><span
				class="ui-icon ui-icon-gear" style="display: inline-block;"></span>Admin</a></li>
		</c:if>
	</ul>

	<div id="homeLandingPageContent" class="centerContent" style="padding: 20px;">
		<h3>Welcome to Vein-To-Vein - ${model.versionNumber}</h3>
		<div class="infoMessage">
			C4G Vein-to-Vein (V2V) is a system to monitor blood inventory from collection to transfusion.
			<br /> 
			A collaboration between the Computing For Good (C4G) program at Georgia Tech, the CDC and participating countries.
			<br />
			<br />
			About <a
				href="http://www.cc.gatech.edu/about/advancing/c4g/" target="_blank">
				C4G</a>.
		</div>
	</div>

	<div id="donorsLandingPageContent">
		<jsp:include page="donors.jsp" />
	</div>

	<!-- div id="collectionsLandingPageContent">
		<jsp:include page="collections.jsp" />
	</div-->

	<div id="testResultsLandingPageContent">
		<jsp:include page="testResults.jsp" />
	</div>

	<div id="productsLandingPageContent">
		<jsp:include page="products.jsp" />
	</div>

	<div id="requestsLandingPageContent">
		<jsp:include page="requests.jsp" />
	</div>

	<div id="usageLandingPageContent">
		<jsp:include page="usage.jsp" />
	</div>

	<div id="reportsLandingPageContent">
		<jsp:include page="reports.jsp" />
	</div>

	<c:if test="${model.user.isAdmin}">
		<div id="adminLandingPageContent">
			<jsp:include page="admin/admin.jsp" />
		</div>
	</c:if>
</div>