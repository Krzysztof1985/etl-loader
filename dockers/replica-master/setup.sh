#!/bin/bash

echo ******************************************************
echo Starting replica set
echo ******************************************************
sleep 5
#mongo replicaSet.js

mongo mongodb://mongo1:27017 replicaSet.js


echo ******************************************************
echo Finished setting up Replica Set for Mongo
echo ******************************************************
