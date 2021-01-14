#!/bin/bash
echo $USER
sudo chmod -R $USER:$USER /usr/local/ #grant permission
set -ex
npm install -g appium --unsafe-perm=true --allow-root
appium -v
appium &>/dev/null &