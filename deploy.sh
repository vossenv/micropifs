#!/usr/bin/env bash

ipaddresses=(
#    "192.168.50.59:linux:pi"
#    "192.168.50.230:linux:pi"
#    "192.168.50.227:linux:pi"
#    "192.168.50.78:linux:pi"
#    "192.168.50.66:windows:jenkins"
    "192.168.50.139:windows:jenkins"
#    "192.168.50.187:linux:self"
#    "192.168.50.187:linux:carag"
)

./gradlew clean assemble

service_name="micropifs"

function deployBash() {
    if [ $2 == "self" ]; then

        printf "\nDeploying to self... \n"
        local script="sudo /usr/bin/java -jar micropifs.jar"
        local dir="/home/carag/.$service_name"

        sudo rm -rf $dir; echo "Creating directory $dir";  sudo mkdir $dir; sudo chmod 777 $dir;
        sudo sudo chmod +x install-microservice-bash.sh
        sudo cp build/libs/micropifs.jar $dir
        sudo ./install-microservice-bash.sh "$dir" "$script" "$service_name"
    else
        local script="sudo\ /usr/bin/java\ -jar\ micropifs.jar"
        local dir="/home/$2/.$service_name"
        ssh $2@$1 "sudo rm -rf $dir; echo "Creating directory $dir";  sudo mkdir $dir; sudo chmod 777 $dir;"
        scp build/libs/micropifs.jar $2@$1:$dir
        ssh $2@$1 'bash -s' < ./install-microservice-bash.sh "$dir" "$script" "$service_name"
    fi
}

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

function removeBash(){
    local dir="/home/$2/.$service_name"
    ssh $2@$1 "echo "Stopping service $service_name";  sudo service $service_name stop; sudo systemctl disable $service_name.service"
    ssh $2@$1 "echo "Removing directory $dir";  sudo rm -rf $dir;"
    ssh $2@$1 "echo "Removing configuration";  sudo rm /etc/systemd/system/$service_name.service;"
    ssh $2@$1 "echo "Reload services";  sudo systemctl daemon-reload;"
    printf "\nFinished removal of $service_name\n"
}

function deployWin() {
    printf "Stopping service.... \n"
    timeout 5 ssh $2@$1 Stop-Service -Name Micropifs
    printf "\nBegin file transfer... \n"
    scp build/libs/micropifs.jar $2@$1:"C:/Program\\ Files\\ (x86)/Micropifs/micropifs.jar"
    printf "\nStarting service.... \n"
    timeout 5 ssh $2@$1 Start-Service -Name Micropifs
    check_remote $1
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
        #removeBash $host $username
    fi
    printf "===== End deploy: $platform / $username / $host =====\n"
done


