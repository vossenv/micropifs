#!/usr/bin/env bash

username="pi"
target="192.168.50.80"

while [[ $# -gt 0 ]]
do
key=$1
case $key in
    -t)
        target=$2
        shift # past argument
        shift # past value
        ;;
    -u)
        username=$2
        shift # past argument
        shift # past value
        ;;
esac
done

ssh ${username}@${target} bash -c "'
sudo mkdir /home/pi/micropifs $1 &> /dev/null
sudo wget https://raw.githubusercontent.com/janssenda/microservice-picam/master/micropifs.service -O /etc/systemd/system/micropifs.service
sudo wget https://raw.githubusercontent.com/janssenda/microservice-picam/master/startup.sh -O /home/pi/micropifs/startup.sh
sudo systemctl daemon-reload
sudo systemctl enable micropifs.service
'"