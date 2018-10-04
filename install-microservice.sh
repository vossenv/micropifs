#!/usr/bin/env bash

# username="pi"
# host="192.168.50.80"
# dir="/home/vagrant/.micropifs"
# source="/home/Pi/Desktop"

while [[ $# -gt 0 ]]
do
key=$1
case $key in
    -h)
        host=$2
        shift # past argumentls
        shift # past value
        ;;
    -u)
        username=$2
        shift # past argument
        shift # past value
        ;;
    -d)
        dir=$2
        shift # past argument
        shift # past value
        ;;
    -s)
        source=$2
        shift # past argument
        shift # past value
        ;;        
    -full)
        install=true
        shift # past argument
        shift # past value
        ;;
esac
done


echo ""
echo "Initiating install with the parameters.."
echo ""
echo "Username = $username"
echo "Host = $host"
echo "Target = $dir"
echo "Source = $source"

if [ ! -d $dir ]; then
    echo "Creating directory $dir"
    sudo mkdir $dir
fi


echo ""

if [[ $install == true ]]; then


    echo "Installing the micropifs service..."

    sudo mv micropifs.jar $dir
    sudo tee /etc/systemd/system/micropifs.service <<-EOF > /dev/null
#!/usr/bin/env bash
[Unit]
Description=Micro Service for Pi Images
[Service]
User=root

WorkingDirectory=$dir
ExecStart=/bin/bash $dir/startup.sh
SuccessExitStatus=143
TimeoutStopSec=10

#Restart=on-failure
#RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

sudo tee $dir/startup.sh <<-EOF > /dev/null
#!/usr/bin/env bash
sudo /usr/bin/java -jar micropifs.jar --local.resource.path="$source"
EOF

    sudo systemctl daemon-reload
    sudo systemctl enable micropifs.service
    echo ""
fi

echo "----------------------------"
[ ! -d /etc/systemd/system/micropifs.service ] && echo "Service verified" || echo "Service failed to install"
[ ! -d $dir/startup.sh ] && echo "Startup file verified" || echo "Startup file failed to install"
[ ! -d $dir/micropifs.jar ] && echo "Application verified" || echo "Application failed to install"
echo "----------------------------"
echo "Attempting to start service"

sudo service micropifs restart
sleep 5
state=$(systemctl show -p ActiveState micropifs | sed 's/ActiveState=//g')

echo "Micro Pi FS is currently $state"
echo "Testing status endpoint... "

status=$(curl localhost:9001/status -s)

[[ $status -eq "true" ]] && echo "Api is running, install complete!" || "Api not running, attention needed"


