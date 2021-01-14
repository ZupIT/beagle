#!/bin/bash
sudo chmod -R a+rwX /usr/local/lib/node_modules #grant permission
set -ex
npm install -g appium
appium -v
appium &>/dev/null &