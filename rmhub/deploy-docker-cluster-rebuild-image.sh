#!/bin/sh
echo "=========================================================================="
echo "Deploying QA cluster..."
export HOST_IP="$(hostname -I | awk '{print$1;}')"
echo "Host IP used for kafka advertised listener: ${HOST_IP}"

docker-compose up -d --build --remove-orphans

echo "=========================================================================="
echo "Let's check the log..."
docker-compose logs -f
