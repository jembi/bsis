function showUpdateCollections() {
    $('#addCollectionsPanel').hide();
    $('#updateCollectionsPanel').show();
    $('.collectionUpdateField').hide();

    $('#addCollectionsTab').removeClass("selectedTab");
    $('#updateCollectionsTab').addClass("selectedTab");
    $('#collectionSearchButton').show();
    if ($('#isUpdateCollection').val() != 'updateCollection') {
        $('#updateCollectionId').val("");
    }
    $('#collectionErrorMessagePanel').hide();
}
;

function setTodayDate(inputElement) {
    if (inputElement.val() == '') {
        var d = new Date();
        var today = $.zerofill(d.getMonth() + 1, 2) + "/" + $.zerofill(d.getDate(), 2) + "/" + (d.getFullYear()).toString();
        inputElement.val(today);
    }
}
;

$(document).ready(function() {
    $('#updateCollectionsPanel').hide();
    $('#addCollectionsTab').addClass("selectedTab");
    $("#collectionDate").datepicker();
    $("#updateCollectionDate").datepicker();
    setTodayDate($("#collectionDate"));
    if ($.getUrlVar('collectionNumber') != undefined) {
        $("#collectionNumber").val($.getUrlVar('collectionNumber'));
    }

    $('#updateCollectionsTab').click(function() {
        showUpdateCollections();
        $('#updateCollectionsForm').attr('action', 'findCollection.html');
        $('.infoMessage').hide();
    });

    $('#addCollectionResetButton').click(function() {
        $('#addCollectionAction input[type="text"]').val("");
        $('#addCollectionAction select').val("");
        $('#otherDonorType').click();
        setTodayDate($("#collectionDate"));
    });

    $('#addCollectionsTab').click(function() {
        $('#updateCollectionsPanel').hide();
        $('#collectionErrorMessagePanel').hide();
        $('#addCollectionsPanel').show();
        $('#updateCollectionsForm').attr('action', 'addCollection.html');
        $('#updateCollectionsTab').removeClass("selectedTab");
        $('#addCollectionsTab').addClass("selectedTab");
        $('#isUpdateCollection').val("");
        $('#addCollectionResetButton').click();
        $('.infoMessage').hide();
    });

    if ($('#isUpdateCollection').val() == 'updateCollection') {
        showUpdateCollections();
        $('.collectionUpdateField').show();
        $('#collectionSearchButton').hide();
    }

    if ($.getUrlVar('update') == "true") {
        $('#updateCollectionsTab').click();
    }

    $('#createCollectionButton').click(function() {
        if ($.validateForm('addCollectionAction', "collectionErrorMessagePanel") == true) {
            $('#addCollectionAction').submit();
        }
    });

    $('#collectionUpdateButton').click(function() {
        $('#updateCollectionMessagePanel').hide();
        if ($.validateForm('updateCollectionsForm', "collectionErrorMessagePanel") == true) {
            $('#updateCollectionsForm').submit();
        }
    });

    $('#collectionDeleteButton').click(function() {
        $('#updateCollectionsForm').attr('action', 'deleteCollection.html');
        $('#updateCollectionsForm').submit();
    });

    if ($('#collectionErrorMessagePanel').html()!=null && $('#collectionErrorMessagePanel').html().trim().length == 0) {
        $('#collectionErrorMessagePanel').hide();
    }

    if ($('#updateCollectionMessagePanel').html()!=null && $('#updateCollectionMessagePanel').html().trim().length == 0) {
        $('#updateCollectionMessagePanel').hide();
    }
});