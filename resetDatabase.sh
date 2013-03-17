#!/bin/bash

read -sp "Enter mysql password: " sqlpasswd

echo ""
echo "Deleting v2v database ..."
mysql -u v2v -p$sqlpasswd -e 'drop database v2v_new';

echo ""
echo "Creating v2v database ..."
mysql -u v2v -p$sqlpasswd -e 'create database v2v_new';

echo ""
echo "Creating schema ..."
mysql -u v2v -p$sqlpasswd v2v_new < ddl_mysql.sql

echo ""
echo "Adding initial data to database ..."
mysql -u v2v -p$sqlpasswd v2v_new < initial_data.sql

echo ""
echo "Adding blood tests to database ..."
mysql -u v2v -p$sqlpasswd v2v_new < tests.sql
