#!/bin/bash

BACKEND_DIR=tests/bff-server
$BACKEND_DIR/gradlew -p $BACKEND_DIR bff:bootJar
nohup java -jar $BACKEND_DIR/bff/build/libs/bff.jar 2>&1 &
