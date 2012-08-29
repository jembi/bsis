$(document).ready(function() {
    $("#testResultsErrorMessagePanel").hide();
    $("#testResultDate").datepicker();
    var today = new Date();
    $.setDefaultDate($('#testResultDate'), today);

    $('#addTestResultButton').click(function() {
        $('#testResultsMessagePanel').hide();
        if ($.validateForm('addTestResultsForm', "testResultsErrorMessagePanel") == true) {
            $('#addTestResultsForm').submit();
        }
    });

    $('#updateTestResultButton').click(function() {
        $('#testResultsMessagePanel').hide();

        if ($.validateForm('updateTestResultsForm', "testResultsErrorMessagePanel") == true) {
            $('#updateTestResultsForm').submit();
        }
    });

    $('#deleteTestResultButton').click(function() {
        $('#updateTestResultsForm').attr("action", "deleteExistingTestResult.html");
        $('#updateTestResultsForm').submit();
    });
});
