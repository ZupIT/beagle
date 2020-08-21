#! /bin/bash

GENERATED_SCHEMA='schema/schema-ruby-erb/Model Generation/Generated/Kotlin'
SCHEMA_TARGET='schema/kotlin-core/src/main/kotlin'

for file in "$GENERATED_SCHEMA"/*.kt
do
    package=$(grep package "$file" | sed -e 's/package //' -e 's/\./\//g')
    mkdir -p "$SCHEMA_TARGET/$package"
    cp "$file" "$SCHEMA_TARGET/$package/$(basename "$file")"
done