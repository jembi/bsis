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

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>

<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<c:set var="findProductFormId">findProductForm-${unique_page_id}</c:set>

<c:set var="findProductFormSearchBySelectorId">findProductFormSearchBySelectorId-${unique_page_id}</c:set>
<c:set var="findProductFormProductTypeSelectorId">findProductFormProductTypeSelectorId-${unique_page_id}</c:set>
<c:set var="findProductFormStatusSelectorId">findProductFormStatusSelectorId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${tabContentId}").find(".findProductButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findProductFormData = $("#${findProductFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findProductResults");
    //showLoadingImage(resultsDiv);
    $.ajax({
      type : "GET",
      url : "findProduct.html",
      data : findProductFormData,
      success: function(data) {
                 animatedScrollTo(resultsDiv);
                 resultsDiv.html(data);
               },
      error: function(data) {
               showErrorMessage("Something went wrong. Please try again later.");        
             }
    });
  });

  $("#${tabContentId}").find(".clearFindFormButton").button({
    icons : {
      
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
    // show the appropriate input based on default search by
    $("#${findProductFormId}").find(".searchBy").trigger("change");
  }

  $("#${findProductFormId}").find(".searchBy").change(toggleSearchBy);
  $("#${findProductFormId}").find(".searchBy").multiselect({
    selectedList: 1,
    multiple: false,
    header: false
  });
  // first time trigger change event so that the input box is shown
  $("#${findProductFormId}").find(".searchBy").trigger("change");

  function toggleSearchBy() {

    var form = $("#${findProductFormId}");
    var searchBy = form.find(".searchBy").val();
    // jquery ui multiselect must be destroyed before hiding the select options
    destroyProductTypeMultiSelect();
    form.find(".hidableDiv").each(function() {
      $(this).hide();
    });

    switch(searchBy) {
      case "collectionNumber":   form.find(".collectionNumberInput").show();
                               break;
      case "productType": form.find(".productTypeInput").show();
                           createProductTypeMultiSelect();
                            break;
    }
  }

  function createProductTypeMultiSelect() {
    $("#${findProductFormId}").find(".productTypeSelector").multiselect({
      position : {
        my : 'left top',
        at : 'right center'
      },
      noneSelectedText: 'None Selected',
      selectedText: function(numSelected, numTotal, selectedValues) {
                      if (numSelected == numTotal) {
                        return "Any Product Type";
                      }
                      else {
                        var checkedValues = $.map(selectedValues, function(input) { return input.title; });
                        return checkedValues.length ? checkedValues.join(', ') : 'Any Product Type';
                      }
                    }
    });
    $("#${findProductFormId}").find(".productTypeSelector").multiselect("checkAll");
  }

  function destroyProductTypeMultiSelect() {
    $("#${findProductFormId}").find(".productTypeInput").multiselect("destroy");
  }

  function getDateExpiresFromInput() {
    return $("#${findProductFormId}").find(".dateExpiresFrom");  
  }

  function getDateExpiresToInput() {
    return $("#${findProductFormId}").find(".dateExpiresTo");  
  }

   $("#${findProductFormId}").find(".productStatusSelector").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    },
//    header: false,
    minWidth: 250,
    noneSelectedText: 'None selected',
//    click: function(e) {
//       if( $(this).multiselect("widget").find("input:checked").length == 0 ){
//       return false;
//       }
//     },
    selectedText: function(numSelected, numTotal, selectedValues) {
                    if (numSelected == numTotal) {
                      return "All Products";
                    }
                    else {
                      var checkedValues = $.map(selectedValues, function(input) { return input.title; });
                      return checkedValues.length ? checkedValues.join(', ') : 'None Selected';
                    }
                  }

  });

  getDateExpiresFromInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 365,
    dateFormat : "dd/mm/yy",
    yearRange : "c-100:c+1",
    onSelect : function(selectedDate) {
      getDateExpiresToInput().datepicker("option", "minDate", selectedDate);
    }
  });

  getDateExpiresToInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 365,
    dateFormat : "dd/mm/yy",
    yearRange : "c-100:c+1",
    onSelect : function(selectedDate) {
      getDateExpiresFromInput().datepicker("option", "maxDate", selectedDate);
    }
  });

  // child div shows donor information. bind this div to productSummaryView event
  $("#${tabContentId}").bind("productSummaryView",
      function(event, content) {
        $("#${mainContentId}").hide();
        $("#${childContentId}").html(content);
      });

  $("#${tabContentId}").bind("productSummarySuccess",
      function(event, content) {
        $("#${mainContentId}").show();
        $("#${childContentId}").html("");
        $("#${tabContentId}").find(".productsTable").trigger("refreshResults");
      });

  $("#${findProductFormId}").submit(function(e) {
    e.preventDefault();
  })
});
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_COMPONENT)">
<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Find Products</b>
    <div class="tipsBox ui-state-highlight" style="display:none;">
      <p>
        ${tips['products.find']}
      </p>
    </div>
    <form:form method="GET" commandName="findProductForm" id="${findProductFormId}"
      class="formFormatClass">
      <div>
        <form:label path="searchBy">Find Product by </form:label>
        <!-- need to set id searchBy selector otherwise the
         search by selector in collections page will get the same id  -->
        <form:select path="searchBy"
                     id="${findProductFormSearchBySelectorId}"
                     class="searchBy">
          <form:option value="collectionNumber" label="${productFields.collectionNumber.displayName}" />
          <form:option value="productType" label="${productFields.productType.displayName}" />
        </form:select>
  
        <div class="collectionNumberInput hidableDiv" style="display:none">
          <form:input path="collectionNumber" placeholder="Collection Number" />
        </div>
    
        <div class="productTypeInput hidableDiv" style="display:none">
        <!-- Quick Fix for ID Duplication issue.
        When ID is duplicated selecting in the Find Product form triggers a selection change in
        Find Request form.
        The merge pull request here https://github.com/ehynds/jquery-ui-multiselect-widget/pull/347
        does not solve the issue. Just work around the problem for now by giving this select a unique ID.
        -->
          <form:select path="productTypes"
                       id="${findProductFormProductTypeSelectorId}"
                       class="productTypeSelector">
            <c:forEach var="productType" items="${productTypes}">
              <form:option value="${productType.id}" label="${productType.productType}" />
            </c:forEach>
          </form:select>
        </div>
      </div>
  
      <div>
          <form:label path="status">Product Status</form:label>
          <form:select path="status"
                       id="${findProductFormStatusSelectorId}"
                       class="productStatusSelector">
            <form:option value="QUARANTINED" label="Quarantined" selected="selected" />
            <form:option value="AVAILABLE" label="Available" selected="selected" />
            <form:option value="EXPIRED" label="Expired" selected="selected" />
            <form:option value="UNSAFE" label="Unsafe" selected="selected" />
            <form:option value="ISSUED" label="Issued" selected="selected" />
            <form:option value="USED" label="Used" selected="selected" />
            <form:option value="SPLIT" label="Split" selected="selected" />
            <form:option value="DISCARDED" label="Discarded" selected="selected" />
          </form:select>
      </div>
  
      <br />
      <br />

      </form:form>

    <div class="formFormatClass">
      <div>
        <label></label>
        <button type="button" class="findProductButton">
          Find products
        </button>
        <button type="button" class="clearFindFormButton">
          Clear form
        </button>
      </div>
    </div>

  <div class="findProductResults"></div>

</div>
  <div id="${childContentId}"></div>
</div>

</sec:authorize>