$(document).ready(function() {
	var oTable = $(".donorTable").dataTable({
		"bJQueryUI" : true
	});

	$(".donorTable tbody").dblclick(function(event) {
		console.log("here");
		$(oTable.fnSettings().aoData).each(function() {
			$(this.nTr).removeClass('row_selected');
		});
		$(event.target.parentNode).addClass('row_selected');
		var elements = $(event.target.parentNode).children();
		var donorId = elements[0].innerHTML;

		$.ajax({
			url : "editDonor.html",
			type : "GET",
			data : {
				donorNumber : donorId
			},
			success : function(data) {
				console.log(data);
				$("<div id='editDonordata'>" + data + "</div>").dialog({
					autoOpen : true,
					height : 300,
					width : 500,
					modal : true,
					buttons : {
						"Update" : function() {
							$(this).dialog("close")
						},
						"Cancel" : function() {
							$(this).dialog("close")
						}
					},
					close : function() {
						$("#editDonordata").remove();
					}

				});
				$("#editDonordata").dialog("open");
			}
		});
	});
});