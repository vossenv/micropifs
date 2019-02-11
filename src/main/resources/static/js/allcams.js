"use strict";
jQuery(function () {

    $("#date").text(moment().format("LLL"));
    buildCamList();
    setDate();

});

function setDate(){
    $("#date").text(moment().format("LLL"));
    setTimeout(function(){
        setDate();
    }, 30*1000);
}

function buildCamList(){

    $.ajax({url: "/camlist", success: function(camList){
            $.each(camList, function (key, camId) {
                var singleCam = "<img class='camImg' id='" + camId +"' />";
                $('#camDiv').append(singleCam);
            });

            $.each(camList, function (key, camId ) { updateSingleCamera(camId) });
    }});
}

function updateSingleCamera(camId) {
    $("#"+camId).attr('src', "/cameras/" + camId + "/next?" + Date.now());
    setTimeout(function(){
        updateSingleCamera(camId)
    }, 250);
}
