MICRONAUT_DIR=src/main/kotlin/br/com/zup/beagle/sample/micronaut
SPRING_DIR=../spring/src/main/kotlin/br/com/zup/beagle/sample/spring

function convert_package() {
    for source in "$MICRONAUT_DIR/$2"/*.kt; do
        sed -f "$1" micronaut2spring.sed > "$SPRING_DIR/$2/$(basename "$source")"
    done
}

convert_package service
convert_package controller
