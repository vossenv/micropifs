#!/usr/bin/env bash

./gradlew build

./deploy-artifacts.sh -h 192.168.50.80 -full
./deploy-artifacts.sh -h 192.168.50.79 -full
./deploy-artifacts.sh -h 192.168.50.230 -full
scp build/libs/micropifs.jar jenkins@192.168.50.66:"C:/Program\\ Files\\ (x86)/micropifs/micropifs.jar"
ssh jenkins@192.168.50.66 Restart-Service -Name micropifs