$(document).ready(function() {
    $("#requestsErrorMessagePanel").hide();
    $("#requestDate").datepicker();
    $("#requiredDate").datepicker();
    var today = new Date();
    $.setDefaultDate($('#requestDate'), today);

    $('#addRequestButton').click(function() {
        $('#requestMessagePanel').hide();
        if ($.validateForm('addRequestsForm', "requestsErrorMessagePanel") == true) {
            $('#addRequestsForm').submit();
        }
    });

    $('#updateRequestButton').click(function() {
        $('#requestMessagePanel').hide();
        if ($.validateForm('updateRequestsForm', "requestsErrorMessagePanel") == true) {
            $('#updateRequestsForm').submit();
        }
    });

    $('#deleteRequestButton').click(function() {
        $('#updateRequestsForm').attr("action", "deleteExistingRequest.html");
        $('#updateRequestsForm').submit();
    });
});
