#!/bin/bash

echo "$3" | docker login docker.pkg.github.com -u "$2" --password-stdin
docker push "$1"
