#! /bin/bash

function copyGeneratedSchema() {
    for file in "$1"/*.kt
    do
        package=$(grep package "$file" | sed -e 's/package //' -e 's/\./\//g')
        mkdir -p "$2/$package"
        cp "$file" "$2/$package/$(basename "$file")"
    done

    echo "Schema: generated in $2"
}

bash '../schema-ruby-erb/model_generation/schema.sh' -a
copyGeneratedSchema '../schema-ruby-erb/model_generation/Generated/Kotlin' '../android/beagle/src/main/java'