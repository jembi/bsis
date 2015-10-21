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

if [ -d "/opt/bsis" ]; then
  # Checkout the latest version
  cd /opt/bsis
  git fetch
  git checkout ${1:-master}
  git merge --ff-only origin/${1:-master}
else 
  # Clone the repository
  sudo mkdir --parents /opt/bsis
  sudo chown --recursive $(whoami):$(groups | awk '{print $1;}') /opt/bsis
  git clone --branch ${1:-master} --no-single-branch --depth=1 http://github.com/jembi/bsis.git /opt/bsis
  cd /opt/bsis
fi

# Build the war and create the database
mvn clean install

# Stop tomcat
sudo service tomcat7 stop

# Remove the deployed version of bsis
sudo rm -r /var/lib/tomcat7/webapps/{bsis.war,bsis}

# Deploy the war
sudo cp /opt/bsis/target/bsis.war /var/lib/tomcat7/webapps/

# Restart tomcat
sudo service tomcat7 start
