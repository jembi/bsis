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

      $(".leftPanel").on("tabsbeforeActivate",
                           function(event, ui) {
                             console.log("here");
                             console.log(ui.tab);
                           });
      
      var tabContent = {};

      function loadTabContent(state) {
        if (state.targetId !== undefined
            && state.oldRequestUrl !== undefined) {
          var data = {};
          // there may or may not be data to send data for this request
          // the data is already encoded as part of the URL string
          if (state.oldRequestData !== undefined) {
            data = state.oldRequestData;
          }
          //showLoadingImage(state.targetId);
          $.ajax({
            url : state.oldRequestUrl,
            data : data,
            method : "GET",
            success : function(responseData) {
              console.log(state.targetId);
              $('#' + state.targetId).html(responseData);
              console.log("loading state");
              console.log(state);
              createBreadCrumb(state);
            }
          });
        } else {
          leftPanelTabs.tabs("load", state.leftPanelSelected);
        }
      }

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

      $(document).bind("addTabContentInitial", function(event, state) {
        console.log("here");
        var topId = state.topPanelSelected;
        var leftId = state.leftPanelSelected;
        if (tabContent[topId] == undefined)
          tabContent[topId] = {};
        console.log(tabContent[topId]);
        if (tabContent[topId][leftId].length == 0) {
          tabContent[topId][leftId] = [];
          tabContent[topId][leftId].push(state);
        }
        console.log(tabContent[topId][leftId]);
        console.log(tabContent[topId][leftId].length);
      });

      $(document).bind("addTabContent", function(event, state) {
        var topId = state.topPanelSelected;
        var leftId = state.leftPanelSelected;
        var content = tabContent[topId][leftId];
        content.push(state);
        createBreadCrumb(state);
      });

      function createBreadCrumb(state, linkDepth) {
        var topId = state.topPanelSelected;
        var leftId = state.leftPanelSelected;
        var breadCrumb = getBreadCrumb(state);
        var breadCrumbElement = $("#" + state.leftPanelSelectedId).find(".breadCrumb");
        breadCrumbElement.html(breadCrumb);
        var breadCrumbLinks = breadCrumbElement.find(".breadCrumbText");
        var numLinks = breadCrumbLinks.length;
        for (var i = 0; i < numLinks-1; i++){
          // storing the value in depth is important because of closure
          var depth = i+1;
          $(breadCrumbLinks[i]).click(
              function() {
                showBreadCrumb(topId, leftId, depth);
              });
        }
      }

      function showBreadCrumb(topId, leftId, depth) {
        var content = tabContent[topId][leftId];
        content.splice(depth, content.length-depth);
        loadTabContent(content[depth-1]);
        createBreadCrumb(content[depth-1]);
      }

      function getBreadCrumb(state) {
        var breadCrumbText = [];
        var topId = state.topPanelSelected;
        var leftId = state.leftPanelSelected;
        var content = tabContent[topId][leftId];
        console.log(content);
        if (content.length == 0)
          return;
        console.log("length: " + content.length);
        console.log("tabDepth: " + state.tabDepth);
        var numLinks = content.length;
        for (var i = 0; i < numLinks; ++i) {
          console.log("contentLabel: " + content[i].contentLabel);
          breadCrumbText.push('<span class="breadCrumbText">' + content[i].contentLabel + '</span>');
        }
        console.log(breadCrumbText);
        return "<div>" + breadCrumbText.join(" > ") + "</div>";
      }
      
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
                loadTabContent(state);
              }
            }
          });

      // for initial page load
      history.pushState({topPanelSelected : 0}, "", "")
    });