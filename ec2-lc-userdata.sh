#!/bin/bash
set -ex
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
rm -f /etc/localtime ; ln -s /usr/share/zoneinfo/Europe/Helsinki /etc/localtime
yum --assumeyes update
yum --assumeyes install java-1.8.0
update-alternatives --install /usr/bin/java java /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java 1100
