#!/bin/bash

docker build -t "$1" ../backend/automated-tests &&
docker run --name "$2" -dp 8080:8080 "$1"
