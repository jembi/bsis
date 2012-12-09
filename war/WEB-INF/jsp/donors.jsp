<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/donors.css" media="all" />
<script type="text/javascript" src="js/donors.js"></script>
<script type="text/javascript" src="js/collections.js"></script>

<div id="donorsTab" class="leftPanel tabs">
	<div class="breadCrumb"></div>
	<ul>
		<li id="findOrAddDonorsContent">
			<a href="findDonorFormGenerator.html">
				Find Donors
			</a>
		</li>
		<li id="addDonorContent">
			<a href="editDonorFormGenerator.html">
				Add Donor
			</a>
		</li>
		<!-- li id="viewDonorsContent">
			<a href="viewDonors.html">View All Donors</a>
		</li-->
	</ul>
</div>
