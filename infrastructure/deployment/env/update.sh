#!/bin/bash

set -o errexit
set -o nounset

# Checkout the latest version
cd /opt/bsis
git fetch origin ${1:-master}
git checkout FETCH_HEAD

# Build the war and create the database
mvn clean install

# Stop tomcat
sudo service tomcat7 stop

# Remove the deployed version of bsis
sudo rm -rf /var/lib/tomcat7/webapps/{bsis.war,bsis}

# Deploy the war
sudo cp /opt/bsis/target/bsis.war /var/lib/tomcat7/webapps/

# Restart tomcat
sudo service tomcat7 start
