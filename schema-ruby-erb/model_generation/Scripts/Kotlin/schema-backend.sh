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

bash '../schema-ruby-erb/model_generation/schema.sh' -b
copyGeneratedSchema '../schema-ruby-erb/model_generation/Generated/KotlinBackend/kotlin-core' '../backend/kotlin-core/src/main/kotlin'
copyGeneratedSchema '../schema-ruby-erb/model_generation/Generated/KotlinBackend/widgets' '../backend/widgets/src/main/kotlin'
