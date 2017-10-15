#!/bin/bash
set -ex
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
rm -f /etc/localtime ; ln -s /usr/share/zoneinfo/Europe/Helsinki /etc/localtime
yum --assumeyes update
yum --assumeyes install java-1.8.0
yum --assumeyes remove java-1.7.0-openjdk

cd /home/ec2-user/
sudo -u ec2-user wget https://s3.eu-central-1.amazonaws.com/schnitzel.ax-artifacts/schnitzelax/schnitzel.ax-1.0.jar -P /home/ec2-user/
sudo -u ec2-user mkdir logs
sudo -u ec2-user java -jar schnitzel.ax-1.0.jar > logs/server.log 2>&1 &
