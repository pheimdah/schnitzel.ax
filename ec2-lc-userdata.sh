#!/bin/bash
set -ex
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
rm -f /etc/localtime ; ln -s /usr/share/zoneinfo/Europe/Helsinki /etc/localtime
yum --assumeyes update
yum --assumeyes install java-1.8.0-openjdk-devel
yum --assumeyes remove java-1.7.0-openjdk

cd /home/ec2-user/
sudo -u ec2-user wget https://github.com/pheimdah/schnitzel.ax/archive/master.zip
sudo -u ec2-user unzip master.zip
sudo -u ec2-user rm master.zip
cd schnitzel.ax-master/
sudo -u ec2-user mkdir logs/
sudo -u ec2-user sh gradlew bootRun > logs/server.log 2>&1 &
