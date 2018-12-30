
dir=$1
script=$2
name=$3

echo ""
echo "Initiating install with the parameters:"
echo " - Directory = $dir"
echo ""

echo "Installing the $name service..."

sudo tee /etc/systemd/system/$name.service <<-EOF > /dev/null
#!/usr/bin/env bash
[Unit]
Description=Micro Fileserver
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
$2
EOF

sudo systemctl daemon-reload
sudo systemctl enable $name.service
echo ""

echo "----------------------------"
[ ! -d /etc/systemd/system/$name.service ] && echo "Service verified" || echo "Service failed to install"
[ ! -d $dir/startup.sh ] && echo "Startup file verified" || echo "Startup file failed to install"
echo "----------------------------"

sudo chmod 777 -R $dir

echo ""
echo "Attempting to start service"
sudo service $name stop
sudo service $name start

sleep 5
state=$(sudo systemctl is-active $name)

apiStatus="down"
count=0

while [ "$apiStatus" != "true" ]; do

    if [ "$count" -gt 50 ]; then break; fi

    apiStatus=$(curl localhost:9001/status -s)    
    count=$((count + 1))

    sleep 1
    echo -n "$count "
done

[ "$apiStatus" = "true" ] && echo "Api is running, install complete!" || echo "Service not running, attention needed"

echo ""

