#!/bin/bash
set -e

docker login registry.gitlab.com
docker build -t "registry.gitlab.com/lillyneir-dev/rmhub-delivery-demo/kubebase:latest" .
docker push "registry.gitlab.com/lillyneir-dev/rmhub-delivery-demo/kubebase:latest" 

rm kubectl helm
