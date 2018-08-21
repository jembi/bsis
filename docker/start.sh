#!/bin/bash

set -e
set -u

if [ ! -f pom.xml ]; then
  echo "No pom.xml file found! Run this script from the project root directory."
  exit 1
fi

sed -i "s/mysql:\/\/localhost/mysql:\/\/mysql/" src/main/resources/database.properties

mvn compile war:exploded

# Start the containers - mysql in the background and tomcat in the foreground
sudo docker start mysql
sudo docker start -ai bsis
