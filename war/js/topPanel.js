$(document).ready(function() {

  // This selector will be reused when selecting actual tab widget
  // elements.
  var tab_a_selector = 'ul.ui-tabs-nav a';

  var topPanelTabs = $("#topPanelTabs").tabs();
  var donorsTabs = $("#donorsTab").tabs();
  var collectionsTabs = $("#collectionsTab").tabs();

  // Define our own click handler for the tabs, overriding the default.
  $(".tabs").find(tab_a_selector).click(function() {
    var t = getSelectedTabs();
    console.log("pushstate called " + JSON.stringify(t));
    history.pushState(t, "", "");
    return false;
  });

  var pushState = history.pushState;
  history.pushState = function () {
    console.log(arguments);
    pushState.apply(history, arguments);
  };

  $(window).bind("popstate", function(event) {
    console.log("popstate called");
    console.log(event);
    if (event === undefined || event.originalEvent == undefined ||
        event.originalEvent.state == undefined) {
      console.log("state is null");
      return;
    }
    var state = event.originalEvent.state;
    console.log(state);
    if (state == undefined)
      return;
    if (state.topPanelSelected !== undefined) {
      topPanelTabs.tabs("select", state.topPanelSelected);
      if (state.leftPanelSelected !== undefined) {
        switch(state.topPanelSelected) {
          case 1: donorsTabs.tabs("select", state.leftPanelSelected);
                  break;
          case 2: collectionsTabs.tabs("select", state.leftPanelSelected);
                  break;
          default:
                  break;
        }

        if (state.targetId !== undefined && state.targetContent !== undefined) {
          console.log(state.targetId);
          //$('#'+state.targetId).html("here");
          $('#' + state.targetId).html(state.targetContent);
        }
      }
    }
  });

  history.pushState({topPanelSelected : 0}, "", "")

});