/* globals Chart:false, feather:false */

(function () {
  'use strict';
  feather.replace({ 'aria-hidden': 'true' });

  // Graphs
  var ctx = document.getElementById('myChart');

  if (ctx == null) return;
  // eslint-disable-next-line no-unused-vars
  var myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: [
        'Sunday',
        'Monday',
        'Tuesday',
        'Wednesday',
        'Thursday',
        'Friday',
        'Saturday'
      ],
      datasets: [{
        data: [
          15339,
          21345,
          18483,
          24003,
          23489,
          24092,
          12034
        ],
        lineTension: 0.2,
        backgroundColor: 'rgba(226, 109, 56, 0.08)',
        borderColor: '#E26D38',
        borderWidth: 3,
        pointBackgroundColor: '#E26D38'
      }]
    },
    options: {
      scales: {
        yAxes: [{
          ticks: {
            beginAtZero: false
          }
        }]
      },
      legend: {
        display: false
      }
    }
  });
})();
