#!/usr/bin/env bash

username="pi"
dir="/home/pi/.micropifs"
source="/home/pi/Desktop"

hostlist=()

hostlist+=("192.168.50.80")             # Front
#hostlist+=("192.168.50.79")            # Garage
#hostlist+=("192.168.50.230")           # Rear IR

# Windows
# hostlist+=("192.168.50.66")


for host in "${hostlist[@]}"
do
    scp ./install-microservice.sh $username@$host:/home/$username/install-microservice.sh
    scp build/libs/micropifs.jar $username@$host:/home/$username/micropifs.jar
    ssh $username@$host sudo sh /home/$username/install-microservice.sh -h $host -u $username -d $dir -s $source
    ssh $username@$host sudo rm /home/$username/install-microservice.sh
done



