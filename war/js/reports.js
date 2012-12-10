function getTimeChart(options) {

  var seriesData = getSeriesData(options.startTime, options.endTime,
      options.interval, options.data);

  var chart = new Highcharts.Chart(
      {
        chart : {
          renderTo : options.renderDest,
          zoomType : 'x',
          spacingRight : 20
        },
        title : {
          text : options.title
        },
        subtitle : {
          text : document.ontouchstart === undefined ? 'Click and drag in the plot area to zoom in'
              : 'Drag your finger over the plot to zoom in'
        },
        xAxis : {
          type : "datetime",
          maxZoom : 7 * 24 * 3600000, // seven days
          title : {
            text : null
          }
        },
        yAxis : {
          title : {
            text : options.yAxisTitle
          },
          startOnTick : false,
          showFirstLabel : false
        },
        tooltip : {
          shared : true
        },
        legend : {
          enabled : false
        },
        plotOptions : {
          area : {
            fillColor : {
              linearGradient : {
                x1 : 0,
                y1 : 0,
                x2 : 0,
                y2 : 1
              },
              stops : [ [ 0, Highcharts.getOptions().colors[0] ],
                  [ 1, 'rgba(2,0,0,0)' ] ]
            },
            lineWidth : 1,
            marker : {
              enabled : false,
              states : {
                hover : {
                  enabled : true,
                  radius : 5
                }
              }
            },
            shadow : false,
            states : {
              hover : {
                lineWidth : 1
              }
            }
          }
        },
        series : [ {
          type : 'line',
          name : options.hoverText,
          pointStart : options.startTime,
          data : seriesData
        } ]
      });

  return chart;
}

function getSeriesData(beginDate, endDate, interval, data) {

  seriesData = [];
  console.log(data);
  for (var x in data) {
    seriesData.push([ parseInt(x), data[x] ]);
  }

  seriesData.sort(function(a, b) {
    return a[0] - b[0];
  });
  return seriesData;
}

function parseInventoryData(data) {

  var result = {};
  result.categories = [];
  result.data = {};

  result.data = [];
  var bloodGroups = {'A+'  : 'A+',
                     'B+'  : 'B+',
                     'AB+' : 'AB+',
                     'O+'  : 'O+',
                     'A-'  : 'A-',
                     'B-'  : 'B-',
                     'AB-' : 'AB-',
                     'O-'  : 'O-'
                    };
  for (var category in data) {
    result.categories.push(category);
    var productTypeData = data[category];
    var bloodGroupData = []
    for (var bloodGroup in bloodGroups) {
      console.log(bloodGroup);
      var inventoryByBloodGroup = productTypeData[bloodGroup];
      var numUnits = 0;
      console.log(inventoryByBloodGroup);
      for (var index in inventoryByBloodGroup) {
        numUnits = numUnits + inventoryByBloodGroup[index];
      }
      bloodGroupData.push(numUnits);
    }
    result.data.push({name: category, data: bloodGroupData});
  }

  console.log(result.data);
  return result;
}

function generateInventoryChart(options) {
 
  var seriesData = parseInventoryData(options.data);

  var chart = new Highcharts.Chart({
    chart: {
        renderTo: options.renderDest,
        type: 'column'
    },
    title: {
        text: 'Product Inventory'
    },
    subtitle: {
        text: 'Number of units of Available Products by Product Type and Blood Group'
    },
    xAxis: {
        categories: ["A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"]
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
    series: seriesData.data
  });
  return chart;
}