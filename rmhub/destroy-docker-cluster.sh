#!/bin/sh
echo "=========================================================================="
echo "Destroying QA cluster..."
docker-compose down --remove-orphans

echo "=========================================================================="
echo "Pruning Docker volumes..."
docker volume prune -f

echo "=========================================================================="
echo "Pruning Docker images..."
docker image prune -f

echo "=========================================================================="
echo "Done!"
