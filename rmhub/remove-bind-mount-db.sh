#!/bin/sh
# this script is not marked as executable for not deleting database by accident
echo "=========================================================================="
echo "Removing bind mount Postgres data..."
sudo rm -rf /rmhub/pgdata-*

echo "=========================================================================="
echo "Done!"
