#!/bin/sh
echo "=========================================================================="
echo "Take down QA cluster..."
docker-compose down --remove-orphans

echo "=========================================================================="
echo "Pruning Docker images..."
docker image prune -f

echo "=========================================================================="
echo "Done!"
