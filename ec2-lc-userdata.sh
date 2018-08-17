#!/bin/bash
set -ex

# AWS CLI
mkdir /root/.aws/          ; echo -e "[default]\nregion=eu-central-1" > /root/.aws/config
mkdir /home/ec2-user/.aws/ ; echo -e "[default]\nregion=eu-central-1" > /home/ec2-user/.aws/config

# Bind Elastic IP
instance_id=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)
aws ec2 associate-address --instance-id $instance_id --public-ip 3.120.127.171

# Forward incoming requests on port 80 to local port 8080
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080

# Set timezone to Europe/Helsinki
rm -f /etc/localtime
ln -s /usr/share/zoneinfo/Europe/Helsinki /etc/localtime

# Install updates and upgrade Java
yum --assumeyes update
yum --assumeyes install java-1.8.0-openjdk-devel
yum --assumeyes remove java-1.7.0-openjdk

cd /home/ec2-user/

# Set up Gradle
mkdir .gradle/
echo "org.gradle.daemon=false" > .gradle/gradle.properties
echo "org.gradle.parallel=true" >> .gradle/gradle.properties

# Install schnitzel.ax web app
wget https://github.com/pheimdah/schnitzel.ax/archive/master.zip
unzip master.zip
rm master.zip
mkdir schnitzel.ax-master/logs/

# Restore permissions, make sure everything is owned by ec2-user
chown -R ec2-user:ec2-user /home/ec2-user/

# Start schnitzel.ax web app
cd schnitzel.ax-master/
sudo -u ec2-user bash -c "sh gradlew bootRun > logs/server.log 2>&1 &"
cd

# Set up a 1 GiB swap file
dd if=/dev/zero of=/swapfile bs=1024 count=1048576
mkswap /swapfile
chmod 0600 /swapfile
swapon /swapfile

# Install some useful utilities
yum --assumeyes install htop telnet jq

# Disable the sendmail service
chkconfig sendmail off
service sendmail stop

