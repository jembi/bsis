$(document).ready(function() {

    $('#createUserButton').click(function() {
        if ($.validateForm('userAction', "userValidationErrorMessagePanel") == true) {
            $('#userAction').submit();
        }
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
