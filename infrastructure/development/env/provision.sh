#!/bin/bash

set -o errexit
set -o nounset

MYSQL_ROOT_PASSWORD=root

# Fetch the latest packages
sudo apt-get update

# Disable prompts
export DEBIAN_FRONTEND=noninteractive

# Set selections for the mysql root password
echo "mysql-server-5.6 mysql-server/root_password password ${MYSQL_ROOT_PASSWORD}" | sudo debconf-set-selections
echo "mysql-server-5.6 mysql-server/root_password_again password ${MYSQL_ROOT_PASSWORD}" | sudo debconf-set-selections

# Install packages
sudo apt-get install --quiet --assume-yes \
  git \
  mysql-server-5.6 \
  openjdk-7-jdk \
  tomcat7 \
  maven
