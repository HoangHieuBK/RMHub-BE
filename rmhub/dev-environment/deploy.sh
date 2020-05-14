#!/bin/sh
echo "=========================================================================="
echo "Deploying dev environment..."
HOST_IP="$(hostname -I | awk '{print$1;}')" docker-compose up -d --build

echo "=========================================================================="
echo "Done!"
