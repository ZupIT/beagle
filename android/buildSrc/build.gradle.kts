plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        register("dependencies") {
            id = "br.com.zup.beagle.dependencies"
            implementationClass = "br.com.zup.beagle.Dependencies"
        }
    }
}