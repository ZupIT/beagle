#!/bin/bash

if [[ -n $(git status --porcelain) ]]; then
    git add "$2"
    git commit -sm "$1"
fi