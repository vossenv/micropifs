#!/usr/bin/env bash

ipaddresses=(
    "192.168.50.59:linux:pi"
    "192.168.50.230:linux:pi"
    "192.168.50.227:linux:pi"
    "192.168.50.78:linux:pi"
    "192.168.50.66:windows:jenkins"
    "192.168.50.139:windows:jenkins"
)

./gradlew clean assemble

service_name="micropifs"

function deployBash() {

    local dir="/home/pi/.$service_name"
    local script="sudo\ /usr/bin/java\ -jar\ micropifs.jar"
    ssh $2@$1 "sudo rm -rf $dir; echo "Creating directory $dir";  sudo mkdir $dir; sudo chmod 777 $dir;"
    scp build/libs/micropifs.jar $2@$1:$dir
    ssh $2@$1 'bash -s' < ./install-microservice-bash.sh "$dir" "$script" "$service_name"
}

function deployWin() {

    printf "Stopping service.... \n"
    timeout 5 ssh $2@$1 Stop-Service -Name Micropifs

    printf "\nBegin file transfer... \n"
    scp build/libs/micropifs.jar $2@$1:"C:/Program\\ Files\\ (x86)/Micropifs/micropifs.jar"

    printf "\nStarting service.... \n"

    timeout 10 ssh $2@$1 Start-Service -Name Micropifs
}

for i in ${ipaddresses[@]}; do

    IFS=':' tokens=(${i});

    host=${tokens[0]}
    platform=${tokens[1]}
    username=${tokens[2]}
    
    printf "\n===== Begin deploy: $platform / $username / $host =====\n\n"
    
    if [ "$platform" == "windows" ]; then 
        deployWin $host $username
    else 
        deployBash $host $username
    fi

    printf "===== End deploy: $platform / $username / $host =====\n"

done


