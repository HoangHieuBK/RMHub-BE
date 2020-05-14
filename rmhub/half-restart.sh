#!/bin/sh
# restart without deleting database
./destroy-docker-cluster.sh
./remove-bind-mount-backend.sh
./remove-bind-mount-kafka.sh
./deploy-docker-cluster.sh
