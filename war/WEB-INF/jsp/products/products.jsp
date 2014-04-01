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

<link type="text/css" rel="stylesheet" href="css/products.css" media="all" />
<script type="text/javascript" src="js/products.js"></script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_COMPONENT)">
<div id="productsTab" class="leftPanel tabs">
  <ul>
    <li id="findOrAddProductsContent"><a
      href="findProductFormGenerator.html">Find Products</a></li>
    <li id="addProductCombinationsContent"><a
      href="addProductCombinationFormGenerator.html">Create products</a></li>
    <li id="addProductsContent"><a
      href="addProductFormGenerator.html">Add Product</a></li>
      <li id="recordProductsContent"><a
      href="recordProductFormGenerator.html">Record Product</a></li>
  </ul>
</div>
</sec:authorize>
