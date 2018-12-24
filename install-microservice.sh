#!/usr/bin/env bash


install="false"
while [[ $# -gt 0 ]]
do
key=$1
case $key in
    -h)
        host=$2
        shift # past argumentls
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
esac
done

echo ""
echo "Initiating install with the parameters.."
echo ""
echo "Host = $host"
echo "Target = $dir"
echo "Source = $source"

sudo rm -rf $dir
echo "Creating directory $dir"
sudo mkdir $dir

echo ""
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


echo "----------------------------"
[ ! -d /etc/systemd/system/micropifs.service ] && echo "Service verified" || echo "Service failed to install"
[ ! -d $dir/startup.sh ] && echo "Startup file verified" || echo "Startup file failed to install"
[ ! -d $dir/micropifs.jar ] && echo "Application verified" || echo "Application failed to install"
echo "----------------------------"
echo ""
echo "Attempting to start service"
sudo service micropifs stop
sudo service micropifs start

echo ""

apiStatus="down"
count=0


while [ "$apiStatus" != "true" ]; do

    if [ "$count" -gt 50 ]; then break; fi

    apiStatus=$(curl localhost:9001/status -s)    
    count=$((count + 1))

    sleep 1
    echo -n "$count "
done

state=$(systemctl show -p ActiveState micropifs | sed 's/ActiveState=//g')

echo ""; echo ""
echo "Micro Pi FS is currently $state"

[ "$apiStatus" = "true" ] && echo "Api is running, install complete!" || echo "Api not running, attention needed"


