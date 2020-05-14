#!/bin/sh
echo "=========================================================================="
echo "Destroying dev cluster..."
docker-compose down --remove-orphans

echo "=========================================================================="
echo "Pruning Docker volumes..."
docker volume prune -f

echo "=========================================================================="
echo "Done!"
