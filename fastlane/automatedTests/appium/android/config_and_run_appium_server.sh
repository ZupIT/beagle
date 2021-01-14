#!/bin/bash
set -ex

sudo chown -R 777 /usr/local/
npm install -g appium --unsafe-perm=true --allow-root
appium -v
appium &>/dev/null &