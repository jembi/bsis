$(document).ready(function() {
    $("#usageErrorMessagePanel").hide();
    $("#usageDate").datepicker();
    var today = new Date();
    $.setDefaultDate($('#usageDate'), today);

    $('#addUsageButton').click(function() {
        $('#usageMessagePanel').hide();
        if ($.validateForm('addUsageForm', "usageErrorMessagePanel") == true) {
            $('#addUsageForm').submit();
        }
    });

    $('#updateUsageButton').click(function() {
        $('#usageMessagePanel').hide();
        if ($.validateForm('updateUsageForm', "usageErrorMessagePanel") == true) {
            $('#updateUsageForm').submit();
        }
    });

    $('#deleteUsageButton').click(function() {
        $('#updateUsageForm').attr("action", "deleteExistingUsage.html");
        $('#updateUsageForm').submit();
    });

    if ($('#usageErrorMessagePanel').html().trim().length == 0) {
        $('#usageErrorMessagePanel').hide();
    }

    if ($('#usageMessagePanel').html().trim().length == 0) {
        $('#usageMessagePanel').hide();
    }
});
