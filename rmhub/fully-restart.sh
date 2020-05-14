#!/bin/sh
# restart cluster and deleting the database also
# this script is not marked as executable for not deleting database by accident
./destroy-docker-cluster.sh
./remove-bind-mount-backend.sh
./remove-bind-mount-kafka.sh
sh -x ./remove-bind-mount-db.sh
./deploy-docker-cluster.sh
