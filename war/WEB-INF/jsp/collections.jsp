<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/collections.css" media="all"/>
<script type="text/javascript" src="js/collections.js"></script>

<div id="collectionsTab" class="leftPanel tabs">
	<ul>
		<li id="findOrAddCollectionsContent"><a
			href="findCollectionFormGenerator.html">Find Collections</a></li>
		<li id="addCollectionsContent"><a
			href="editCollectionFormGenerator.html">New Collection</a></li>
	</ul>
</div>
