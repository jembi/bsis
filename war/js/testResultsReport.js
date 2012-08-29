$(document).ready(function() {
    $("#collectionFromDate").datepicker();
    $("#collectionToDate").datepicker();
    var today = new Date();
    var firstDayOfYear = new Date();
    firstDayOfYear.setFullYear(today.getFullYear());
    firstDayOfYear.setMonth(0);
    firstDayOfYear.setDate(1);
    $.setDefaultDate($('#collectionFromDate'),firstDayOfYear);
    $.setDefaultDate($('#collectionToDate'),today);

    $('#allTestResults').click(function() {
        if("checked"==$('#allTestResults').attr('checked')){
            $('.indicatorOption').removeAttr("checked");
        }
    });

    $('.indicatorOption').click(function() {
        if("checked"==$('#allTestResults').attr('checked')){
            $('#allTestResults').removeAttr("checked");
        }
    });

    $('#testResultsReportButton').click(function() {
        if ($.validateDate($('#collectionFromDate'), $('#testResultsErrorMessagePanel')) == true) {
            if ($.validateDate($('#collectionToDate'), $('#testResultsErrorMessagePanel')) == true) {
                $('#testResultsReportForm').submit();
            }
        }
    });
});
