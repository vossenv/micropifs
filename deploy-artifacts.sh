#!/usr/bin/env bash


ipaddresses=(
    "192.168.50.59"
    "192.168.50.230"
    "192.168.50.227"
)

username="pi"
dir="/home/pi/.micropifs"
source="/home/pi/Desktop"

while [[ $# -gt 0 ]]
do
key=$1
case $key in
   -h)
       ipaddresses=($2)
       shift # past argumentls
       shift # past value
       ;;
esac
done

function printColor(){

    case $2 in
        "black") col=0;;
          "red") col=1;;
        "green") col=2;;
       "yellow") col=3;;
         "blue") col=4;;
      "magenta") col=5;;
         "cyan") col=6;;
        "white") col=7;;
              *) col=7;;
    esac

    printf "$1\n"
}

function deployOnLinuxTarget() {


    printColor "\nDeploying on $1... (Raspbian)\n" "green"

    scp ./install-microservice.sh $username@$1:/home/$username/install-microservice.sh
    scp build/libs/micropifs.jar $username@$1:/home/$username/micropifs.jar
    ssh $username@$1 sudo bash /home/$username/install-microservice.sh -h $1 -d $dir -s $source
    ssh $username@$1 sudo rm /home/$username/install-microservice.sh

    printColor "\n---------- Deploy on $1 completed ----------\n" "blue"
}

function deployOnWinTarget {

    printColor "\nDeploying on $1... (Windows)\n" "green"

    scp build/libs/micropifs.jar jenkins@$1:"C:/Program\\ Files\\ (x86)/micropifs/micropifs.jar"
    timeout 10 ssh jenkins@$1 Restart-Service -Name micropifs

    printColor "\n---------- Deploy on $1 completed ----------\n" "blue"
}

for i in ${ipaddresses[@]}; do
    #deployOnLinuxTarget $i && printf "" || echo "Deploy to $i failed... continuing"
done

deployOnWinTarget "192.168.50.66" && printf "" || echo "Deploy to $i failed... continuing"
