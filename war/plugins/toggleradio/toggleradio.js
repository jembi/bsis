$.fn.toggleRadio = function() {
  var allselected = this;

  return this.click(function(e) {
    if ($(this).attr('toggled') == 'true') {
      // if the clicked button was already checked
      allselected.each(function() {
        $(this).attr({
          checked : false,
          toggled : false
        });
      });
    } else {
      // if the clicked button was not already checked
      $(this).attr({
        checked : true,
        toggled : true
      }).siblings().attr({
        checked : false,
        toggled : false
      });
    }
  }).each(function() {
    // initialize the 'toggled' attribute to match the initial state of the
    // radio buttons
    if ($(this).is(':checked')) {
      $(this).attr('toggled', true);
    } else {
      $(this).attr('toggled', false);
    }
  });
};
