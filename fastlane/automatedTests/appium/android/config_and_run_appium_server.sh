#!/bin/bash
set -ex
sudo npm install -g appium
appium -v
appium &>/dev/null &