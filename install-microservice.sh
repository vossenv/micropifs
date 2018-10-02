#!/usr/bin/env bash

username="pi"
host="192.168.50.80"
dir="/home/pi/.micropifs"

while [[ $# -gt 0 ]]
do
key=$1
case $key in
    -h)
        host=$2
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

ssh ${username}@${host} bash -c "'
sudo mkdir $dir $1 &> /dev/null
sudo wget https://raw.githubusercontent.com/janssenda/microservice-picam/master/micropifs.service -O /etc/systemd/system/micropifs.service
sudo wget https://raw.githubusercontent.com/janssenda/microservice-picam/master/startup.sh -O $dir/startup.sh
sudo systemctl daemon-reload
sudo systemctl enable micropifs.service
'"

ssh $username@$host sudo service micropifs restart
ssh $username@$host sudo service micropifs status
