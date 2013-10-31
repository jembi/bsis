<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

});
</script>

<div id="${tabContentId}">
  <div id="${mainContentId}">

    <form class="formFormatClass splitProductForm">

      <input name="productId" type="hidden" value="${productId}"/>
  
      <div>
        <label style="width: auto;">
          Split this product to create
          ${product.productType.pediProductType.productTypeCode}
          products
        </label>
      </div>
      <div>
        <label style="width: auto;">Number of pedi products to create</label>
        <input name="numProductsAfterSplitting" type="number" min="1" max="10" />
      </div>
  
  </form>

  
  </div>
</div>