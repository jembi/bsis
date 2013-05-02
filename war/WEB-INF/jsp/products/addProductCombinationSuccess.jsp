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

  $("#${tabContentId}").find(".printButton").button({
    icons : {
      primary : 'ui-icon-print'
    }
  }).click(function() {
    $("#${mainContentId}").find(".printableArea").printArea();
  });

  $("#${tabContentId}").find(".doneButton").button({
    icons : {
      primary : 'ui-icon-check'
    }
  }).click(function() {
    refetchContent("${addAnotherProductUrl}", $("#${tabContentId}"));
  });

  $("#${tabContentId}").find(".addMoreProductsButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    refetchContent("${addAnotherProductUrl}", $("#${tabContentId}"));
  });

});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">
    <div class="successBox ui-state-highlight">
      <img src="images/check_icon.png"
           style="height: 30px; padding-left: 10px; padding-right: 10px;" />
      <span class="successText">
        Products created Successfully.
      </span>
      <div style="margin-left: 55px;">
        You can view the details below. Click "Add more products" to create more products.
      </div>
    </div>
    <div>
      <div class="summaryPageButtonSection" style="text-align: right;">
        <button type="button" class="doneButton">
          Done
        </button>
        <button type="button" class="addMoreProductsButton">
          Add more products
        </button>
        <button type="button" class="printButton">
          Print
        </button>
      </div>

      <div class="formFormatClass">

        <div>
          <label><b>${productFields.collectionNumber.displayName}</b></label>
          <label><b>${collectionNumber}</b></label>
        </div>

        <div>
          <label style="width: auto;">
            The following new products were created 
          </label>
        </div>

        <table class="simpleTable" style="width: 50%;">
          <thead>
            <tr>
              <th>${productFields.productType.displayName}</th>
              <th>${productFields.expiresOn.displayName}</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach var="product" items="${createdProducts}">
            <tr>
              <td style="text-align: left;">${product.productType}</td>
              <td style="text-align: center;">${product.expiresOn}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>

        <br />
        <br />

        <div>
          <label style="width: auto;">
            All products for this collection are shown below. 
          </label>
        </div>

        <table class="simpleTable" style="width: 70%;">
          <thead>
            <tr>
              <th>${productFields.productType.displayName}</th>
              <th>${productFields.createdOn.displayName}</th>
              <th>${productFields.expiresOn.displayName}</th>
              <th>${productFields.status.displayName}</th>
              <th>${productFields.createdBy.displayName}</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach var="product" items="${allProductsForCollection}">
            <tr>
              <td style="text-align: left;">${product.productType}</td>
              <td style="text-align: center;">${product.createdOn}</td>
              <td style="text-align: center;">${product.expiresOn}</td>
              <td style="text-align: center;">${product.status}</td>
              <td style="text-align: center;">${product.createdBy}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>


      </div>

    </div>
  </div>

  <div id="${childContentId}">
  </div>

</div>