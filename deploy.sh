#!/usr/bin/env bash

./gradlew clean assemble

service_name="micropifs"

function check_remote(){
    echo -e "Checking API status remotely... \n"
    apiStatus="down"
    count=0
    while [ "$apiStatus" != "true" ]; do
        if [ "$count" -gt 50 ]; then break; fi
        apiStatus=$(curl http://$1:9001/status -s)
        count=$((count + 1))
        sleep 1
        echo -n "$count "
    done
    echo -e "\n"
    [ "$apiStatus" = "true" ] && echo "Api is running, install complete!" || echo "Service not running, attention needed"
}


function deployWin() {
    printf "Stopping service.... \n"
    timeout 5 ssh $2@$1 Stop-Service -Name Micropifs
    printf "\nBegin file transfer... \n"
    scp build/libs/micropifs.jar $2@$1:"C:/Program\\ Files/Micropifs/micropifs.jar"
    printf "\nStarting service.... \n"
    timeout 5 ssh $2@$1 Start-Service -Name micropifs
    check_remote $1
}

deployWin 192.168.50.139 carag

