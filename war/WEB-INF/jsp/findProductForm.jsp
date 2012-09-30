<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script>
  $("#findProductButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findProductFormData = $("#findProductForm").serialize();
    $.ajax({
      type : "GET",
      url : "findProduct.html",
      data : findProductFormData,
      success : function(data) {
        $('#findProductResult').html(data);
        window.scrollTo(0, document.body.scrollHeight);
      }
    });
  });

  $("#findProductFormTypes").multiselect({
    position : {
      my : 'left top',
      at : 'right center'

    }
  });

  $("#findProductFormAvailabilityStatuses").multiselect({
    position : {
      my : 'left top',
      at : 'right center',
      multiple : false,
      selectedList : 1,
      header : false
    }
  });
</script>

<form:form method="GET" commandName="findProductForm"
	id="findProductForm" class="findProductForm">
	<table>
		<thead>
			<tr>
				<td><b>Find Products</b></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="productNumber">${model.productNoDisplayName}</form:label></td>
				<td><form:input path="productNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="collectionNumber">${model.collectionNoDisplayName}</form:label></td>
				<td><form:input path="collectionNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="types">${model.productTypeDisplayName} </form:label></td>
				<td><form:select path="types" id="findProductFormTypes">
						<form:option value="wholeBlood" label="Whole Blood" />
						<form:option value="rcc" label="RCC" />
						<form:option value="ffp" label="FFP" />
						<form:option value="platelets" label="Platelets" />
						<form:option value="partialPlatelets" label="Partial Platelets" />
					</form:select></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b><i>Filter Products</i></b></td>
			</tr>
			<tr>
				<td><form:label path="availability">${model.isIssuedDisplayName}</form:label></td>
				<td><form:select path="availability"
						id="findProductFormAvailabilityStatuses">
						<form:option value="available" label="Available" selected="selected" />
						<form:option value="notAvailable" label="Not Available"
							selected="selected" />
					</form:select></td>
			</tr>
			<tr>
				<td />
				<td><button type="button" id="findProductButton">Find
						product</button></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="findProductResult"></div>