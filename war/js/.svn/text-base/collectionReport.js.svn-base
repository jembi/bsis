$(document).ready(function() {
    $("#collectionFromDate").datepicker();
    $("#collectionToDate").datepicker();
    var today = new Date();
    var firstDayOfYear = new Date();
    firstDayOfYear.setFullYear(today.getFullYear());
    firstDayOfYear.setMonth(0);
    firstDayOfYear.setDate(1);
    $.setDefaultDate($('#collectionFromDate'), firstDayOfYear);
    $.setDefaultDate($('#collectionToDate'), today);
    $('#allBloodTypes').click(function() {
        if ("checked" == $('#allBloodTypes').attr('checked')) {
            $('.bloodGroupOption').removeAttr("checked");
        }
    });

    $('.bloodGroupOption').click(function() {
        if ("checked" == $('#allBloodTypes').attr('checked')) {
            $('#allBloodTypes').removeAttr("checked");
        }
    });

    $('#collectionsReportButton').click(function() {
        if ($.validateDate($('#collectionFromDate'), $('#collectionErrorMessagePanel')) == true) {
            if ($.validateDate($('#collectionToDate'), $('#collectionErrorMessagePanel')) == true) {
                $('#collectionsReportForm').submit();
            }
        }
    });
});
