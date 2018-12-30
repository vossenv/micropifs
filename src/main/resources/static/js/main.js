"use strict";
jQuery(function () {

    refresh2()

});


// function refresh() {
//     var strImageUrl = "http://localhost:9001/frame?" + Date.now();
//     var img = new Image();
//     img.onload = function () {
//         $("#img").attr('src', strImageUrl);
//         refresh();
//     };
//     img.src = strImageUrl;
// }

function refresh2() {
    var strImageUrl = "/next?" + Date.now();

    var img = new Image();
    img.onload = function () {
        $("#img").attr('src', strImageUrl);
        refresh2();
    };
    img.src = strImageUrl;
}
// function refresh() {
//
//     $.ajax({
//         url: "/frame",
//         type: 'GET',
//         contentType: 'application/json',
//         success: function (data, status, xhr) {
//             // console.log(xhr.getResponseHeader("Percent-Motion"));
//             $("#info").html(xhr.getResponseHeader("Percent-Motion"));
//             $("#img").attr('src',"/frame?" + Date.now());
//             refresh()
//         }
//
//     });
//
// }