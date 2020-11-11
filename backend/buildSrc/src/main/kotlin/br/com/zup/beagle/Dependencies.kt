/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package br.com.zup.beagle

import org.gradle.api.Plugin
import org.gradle.api.Project

class Dependencies : Plugin<Project> {
    override fun apply(project: Project) {}

    object ApplicationId {
        const val id = "br.com.zup.beagle.sample"
        const val micronautSample = "br.com.zup.beagle.sample.micronaut.BeagleUiSampleApplication"
    }

    object GroupId {
        const val backendSample = "br.com.zup.beagle.sample"
        const val backendStarters = "br.com.zup.beagle.spring-starter"
    }

    object Modules {
        const val widgets = ":widgets"
        const val processor = ":processor"
        const val framework = ":framework"
        const val widgetsDsl = ":widgets-dsl"

        const val sampleCore = ":sample:core"
        const val sampleSpring = ":sample:spring"
        const val sampleMicronaut = ":sample:micronaut"

        const val springStarter = ":starters:spring"
        const val micronautStarter = ":starters:micronaut"

        const val schemaKotlinCore = ":kotlin-core"

        const val commonAnnotation = ":annotation"
        const val commonProcessorUtils = ":processor-utils"
    }

    object Releases {
        const val beagleVersionName = "0.0.1-SNAPSHOT"
    }

    object Versions {
        const val kotlin = "1.4.10"
        const val kotlinPoet = "1.7.2"
        const val junit5 = "5.7.0"
        const val googleAutoService = "1.0-rc7"
        const val mockk = "1.10.2"
        const val jacksonKotlin = "2.11.3"
        const val guava = "30.0-jre"
        const val slf4j = "1.7.30"
        const val springBoot = "2.3.5.RELEASE"
        const val micronaut = "2.1.3"
        const val incap = "0.3"
    }

    object BackendLibraries {
        const val guava = "com.google.guava:guava:${Versions.guava}"
        const val slf4jJUL = "org.slf4j:slf4j-jdk14:${Versions.slf4j}"
    }

    object ProcessorLibraries {
        const val autoService = "com.google.auto.service:auto-service:${Versions.googleAutoService}"
        const val incap = "net.ltgt.gradle.incap:incap:${Versions.incap}"
        const val incapPrcessor = "net.ltgt.gradle.incap:incap-processor:${Versions.incap}"
    }

    object GeneralLibraries {
        const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        const val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"
        const val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jacksonKotlin}"
    }

    object TestLibraries {
        const val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
        const val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    }

    object SpringLibraries {
        const val autoconfigure = "org.springframework.boot:spring-boot-autoconfigure:${Versions.springBoot}"
        const val autoconfigureProcessor = "org.springframework.boot:spring-boot-autoconfigure-processor:${Versions.springBoot}"
        const val webStarter = "org.springframework.boot:spring-boot-starter-web:${Versions.springBoot}"
        const val testStarter = "org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}"
        const val actuatorStarter = "org.springframework.boot:spring-boot-starter-actuator:${Versions.springBoot}"
    }

    object MicronautLibraries {
        const val runtime = "io.micronaut:micronaut-runtime:${Versions.micronaut}"
        const val netty = "io.micronaut:micronaut-http-server-netty:${Versions.micronaut}"
        const val injectJava = "io.micronaut:micronaut-inject-java:${Versions.micronaut}"
    }

}