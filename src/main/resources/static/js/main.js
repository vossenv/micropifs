"use strict";
jQuery(function () {

    var url = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');

    var camID;

    for (var k in url) {

        console.log(k);

        console.log("");

    }

    // for (var i = 0; i < url.length; i++) {
    //     var urlparam = url[i].split('=');
    //     if (urlparam[0].toLowerCase() !== 'camid') {
    //         continue;
    //     }
    //     camID = urlparam[1];
    // }
    // update()

});

function update() {
    var strImageUrl = "/next?" + Date.now();

    var img = new Image();
    img.onload = function () {
        $("#img").attr('src', strImageUrl);
        update();
    };
    img.src = strImageUrl;
}
