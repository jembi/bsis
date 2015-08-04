#!/bin/bash

read -sp "Enter mysql username: " sqluser
echo ""
read -sp "Enter mysql password: " sqlpasswd

echo ""
echo "Deleting bsis database ..."
mysql -u $sqluser -p$sqlpasswd -e 'drop database bsis';

echo ""
echo "Creating bsis database ..."
mysql -u $sqluser -p$sqlpasswd -e 'create database bsis';

echo ""
echo "Creating schema ..."
mysql -u $sqluser -p$sqlpasswd bsis < ddl_mysql.sql

echo ""
echo "Adding initial data to database ..."
mysql -u $sqluser -p$sqlpasswd bsis < initial_data.sql

echo ""
echo "Adding blood tests to database ..."
mysql -u $sqluser -p$sqlpasswd bsis < tests.sql
