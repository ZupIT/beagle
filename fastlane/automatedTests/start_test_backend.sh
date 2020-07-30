#!/bin/bash

BACKEND_DIR=backend
$BACKEND_DIR/gradlew -p $BACKEND_DIR automated-tests:bootJar
nohup java -jar $BACKEND_DIR/automated-tests/build/libs/automated-tests.jar 2>&1 &
