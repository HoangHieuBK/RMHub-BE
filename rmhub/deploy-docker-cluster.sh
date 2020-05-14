#!/bin/sh
echo "=========================================================================="
echo "Maven build..."
mvn clean install -DskipTests -T 8

echo "=========================================================================="
echo "Deploying QA cluster..."
# HOST_IP variable is used for expose kafka advertised listener
# Work with Ubuntu 18.04 LTS +
export HOST_IP="$(hostname -I | awk '{print$1;}')"
echo "Host IP used for kafka advertised listener: ${HOST_IP}"

docker-compose up -d --build --remove-orphans

echo "=========================================================================="
echo "Let's check the log..."
docker-compose logs -f
