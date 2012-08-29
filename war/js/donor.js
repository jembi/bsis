$(document).ready(function() {
    $('.createDonorField').hide();

    if ($('#donorsTable').length > 0) {
        $('#donorDetails').hide();
        $('#findDonorButtonPanel').hide();
    }

    $('#createDonorButton').click(function() {
        $('#donorMessagePanel').hide();
        if ($.validateForm('donorAction', "donorValidationErrorMessagePanel") == true) {
            $('#donorAction').attr('action', 'createDonor.html');
            $('#donorAction').submit();
        }
    });

    $('#updateDonorButton').click(function() {
        $('#donorMessagePanel').hide();
        if ($.validateForm('donorAction', "donorValidationErrorMessagePanel") == true) {
            $('#donorAction').attr('action', 'updateDonor.html');
            $('#donorAction').submit();
        }
    });

    $('#deleteDonorButton').click(function() {
        $('#donorAction').attr('action', 'deleteDonor.html');
        $('#donorAction').submit();
    });

    $('.backToFindDonor').click(function() {
        $('#donorAction').attr('action', 'findDonor.html');
        $('#findDonorButtonPanel').show();
        $('#donorDetails').show();
        $('#donorValidationErrorMessagePanel').hide();
        $('#donorErrorMessagePanel').hide();
        $('#donorMessagePanel').hide();
        $('.createDonorField').hide();
        $("#findDonorButton").show();
        $("#updateDonorButton").hide();
        $("#deleteDonorButton").hide();
        $("#donorHistory").hide();
        $("#createDonorButton").hide();
        $(".backToFindDonor").hide();
        $("#donorChoices").hide();
    });

    if ($('#donorScreenType').val() == 'createDonor') {
        $('#findDonorButtonPanel').hide();
        $('.createDonorField').show();
        $('#updateDonorButtonPanel').hide();
    }
    else if ($('#donorScreenType').val() == 'updateDonor') {
        $('#findDonorButtonPanel').hide();
        $('.createDonorField').show();
        $('#createDonorButtonPanel').hide();
    }
    else if ($('#donorScreenType').val() == 'multipleDonors') {
        $('#findDonorButtonPanel').hide();
        $('#createDonorButtonPanel').hide();
        $('#updateDonorButtonPanel').hide();
    }
    else if ($('#donorScreenType').val() == 'init') {
        $('#createDonorButtonPanel').hide();
        $('#updateDonorButtonPanel').hide();
        $('.createDonorField').hide();
    }

    var toggleDonorPanel = function() {
        if ($('#donorHistoryTable').is(":visible")) {
            $('#donorHistoryTable').hide();
            $('#donorHistoryPanelButton').attr("src", "images/plus.png")
        }
        else if (!($('#donorHistoryTable').is(":visible"))) {
            $('#donorHistoryTable').show();
            $('#donorHistoryPanelButton').attr("src", "images/minus.png")
        }
    };
    $('#donorHistoryHeadingPanel').click(toggleDonorPanel);
    $('#donorHistoryTable').hide();

    if ($('#donorValidationErrorMessagePanel').html().trim().length == 0) {
        $('#donorValidationErrorMessagePanel').hide();
    }

    if ($('#donorMessagePanel').html().trim().length == 0) {
        $('#donorMessagePanel').hide();
    }

});
