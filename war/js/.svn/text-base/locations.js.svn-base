$(document).ready(function() {

    $('#locationResetButton').click(function() {
        $('#locationAction input[type="text"]').val("");
        $('#locationType').val("");
        $('#locationAction input[type="radio"][value="false"]').click()
        $('#updateLocationButton').hide();
        $('#createLocationButton').show();
    });

    $('#updateLocationButton').click(function() {
        $('#locationAction').attr('action', 'admin-updateLocation.html');
        $('#locationAction').submit();
    });

    $('#deleteLocationButton').click(function() {
        $('#locationAction').attr('action', 'admin-deleteLocation.html');
        $('#locationAction').submit();
    });

    $('#updateLocationButton').click(function() {
        $('#locationAction').attr('action', 'admin-updateLocation.html');
        $('#locationAction').submit();
    });

    if ($('#locationId').length > 0) {
        $('#updateLocationButton').show();
        $('#deleteLocationButton').show();
        $('#createLocationButton').hide();
    }
    else {
        $('#updateLocationButton').hide();
        $('#deleteLocationButton').hide();
    }

    $('#allLocations').hide();

    $('#updateLocationsTab').click(function() {
        $('#locations').hide();
        $('#allLocations').show();
        $('#updateLocationsTab').addClass('selectedTab');
        $('#addLocationsTab').removeClass('selectedTab');


    });

    $('#addLocationsTab').click(function() {
        $('#locations').show();
        $('#allLocations').hide();
        $('#addLocationsTab').addClass('selectedTab');
        $('#updateLocationsTab').removeClass('selectedTab');

    });

    if ($.getUrlVar("view") == "true") {
        $('#updateLocationsTab').click();
    }

});