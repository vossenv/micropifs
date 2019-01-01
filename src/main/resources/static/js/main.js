"use strict";
jQuery(function () {

    var url = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    var params = {};
    $(url).each( function( index, value ) {
        var p = value.split('=');
        params[p[0].toLowerCase()] = p[1].toLowerCase();
    });

    $("#img").attr('src', "http://localhost:9001/cameras/" + params['camid'] + "/next");

    update(params);

});

function update(p) {

    var strImageUrl = "/cameras/" + p['camid'] + "/next?" + Date.now();
    var img = new Image();
    img.onload = function () {
        $("#img").attr('src', strImageUrl);
        update(p);
    };
    img.src = strImageUrl;
}
