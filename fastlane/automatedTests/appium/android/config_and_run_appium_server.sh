#!/bin/bash
sudo chmod -R 777 /usr/local/
set -ex
npm install -g appium --unsafe-perm=true --allow-root
appium -v
appium &>/dev/null &