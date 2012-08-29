$(document).ready(function() {

    $('#updateUserButton').click(function() {
        if ($('#selfUser').length > 0) {
            $('#userAction').attr('action', 'updateSelfUser.html');
        }
        else {
            $('#userAction').attr('action', 'admin-updateExistingUser.html');
        }
        if ($.validateForm('userAction', "userValidationErrorMessagePanel") == true) {
            $('#userAction').submit();
        }
    });

    $('#deleteUserButton').click(function() {
        $('#userAction').attr('action', 'admin-deleteUser.html');
        $('#userAction').submit();
    });

    $('#showPassword').change(function() {
        var thisCheck = $(this);
        if (thisCheck.is(':checked')) {
            document.getElementById('password').type = 'text';
        }
        if (!thisCheck.is(':checked')) {
            document.getElementById('password').type = 'password';
        }
    });
});
