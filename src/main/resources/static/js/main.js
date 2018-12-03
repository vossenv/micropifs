"use strict";
jQuery(function () {

    refresh()

});


function refresh() {
    var strImageUrl = "http://localhost:9001/next?" + Date.now();
    var img = new Image();
    img.onload = function () {
        $("#img").attr('src', strImageUrl);
        refresh();
    };
    img.src = strImageUrl;
}
function getSingleFrame() {



    // $.ajax({
    //     url: "http://localhost:9001/next",
    //     type: 'GET',
    //     contentType: 'application/json',
    //     success: function (data) {
    //
    //         $("#img").attr('src',data);
    //         getSingleFrame()
    //     }
    //
    // });

}