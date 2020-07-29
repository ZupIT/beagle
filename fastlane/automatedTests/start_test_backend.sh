#!/bin/bash

BACKEND_DIR="${1:-.}"
$BACKEND_DIR/gradlew :automated-tests:assemble -p $BACKEND_DIR
nohup $BACKEND_DIR/gradlew --daemon :automated-tests:bootRun -p $BACKEND_DIR 2>&1 &
