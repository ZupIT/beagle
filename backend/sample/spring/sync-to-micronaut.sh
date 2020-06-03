MICRONAUT_DIR=../micronaut/src/main/kotlin/br/com/zup/beagle/sample/micronaut
SPRING_DIR=src/main/kotlin/br/com/zup/beagle/sample/spring

function convert_package() {
    for source in "$SPRING_DIR/$2"/*.kt; do
        sed -f "$1" spring2micronaut.sed > "$MICRONAUT_DIR/$2/$(basename "$source")"
    done
}

convert_package service
convert_package controller
