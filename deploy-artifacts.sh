#!/usr/bin/env bash

while [[ $# -gt 0 ]]
do
key=$1
case $key in
    -h)
        host=$2
        shift # past argumentls
        shift # past value
        ;;
    -full)
        full="-full"
        shift # past argument
        shift # past value
        ;;
esac
done


username="pi"
dir="/home/pi/.micropifs"
source="/home/pi/Desktop"

scp ./install-microservice.sh $username@$host:/home/$username/install-microservice.sh
scp build/libs/micropifs.jar $username@$host:/home/$username/micropifs.jar
ssh $username@$host sudo bash /home/$username/install-microservice.sh -h $host -u $username -d $dir -s $source $full
ssh $username@$host sudo rm /home/$username/install-microservice.sh
