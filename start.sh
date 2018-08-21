#!/bin/bash

set -eu

sudo docker-compose up -d mysql
mvn clean package
sudo docker-compose up bsis
