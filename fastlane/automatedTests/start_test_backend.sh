#!/bin/bash

BACKEND_DIR=tests/bff-dir
$BACKEND_DIR/gradlew -p $BACKEND_DIR bff:bootJar
nohup java -jar $BACKEND_DIR/automated-tests/build/libs/automated-tests.jar 2>&1 &
