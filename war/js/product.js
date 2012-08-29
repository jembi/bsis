function showUpdateProducts() {
    $('#addProductsPanel').hide();
    $('#updateProductsPanel').show();
    $('#findProductNumberInputRow').show();
    $('.productUpdateField').hide();

    $('#addProductsTab').removeClass("selectedTab");
    $('#updateProductsTab').addClass("selectedTab");
    $('#productSearchButton').show();
    if ($('#isUpdateProduct').val() != 'updateProduct') {
        $('#updateProductId').val("");
    }
}
;

$(document).ready(function() {
    $('#updateProductsPanel').hide();
    $('#addProductsTab').addClass("selectedTab")

    if ($.getUrlVar('collectionNumber') != undefined) {
        $("#collectionNumber").val($.getUrlVar('collectionNumber'));
    }

    $('#updateProductsTab').click(function() {
        $('#productErrorMessagePanel').hide();
        $('#quarantineMsg').hide();
        showUpdateProducts();
        $('#updateProductsForm').attr('action', 'findProduct.html');
        $('#productMessagePanel').html("");
        $('#quarantineMsg').html("");
    });

    $('#addProductResetButton').click(function() {
        $('#addProductAction input[type="text"]').val("");
        $('#addProductAction input[type="radio"]').removeAttr('checked');
        $('#wholeBlood').click();
        $('#productMessagePanel').html("");
        $('#quarantineMsg').hide();
    });

    $('#addProductsTab').click(function() {
        $('#updateProductsPanel').hide();
        $('#quarantineMsg').hide();
        $('#productErrorMessagePanel').hide();
        $('#addProductsPanel').show();
        $('#updateProductsForm').attr('action', 'addProduct.html');
        $('#updateProductsTab').removeClass("selectedTab");
        $('#addProductsTab').addClass("selectedTab");
        $('#isUpdateProduct').val("");
        $('#addProductResetButton').click();
    });

    if ($('#isUpdateProduct').val() == 'updateProduct') {
        showUpdateProducts();
        $('.productUpdateField').show();
        $('#findProductNumberInputRow').hide();
        $('#productSearchButton').hide();
    }

    $('#productAddButton').click(function() {
        if ($.validateForm('addProductAction', "productErrorMessagePanel") == true) {
            $('#addProductAction').submit();
        }
    });

    $('#productUpdateButton').click(function() {
        if ($.validateForm('updateProductAction', "productErrorMessagePanel") == true) {
            $('#updateProductAction').attr('action', 'updateProduct.html');
            $('#updateProductAction').submit();
        }
    });

    $('#productDeleteButton').click(function() {
        $('#updateProductAction').attr('action', 'deleteProduct.html');
        $('#updateProductAction').submit();
    });

    if ($.getUrlVar('update') == "true") {
        $('#updateProductsTab').click();
    }

});


