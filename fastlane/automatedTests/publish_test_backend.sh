#!/bin/bash

BACKEND_DIR=tests/bff-server
IMAGE_NAME=$(echo "$registry/$repository/$image" | tr [:upper:] [:lower:])
$BACKEND_DIR/gradlew -p $BACKEND_DIR bff:bootBuildImage --imageName=$IMAGE_NAME
echo $token | docker login $registry -u $user --password-stdin
docker push $IMAGE_NAME
docker logout $registry
