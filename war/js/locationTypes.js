$(document).ready(function() {

    $('#locationTypeResetButton').click(function() {
        $('#locationTypeAction input[type="text"]').val("");
        $('#updateLocationTypeButton').hide();
        $('#deleteLocationTypeButton').hide();
        $('#createLocationTypeButton').show();
    });

    $('#updateLocationTypeButton').click(function() {
        $('#locationTypeAction').attr('action', 'admin-updateLocationType.html');
        $('#locationTypeAction').submit();
    });

    $('#deleteLocationTypeButton').click(function() {
        $('#locationTypeAction').attr('action', 'admin-deleteLocationType.html');
        $('#locationTypeAction').submit();
    });

    if ($('#locationTypeId').length > 0) {
        $('#updateLocationTypeButton').show();
        $('#deleteLocationTypeButton').show();
        $('#createLocationTypeButton').hide();
    }
    else {
        $('#updateLocationTypeButton').hide();
        $('#deleteLocationTypeButton').hide();
    }

    $('#allLocationTypes').hide();

    $('#updateLocationTypesTab').click(function() {
        $('#locationTypes').hide();
        $('#allLocationTypes').show();
        $('#updateLocationTypesTab').addClass('selectedTab');
        $('#addLocationTypesTab').removeClass('selectedTab');


    });

    $('#addLocationTypesTab').click(function() {
        $('#locationTypes').show();
        $('#allLocationTypes').hide();
        $('#addLocationTypesTab').addClass('selectedTab');
        $('#updateLocationTypesTab').removeClass('selectedTab');

    });

    if($.getUrlVar("view")=="true")
    {
        $('#updateLocationTypesTab').click();
    }
});