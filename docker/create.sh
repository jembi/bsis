#!/bin/bash

set -e
set -u

# Build the tomcat image
sudo docker build \
  -t bsis/tomcat:8 \
  docker/tomcat/

# Create the MySQL container
sudo docker create \
  --name mysql\
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  mysql:5.7

# Create the tomcat container
sudo docker create \
  --name bsis \
  --link mysql:mysql \
  -it \
  -p 8080:8080 \
  -p 8000:8000 \
  -v `pwd`/target/bsis:/usr/local/tomcat/webapps/bsis:ro \
  bsis/tomcat:8
