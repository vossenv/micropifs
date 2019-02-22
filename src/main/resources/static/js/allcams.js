"use strict";


// class Camera {
//     constructor(camId) {
//         this.camId = camId;
//     }
//
//     run() {
//         function fn(camId) {
//             setTimeout(function () {
//                 console.log("Update called from " + this.camId);
//                 fn(camId)
//             }, 250);
//         }
//         fn(this.camId)
//     };
// }


// function buildCamList_2() {
//
//
//     $.ajax({
//         url: "/camlist", success: function (camList) {
//
//             var dict = {};
//             $.each(camList, function (key, camId) {
//
//                 var singleCam = "<img class='camImg' id='" + camId + "' />";
//                 $('#camDiv').append(singleCam);
//
//             });
//
//             $.each(camList, function (key, camId) {
//                 updateSingleCamera(camId)
//             });
//         }
//     });
// }

jQuery(function () {

    $("#date").text(moment().format("LLL"));
    buildCamList();
    setDate();

    // let c = new Camera("abc");
    //
    // c.run();

});

function setDate() {
    $("#date").text(moment().format("LLL"));
    setTimeout(function () {
        setDate();
    }, 30 * 1000);
}

function buildCamList() {

    $.ajax({
        url: "/camlist", success: function (camList) {
            $.each(camList, function (key, camId) {
                var singleCam = "<img class='camImg' id='" + camId + "' />";
                $('#camDiv').append(singleCam);
            });

            $.each(camList, function (key, camId) {
                updateSingleCamera(camId)
            });
        }
    });
}

function updateSingleCamera(camId) {
    $("#" + camId).attr('src', "/cameras/" + camId + "/next?" + Date.now());
    setTimeout(function () {
        updateSingleCamera(camId)
    }, 250);
}

// function fn(camId) {
//     setTimeout(function () {
//
//         console.log("Update called from " + this.camId);
//         fn(camId)
//     }, 250);
// }
