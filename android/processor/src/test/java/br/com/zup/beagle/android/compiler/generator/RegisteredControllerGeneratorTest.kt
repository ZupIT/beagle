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

package br.com.zup.beagle.android.compiler.generator

import br.com.zup.beagle.android.compiler.generatefunction.CONTROLLER_REFERENCE_GENERATED
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.DEFAULT_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_DEFAULT_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_SINGLE_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_UNDEFINED_DEFAULT_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_CONTROLLER
import br.com.zup.beagle.android.compiler.mocks.VALID_DEFAULT_CONTROLLER
import br.com.zup.beagle.android.compiler.mocks.VALID_LIST_CONTROLLERS
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Beagle Annotation Processor")
internal class RegisteredControllerGeneratorTest {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register controllers")
    @Nested
    inner class Controllers {

        @Test
        @DisplayName("Then should create class with list of controllers")
        fun testGenerateSingleControllerCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + DEFAULT_IMPORTS + VALID_CONTROLLER + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(CONTROLLER_REFERENCE_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_SINGLE_CONTROLLER_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with single controller")
        fun testGenerateSingleControllerWithDefaultControllerCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_DEFAULT_CONTROLLER + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(CONTROLLER_REFERENCE_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_DEFAULT_CONTROLLER_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with single controller")
        fun testGenerateSingleControllerWithoutDefaultDeclaredCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(CONTROLLER_REFERENCE_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_UNDEFINED_DEFAULT_CONTROLLER_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with list of controllers")
        fun testGenerateListOfControllersCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_LIST_CONTROLLERS + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(CONTROLLER_REFERENCE_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_LIST_CONTROLLER_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
    }

}