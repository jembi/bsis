function getCollectionsChart(options) {

  var seriesData = parseCollectionsData(options.startTime, options.endTime,
      options.interval, options.data);
  var options = {
      chart: {
          renderTo: options.renderDest,
          zoomType: 'x',
          spacingRight: 20,
          type: 'line'
      },
      title: {
          text: 'No. of Collections by Blood Group'
      },
      xAxis: {
          title: {
              text: 'Date'
          },
          type: 'datetime'
      },
      yAxis: {
          title: {
              text: 'No. of Collections'
          }
      },
      credits: {
          enabled: false
      },
      
      plotOptions: {
      },
      series: seriesData
  };

  var chart = new Highcharts.Chart(options);

  return chart;
}

function getRequestsChart(options) {

  var seriesData = parseRequestsData(options.startTime, options.endTime,
      options.interval, options.data);
  console.log(seriesData);
  var options = {
      chart: {
          renderTo: options.renderDest,
          zoomType: 'x',
          spacingRight: 20,
          type: 'line'
      },
      title: {
          text: 'No. of Requests by Blood Group'
      },
      xAxis: {
          title: {
              text: 'Date'
          },
          type: 'datetime'
      },
      yAxis: {
          title: {
              text: 'No. of Requests'
          }
      },
      credits: {
          enabled: false
      },
      
      plotOptions: {
      },
      series: seriesData
  };

  var chart = new Highcharts.Chart(options);

  return chart;
}

function getDiscardedProductsChart(options) {

  var seriesData = parseDiscardedProductsData(options.startTime, options.endTime,
      options.interval, options.data);
  var options = {
      chart: {
          renderTo: options.renderDest,
          zoomType: 'x',
          spacingRight: 20,
          type: 'line'
      },
      title: {
          text: 'No. of Discarded Products by Blood Group'
      },
      xAxis: {
          title: {
              text: 'Date'
          },
          type: 'datetime'
      },
      yAxis: {
          title: {
              text: 'No. of Discarded Products'
          }
      },
      credits: {
          enabled: false
      },
      
      plotOptions: {
      },
      series: seriesData
  };

  var chart = new Highcharts.Chart(options);

  return chart;
}

function getIssuedProductsChart(options) {

  var seriesData = parseIssuedProductsData(options.startTime, options.endTime,
      options.interval, options.data);
  var options = {
      chart: {
          renderTo: options.renderDest,
          zoomType: 'x',
          spacingRight: 20,
          type: 'line'
      },
      title: {
          text: 'No. of Products Issued by Blood Group'
      },
      xAxis: {
          title: {
              text: 'Date'
          },
          type: 'datetime'
      },
      yAxis: {
          title: {
              text: 'No. of Products Issued'
          }
      },
      credits: {
          enabled: false
      },
      
      plotOptions: {
      },
      series: seriesData
  };

  var chart = new Highcharts.Chart(options);

  return chart;
}

function parseInventoryData(data) {

  var result = {};

  result.data = [];
  var bloodGroups = {'' : '',
                     'A+'  : 'A+',
                     'B+'  : 'B+',
                     'AB+' : 'AB+',
                     'O+'  : 'O+',
                     'A-'  : 'A-',
                     'B-'  : 'B-',
                     'AB-' : 'AB-',
                     'O-'  : 'O-'
                    };
  for (var category in data) {
    var productTypeData = data[category];
    var bloodGroupData = [];
    for (var bloodGroup in bloodGroups) {
      var inventoryByBloodGroup = productTypeData[bloodGroup];
      console.log(inventoryByBloodGroup);
      var numUnits = 0;
      for (var index in inventoryByBloodGroup) {
        numUnits = numUnits + inventoryByBloodGroup[index];
      }
      var drilldown = {
        name: category + " " + bloodGroup,
        categories: ['Less than 5 days old',
                     '5 to 9 days old',
                     '10-14 days old',
                     '15 to 19 days old',
                     '20 to 24 days old',
                     '25 to 29 days old',
                     '30 days or older'],
        data: [inventoryByBloodGroup[0],
               inventoryByBloodGroup[5],
               inventoryByBloodGroup[10],
               inventoryByBloodGroup[15],
               inventoryByBloodGroup[20],
               inventoryByBloodGroup[25],
               inventoryByBloodGroup[30]
              ]
      };
      bloodGroupData.push({y: numUnits, drilldown: drilldown});
    }
    result.data.push({name: category, data: bloodGroupData});
  }

  return result;
}

function setChart(chart, categories, data) {

  chart.xAxis[0].setCategories(categories, false);
  for (var index = chart.series.length-1; index >= 0; --index) {
    chart.series[index].remove(false);
  }

  for (var index = 0; index < data.length; index++) {
    chart.addSeries({name: data[index].name, data: data[index].data}, false);
  }
  chart.redraw();
}

function setDrilldownChart(chart, name, categories, data) {

  chart.xAxis[0].setCategories(categories, false);
  for (var index = chart.series.length-1; index >= 0; --index) {
    chart.series[index].remove(false);
  }

  chart.addSeries({
    name: name,
    data: data
  }, false);
  chart.redraw();
}

function generateInventoryChart(options) {
 
  var seriesData = parseInventoryData(options.data);

  var categories = ["Unknown", "A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"];
  var title = 'Product Inventory';
  var subtitle = options.subtitle;

  var data = seriesData.data;

  var chart = new Highcharts.Chart({
    chart: {
        renderTo: options.renderDest,
        type: 'column'
    },
    title: {
        text: title
    },
    subtitle: {
        text: subtitle,
    },
    xAxis: {
        categories: categories
    },
    yAxis: {
        min: 0,
        title: {
            text: 'Number of units of Product in Inventory'
        }
    },
    legend: {
        backgroundColor: '#FFFFFF',
        align: 'center',
        verticalAlign: 'bottom',
    },
    tooltip: {
        formatter: function() {
            return ''+
                this.x +': '+ this.y +' units';
        }
    },
    plotOptions: {
        column: {
            cursor: "pointer",
            point: {
              events: {
                click: function() {
                         var drilldown = this.drilldown;
                         if (drilldown) {
                           setDrilldownChart(chart, drilldown.name, drilldown.categories, drilldown.data);
                         }
                         else {
                           setChart(chart, categories, data);
                         }
                       }
              }
            },
            dataLabels: {
              align: 'left',
              enabled: true,
              rotation: 270,
              x: 4,
              y: -5
            },
            pointPadding: 0.2,
            borderWidth: 0
        }
    },
    series: data
  });
  return chart;
}

function getTestResultsChart(options) {

  var seriesData = parseTestResultsData(options.startTime, options.endTime,
      options.interval, options.data);

  var options = {
      chart: {
          renderTo: options.renderDest,
          zoomType: 'x',
          spacingRight: 20,
          type: 'line'
      },
      title: {
          text: 'Positive Test Results for different Disease Markers over time'
      },
      xAxis: {
          title: {
              text: 'Date'
          },
          type: 'datetime'
      },
      yAxis: {
          title: {
              text: 'Number of Test Results Found Positive'
          }
      },
      credits: {
          enabled: false
      },
      
      plotOptions: {
      },
      series: seriesData
  };

  var chart = new Highcharts.Chart(options);

  return chart;
}

function parseTestResultsData(beginDate, endDate, interval, data) {

  var resultData = [];
  for (var testName in data) {
    var resultsForTest = {}
    resultsForTest.name = testName;
    resultsForTest.data = [];
    for (var x in data[testName]) {
      resultsForTest.data.push([ parseInt(x), data[testName][x] ]);
    }
    resultsForTest.data.sort(function(a, b) {
      return a[0] - b[0];
    });
    resultData.push(resultsForTest);
  }

  return resultData;
}

function parseCollectionsData(beginDate, endDate, interval, data) {

  var resultData = [];
  for (var bloodGroup in data) {
    var resultsForBloodGroup = {}
    resultsForBloodGroup.name = bloodGroup;
    resultsForBloodGroup.data = [];
    for (var x in data[bloodGroup]) {
      resultsForBloodGroup.data.push([ parseInt(x), data[bloodGroup][x] ]);
    }
    resultsForBloodGroup.data.sort(function(a, b) {
      return a[0] - b[0];
    });
    resultData.push(resultsForBloodGroup);
  }

  return resultData;
}

function parseRequestsData(beginDate, endDate, interval, data) {

  console.log(data);
  var resultData = [];
  for (var bloodGroup in data) {
    var resultsForBloodGroup = {}
    resultsForBloodGroup.name = bloodGroup;
    resultsForBloodGroup.data = [];
    for (var x in data[bloodGroup]) {
      resultsForBloodGroup.data.push([ parseInt(x), data[bloodGroup][x] ]);
    }
    resultsForBloodGroup.data.sort(function(a, b) {
      return a[0] - b[0];
    });
    resultData.push(resultsForBloodGroup);
  }

  return resultData;
}

function parseDiscardedProductsData(beginDate, endDate, interval, data) {

  var resultData = [];
  for (var bloodGroup in data) {
    var resultsForBloodGroup = {}
    resultsForBloodGroup.name = bloodGroup;
    resultsForBloodGroup.data = [];
    for (var x in data[bloodGroup]) {
      resultsForBloodGroup.data.push([ parseInt(x), data[bloodGroup][x] ]);
    }
    resultsForBloodGroup.data.sort(function(a, b) {
      return a[0] - b[0];
    });
    resultData.push(resultsForBloodGroup);
  }

  return resultData;
}

function parseIssuedProductsData(beginDate, endDate, interval, data) {

  var resultData = [];
  for (var bloodGroup in data) {
    var resultsForBloodGroup = {}
    resultsForBloodGroup.name = bloodGroup;
    resultsForBloodGroup.data = [];
    for (var x in data[bloodGroup]) {
      resultsForBloodGroup.data.push([ parseInt(x), data[bloodGroup][x] ]);
    }
    resultsForBloodGroup.data.sort(function(a, b) {
      return a[0] - b[0];
    });
    resultData.push(resultsForBloodGroup);
  }

  return resultData;
}
