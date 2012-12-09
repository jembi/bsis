<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/products.css" media="all" />
<script type="text/javascript" src="js/products.js"></script>

<div id="productsTab" class="leftPanel tabs">
	<ul>
		<li id="findOrAddProductsContent"><a
			href="findProductFormGenerator.html">Find Products</a></li>
		<li id="addProductsContent"><a
			href="editProductFormGenerator.html">Add Product</a></li>
	</ul>
</div>
