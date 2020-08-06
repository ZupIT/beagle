#!/bin/bash

if [[ -n $(git status --porcelain) ]]; then
    git config user.email 'beagle@zup.com.br'
    git config user.name 'Beagle'
    git add "$2"
    git commit -sm "$1"
fi