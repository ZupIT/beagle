#! /bin/bash

function copyGeneratedSchema() {
    for file in "$1"/*.kt
    do
        package=$(grep package "$file" | sed -e 's/package //' -e 's/\./\//g')
        mkdir -p "$2/$package"
        cp "$file" "$2/$package/$(basename "$file")"
    done
}

bash 'schema-ruby-erb/model_generation/schema.sh' -k
copyGeneratedSchema 'schema-ruby-erb/model_generation/Generated/KotlinBackend/kotlin-core' 'backend/kotlin-core/src/main/kotlin'
copyGeneratedSchema 'schema-ruby-erb/model_generation/Generated/KotlinBackend/widgets' 'backend/widgets/src/main/kotlin'

copyGeneratedSchema 'schema-ruby-erb/model_generation/Generated/Kotlin' 'android/beagle/src/main/java'

# copyGeneratedSchema 'schema-ruby-erb/model_generation/Generated/Kotlin' 'schema/kotlin-core/src/main/kotlin'
# copyGeneratedSchema 'schema-ruby-erb/model_generation/Generated/KotlinBackend' 'backend/widgets/src/main/kotlin'
# mv backend/widgets/src/main/kotlin/br/com/zup/beagle/core/*.kt 'schema/kotlin-core/src/main/kotlin/br/com/zup/beagle/core'
# mv backend/widgets/src/main/kotlin/br/com/zup/beagle/widget/core/*.kt 'schema/kotlin-core/src/main/kotlin/br/com/zup/beagle/widget/core'
