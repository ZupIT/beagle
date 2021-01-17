#!/bin/bash
set -e
npm install -g appium
appium -v
appium &>/dev/null &