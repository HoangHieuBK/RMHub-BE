#!/bin/sh
# restart with source code change
./take-down-docker-cluster.sh
./remove-bind-mount-backend.sh
./deploy-docker-cluster.sh
