$(document).ready(function() {
	$("#findDonorButton").click(function() {
		var findDonorFormData = $("#findDonorForm").serialize();
		$.ajax({
			type : "GET",
			url : "findDonor.html",
			data : findDonorFormData,
			success : function(data) {
				$('#findDonorResult').html(data);
			}
		});
	});
});