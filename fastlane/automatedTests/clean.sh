#!/bin/bash

docker container stop "$1"
docker container rm "$1"
