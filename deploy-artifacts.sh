#!/usr/bin/env bash

username="pi"
host="192.168.50.80"

dir="/home/pi/.micropifs"
source="/home/pi/Desktop"

scp ./install-microservice.sh $username@$host:/home/$username/install-microservice.sh
scp build/libs/micropifs.jar $username@$host:/home/$username/micropifs.jar
ssh $username@$host sudo /home/$username/install-microservice.sh -h $host -u $username -d $dir -s $source
ssh $username@$host sudo rm /home/$username/install-microservice.sh

# ssh $username@$host sudo service micropifs restart
