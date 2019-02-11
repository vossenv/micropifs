/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {

    $("#inputError").hide();
    var units = "imperial";
    var zipCode = "55407";
    var apiKey = "d446ade3c095f0739178c40ed40583be";

    getCurrentWeather(zipCode, apiKey, units);


});

function getCurrentWeather(zipCode, apiKey, units) {

    var callA = "https://api.openweathermap.org/data/2.5/weather?zip=";
    var callB = zipCode + ",us&units=" + units + "&APPID=" + apiKey;
    var call = callA + callB;

    $.ajax({
        type: "GET",
        url: call,
        success: function (weatherData) {
            var city = weatherData.name;
            var temp = weatherData.main.temp;
            var pressure = (weatherData.main.pressure * 100) / 1000;
            var humidity = weatherData.main.humidity;
            var temp_min = weatherData.main.temp_min;
            var temp_max = weatherData.main.temp_max;

            var type = weatherData.weather[0].main;
            var desc = weatherData.weather[0].description;
            var iconCode = weatherData.weather[0].icon;

            var windSpd = weatherData.wind.speed * 2.23694;
            var windDir = weatherData.wind.deg;

            var iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";
            var imgCode = "<img id='wicon' src='" + iconUrl + "'>";

            var z = windDir;
            switch (true) {
                case z === 0 || z === 360: windDir = "N"; break;
                case z === 90: windDir = "W"; break;
                case z === 180: windDir = "S"; break;
                case z === 270: windDir = "E"; break;
                case 0 < z && z < 90: windDir = "NW"; break;
                case 90 < z && z < 180: windDir = "SW"; break;
                case 180 < z && z < 270: windDir = "SE"; break;
                case 270 < z && z < 360: windDir = "NE"; break;
            }

            var unitLabel = "F";
            switch (units){
                case "metric": unitLabel = "C"; break;
            }

            var current = "";
            current += "Temperature: <span class='data'>" + Number(temp).toFixed(0) + " &deg;"+unitLabel + "</span><br>";
            current += "Forecast: <span class='data'>" + Number(temp_min).toFixed(0) + " to " + Number(temp_max).toFixed(0)+ " &deg;"+unitLabel + "</span><br>";
            current += "Humidity: <span class='data'>" + Number(humidity).toFixed(0) + " %" + "</span><br>";
            current += "Wind: <span class='data'>" + Number(windSpd).toFixed(0) + " mph " + windDir + "</span><br>";

            $("#banner-pic").html(imgCode + "<br/>" + type + "<br/>" +desc);
            $("#banner-data").html(current);

            setTimeout(function(){
                getCurrentWeather(zipCode, apiKey, units);
            }, 900000); // 15 minutes
        },
        error: function () {
            setTimeout(function(){
                getCurrentWeather(zipCode, apiKey, units);
            }, 900000); // 15 minutes
        }


    });


}

