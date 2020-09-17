#! /bin/bash

function copyGeneratedSchema() {
    for file in "$1"/*.kt
    do
        package=$(grep package "$file" | sed -e 's/package //' -e 's/\./\//g')
        mkdir -p "$2/$package"
        cp "$file" "$2/$package/$(basename "$file")"
    done
}

bash 'schema/schema-ruby-erb/model_generation/schema.sh' -k
copyGeneratedSchema 'schema/schema-ruby-erb/model_generation/Generated/Kotlin' 'schema/kotlin-core/src/main/kotlin'
copyGeneratedSchema 'schema/schema-ruby-erb/model_generation/Generated/KotlinBackend' 'backend/widgets/src/main/kotlin'
