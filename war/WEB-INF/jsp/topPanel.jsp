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
	<p class="topPanelUsername">
		Logged in as ${model.user.username}
	</p>
	<a href="logout.html">Sign Out</a>
</div>

<div id="topPanelTabs" class="topPanelTabs">
	<ul id="topTabs">
		<li class="topPanelTab"><a href="#homeLandingPageContent">Home</a></li>
		<li class="topPanelTab"><a href="#donorsLandingPageContent">Donors</a></li>
		<li class="topPanelTab"><a href="#collectionsLandingPageContent">Collections</a></li>
		<li class="topPanelTab"><a href="#testResultsLandingPageContent">Test
				Results</a></li>
		<li class="topPanelTab"><a href="#productsLandingPageContent">Products</a></li>
		<li class="topPanelTab"><a href="#requestsLandingPageContent">Requests</a></li>
		<li class="topPanelTab"><a href="#usageLandingPageContent">Usage</a></li>
		<li class="topPanelTab"><a href="#reportsLandingPageContent">Reports</a></li>
	</ul>

	<div id="homeLandingPageContent" class="centerContent">
		<h3>Welcome to Vein-To-Vein</h3>
		<div class="infoMessage">
			Vein-to-Vein (V2V) is a system to monitor and analyze data from blood
			collection, testing, and utilization. This is an initiative of the
			Compute For Good (C4G) program at the Georgia Institute of Technology
			in collaboration with the Center for Disease Control and Prevention
			(CDC). <br /> <br /> See <a
				href="http://www.cc.gatech.edu/about/advancing/c4g/" target="_blank">
				http://www.cc.gatech.edu/about/advancing/c4g/ </a> for more information.
		</div>
	</div>

	<div id="donorsLandingPageContent">
		<jsp:include page="donors.jsp" />
	</div>

	<div id="collectionsLandingPageContent">
		<jsp:include page="collections.jsp" />
	</div>

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

</div>