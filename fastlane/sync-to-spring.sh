#!/bin/bash

MICRONAUT_DIR=../backend/sample/micronaut/src/main/kotlin/br/com/zup/beagle/sample/micronaut
SPRING_DIR=../backend/sample/spring/src/main/kotlin/br/com/zup/beagle/sample/spring

function convert_package() {
    for source in "$MICRONAUT_DIR/$1"/*.kt; do
        sed -f micronaut2spring.sed "$source" > "$SPRING_DIR/$1/$(basename "$source")"
    done
}

convert_package service
convert_package controller
