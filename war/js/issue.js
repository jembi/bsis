function setTodayDate(dateElement) {
    var today = new Date();
    $.setDefaultDate(dateElement, today);
}

function updateQuantitySelected() {
    var quantitySelected = $('.issueCheckBox:checked').length * 1.0;

    function getPartialPlateletsSelected() {
        var partialPlateletsCount = 0.0;
        var arr = $('.issueCheckBox:checked').parent().siblings('.productType');
        arr.each(function(index, element) {
            element = jQuery(element);
            var productType = element.html();
            if (productType == 'partialPlatelets') {
                partialPlateletsCount++;
            }
        });
        return partialPlateletsCount;
    }

    var partialPlateletsSelected = getPartialPlateletsSelected();

    quantitySelected = quantitySelected - partialPlateletsSelected + (partialPlateletsSelected / 6.0);
    if (quantitySelected%1!=0) {
        $('#quantitySelected').text(quantitySelected.toFixed(2));
    }
    else{
        $('#quantitySelected').text(Math.floor(quantitySelected));
    }
    if (quantitySelected == 0 || quantitySelected%1!=0) {
        $('#issueSelectedProductsButton').attr('disabled', 'disabled');
    }
    else {
        $('#issueSelectedProductsButton').removeAttr('disabled');
    }
}
$(document).ready(function() {
    updateQuantitySelected();
    $(".issueDate").datepicker();
    $('#issueSelectedProductsButton').click(function() {
        $('#issueMessagePanel').hide();

        var proceed = true;
        $(".issueDate").each(function(index, issueDateElement) {
            var result = $.validateDate(issueDateElement, $('#issueProductsErrorPanel'));
            if (result == false) {
                proceed = false;
                return false;
            }
        });
        if (proceed == true) {
            $('#quantityIssued').val($('#quantitySelected').text());
            $('#issueSelectedProductsForm').submit();
        }
    });

    $('.issueCheckBox').click(function() {
        var issueDateCheckBox = jQuery(this);
        var issueDateElement = issueDateCheckBox.parent().siblings().children('.issueDate');
        if (issueDateCheckBox.attr('checked') == 'checked') {
            setTodayDate(issueDateElement);
        }
        else {
            issueDateElement.val('');
        }
        updateQuantitySelected();
    })

    if ($('#issueProductsErrorPanel').html().trim().length == 0) {
        $('#issueProductsErrorPanel').hide();
    }

    if ($('#issueMessagePanel').html().trim().length == 0) {
        $('#issueMessagePanel').hide();
    }
});
