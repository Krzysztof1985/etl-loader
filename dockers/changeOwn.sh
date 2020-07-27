#!/usr/bin/env bash

# Script to remove old files after replica set, requires sudo permission
sudo chown ${USER}:${USER} . -R
rm -rf ./db0/data
rm -rf ./db1/data
rm -rf ./db2/data
