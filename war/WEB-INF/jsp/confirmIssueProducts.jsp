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

<script>
	$(document).ready(function() {
	  $.each( $("#${tabContentId}").find(".radioButtonParent"),
	      		function() {
	    				$(this).find("input:first").attr('checked', true);
	  				});

	  $("#${tabContentId}").find('input[type="radio"]').change(sendSelectionOfInputsToParentDialog);

	  function sendSelectionOfInputsToParentDialog() {
	    var productVolumes = {};
  		$.each($("#${tabContentId}").find('input[type="radio"]:checked'),
		    	 function() {
		  		   productVolumes[$(this).prop("name")] = $(this).val();
					 });
  		$("#${tabContentId}").parent().trigger("updateProductVolumeSelection", productVolumes);
	  }

	  sendSelectionOfInputsToParentDialog();

	});
</script>

<div id="${tabContentId}">

	<div class="formInTabPane">
		<label>Please select the product quantities to issue for this request:</label>
			<div>
				<label style="width: auto;">${model.requestFields.requestNumber.displayName}</label>
				<label style="width: auto;">${model.request.requestNumber}</label>
			</div>
			<div>
				<label style="width: auto;">${model.requestFields.totalVolumeRequested.displayName}</label>
				<label style="width: auto;">${model.request.totalVolumeRequested} ml</label>
			</div>
			<div>
				<label style="width: auto;">${model.requestFields.totalVolumeIssued.displayName}</label>
				<label style="width: auto;">${model.request.totalVolumeIssued} ml</label>
			</div>
	</div>

	<hr />

		<c:forEach var="product" items="${model.allProducts}">
	
			<div class="formInTabPane">
	
				<div class="radioButtonParent">
					<label style="font-weight: bold;">${product.productNumber}</label>
		
					<c:forEach var="productVolume" items="${model.productVolumes}">
						<input type="radio"
									 id="${product.productNumber}-${productVolume.volume}"
									 name="${product.id}" value="${productVolume.volume}" style="width: auto;"/>
						<label for="${product.productNumber}-${productVolume.volume}">${productVolume.volume} ml</label>
					</c:forEach>
	
				</div>
			</div>
		</c:forEach>

</div>
