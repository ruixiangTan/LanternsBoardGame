/**
 * Created by Musers on 11/5/2015.
 */
var data;
var ctx;
var myPieChart;

$.getJSON("/Expense", function (result) {

    data = result;
    ctx = document.getElementById("myChart").getContext("2d");
    myPieChart = new Chart(ctx).Pie(data);

});