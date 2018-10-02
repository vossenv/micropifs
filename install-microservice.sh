#!/usr/bin/env bash

username="pi"
target="192.168.50.80"

ssh $username@$target sudo mkdir /home/pi/micropifs $1 &> /dev/null
ssh $username@$target sudo wget https://github.com/janssenda/microservice-picam/blob/master/micropifs.service -O /etc/systemd/system/micropifs.service
ssh $username@$target sudo wget https://github.com/janssenda/microservice-picam/blob/master/startup.sh -O /home/pi/micropifs/startup.sh
ssh $username@$target sudo systemctl daemon-reload
ssh $username@$target sudo systemctl enable micropifs.service


#ssh $username@$target bash -c "'
#sudo mkdir /home/pi/micropifs $1 &> /dev/null
#sudo wget https://github.com/janssenda/microservice-picam/blob/master/micropifs.service -O /etc/systemd/system/micropifs.service
#sudo wget https://github.com/janssenda/microservice-picam/blob/master/startup.sh -O /home/pi/micropifs/startup.sh
#sudo systemctl daemon-reload
#sudo systemctl enable micropifs.service
