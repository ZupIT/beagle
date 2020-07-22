#!/bin/bash

./gradlew :backend:automated-tests:assemble
nohup ./gradlew --daemon :backend:automated-tests:bootRun 2>&1 &
