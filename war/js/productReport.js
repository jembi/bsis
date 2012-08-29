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

    $('#allProducts').click(function() {
        if ("checked" == $('#allProducts').attr('checked')) {
            $('.productTypeOption').removeAttr("checked");
        }
    });

    $('.productTypeOption').click(function() {
        if ("checked" == $('#allProducts').attr('checked')) {
            $('#allProducts').removeAttr("checked");
        }
    });

     $('#productsReportButton').click(function() {
        if ($.validateDate($('#collectionFromDate'), $('#productsErrorMessagePanel')) == true) {
            if ($.validateDate($('#collectionToDate'), $('#productsErrorMessagePanel')) == true) {
                $('#productsReportForm').submit();
            }
        }
    });
});
