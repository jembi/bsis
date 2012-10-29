$(document).ready(
    function() {

      // This selector will be reused when selecting actual tab widget
      // elements.
      var tab_a_selector = 'ul.ui-tabs-nav a';

      var topPanelTabs = $("#topPanelTabs").tabs({
        cache : true
      });
      var donorsTabs = $("#donorsTab").tabs({
        cache : true
      });
      var collectionsTabs = $("#collectionsTab").tabs({
        cache : true
      });

      // Define our own click handler for the tabs, overriding the default.
      $(".tabs").find(tab_a_selector).click(function() {
        var t = getSelectedTabs();
        console.log("pushstate called " + JSON.stringify(t));
        history.pushState(t, "", "");
        return false;
      });

      var pushState = history.pushState;
      history.pushState = function() {
        console.log(arguments);
        pushState.apply(history, arguments);
      };

      $(window).bind(
          "popstate",
          function(event) {
            console.log("popstate called");
            console.log(event);
            if (event === undefined || event.originalEvent == undefined
                || event.originalEvent.state == undefined) {
              console.log("state is null");
              return;
            }
            var state = event.originalEvent.state;
            console.log(state);
            if (state == undefined)
              return;

            if (state.topPanelSelected !== undefined) {

              topPanelTabs.tabs("select", state.topPanelSelected);

              var leftPanelTabs;
              if (state.leftPanelSelected !== undefined) {
                switch (state.topPanelSelected) {
                case 1:
                  leftPanelTabs = donorsTabs;
                  break;
                case 2:
                  leftPanelTabs = collectionsTabs;
                  break;
                default:
                  break;
                }

                leftPanelTabs.tabs("select", state.leftPanelSelected);
                if (state.targetId !== undefined
                    && state.oldRequestUrl !== undefined) {
                  var data = {};
                  // there may or may not be data to send data for this
                  // request
                  // the data may already be encoded as part of the URL
                  // string
                  if (state.oldRequestData !== undefined) {
                    data = state.oldRequestData;
                  }
                  $.ajax({
                    url : state.oldRequestUrl,
                    data : data,
                    method : "GET",
                    success : function(responseData) {
                      $('#' + state.targetId).html(responseData);
                    }
                  });
                } else {
                  leftPanelTabs.tabs("load", state.leftPanelSelected);
                }

              }
            }
          });

      history.pushState({
        topPanelSelected : 0
      }, "", "")

    });