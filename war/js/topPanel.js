$(document).ready(
    function() {

      // This selector will be reused when selecting actual tab widget
      // elements.
      var tab_a_selector = 'ul.ui-tabs-nav a';

      var pushState = history.pushState;
      history.pushState = function() {
        console.log("pushState called");
        pushState.apply(history, arguments);
      };

      var topPanelTabs = $("#topPanelTabs").tabs({
        cache : true
      });

      var donorsTabs = $("#donorsTab").tabs({
        cache : true,
        // select event is deprecated in jquery 1.9. Use it for now.
//        select: function(event, ui) {
//                  var tabElement = $(ui.tab)[0];
//                  showLoadingImage(tabElement.hash.substr(1));
//                }
      });
      var collectionsTabs = $("#collectionsTab").tabs({
        cache : true
      });
      var testResultsTabs = $("#testResultsTab").tabs({
        cache : true
      });
      var productsTabs = $("#productsTab").tabs({
        cache : true
      });
      var requestsTabs = $("#requestsTab").tabs({
        cache : true
      });
      var usageTabs = $("#usageTab").tabs({
        cache : true
      });
      var reportsTabs = $("#reportsTab").tabs({
        cache : true
      });
      var adminTabs = $("#adminTab").tabs({
        cache : true
      });

      var tabContent = {};

      // Define our own click handler for the tabs, overriding the default.
      $(".tabs").find(tab_a_selector).click(function(event, ui) {
        var t = getSelectedTabs();
        console.log("pushstate called " + JSON.stringify(t));
        if (tabContent[t.topPanelSelected] == undefined)
          tabContent[t.topPanelSelected] = {};
        if (tabContent[t.topPanelSelected][t.leftPanelSelected] == undefined)
          tabContent[t.topPanelSelected][t.leftPanelSelected] = [];
        history.pushState(t, "", "");
        return false;
      });

      $(window).bind(
          "popstate",
          function(event) {
            if (event === undefined || event.originalEvent == undefined
                || event.originalEvent.state == undefined) {
              console.log("state is null");
              return;
            }
            var state = event.originalEvent.state;
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
                case 3:
                  leftPanelTabs = testResultsTabs;
                  break;
                case 4:
                  leftPanelTabs = productsTabs;
                  break;
                case 5:
                  leftPanelTabs = requestsTabs;
                  break;
                case 6:
                  leftPanelTabs = usageTabs;
                  break;
                case 7:
                  leftPanelTabs = reportsTabs;
                  break;
                case 7:
                  leftPanelTabs = adminTabs;
                  break;
                default:
                  break;
                }

                leftPanelTabs.tabs("select", state.leftPanelSelected);
              }
            }
          });

      // for initial page load
      history.pushState({topPanelSelected : 0}, "", "")
    });