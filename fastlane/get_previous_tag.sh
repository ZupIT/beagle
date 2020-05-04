#!/bin/bash

all_tags=`git tag -l | wc -l`

if [ $all_tags = 0 ]  || [  $all_tags = 1 ]
then
  printf `git rev-list --max-parents=0 HEAD`
else
  printf `git describe --abbrev=0 --tags $(git rev-list --tags --skip=1 --max-count=1)`
fi