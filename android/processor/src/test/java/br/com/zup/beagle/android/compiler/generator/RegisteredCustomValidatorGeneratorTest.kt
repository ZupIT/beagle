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

import br.com.zup.beagle.android.compiler.BeagleSetupProcessor.Companion.REGISTERED_CUSTOM_VALIDATOR_GENERATED
import br.com.zup.beagle.android.compiler.DependenciesRegistrarComponentsProvider
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.generatefunction.GenerateFunctionCustomValidator
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_CUSTOM_VALIDATOR_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_CUSTOM_VALIDATOR_WITH_REGISTRAR_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_SINGLE_CUSTOM_VALIDATOR_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INVALID_CUSTOM_VALIDATOR
import br.com.zup.beagle.android.compiler.mocks.INVALID_CUSTOM_VALIDATOR_WITH_INHERITANCE
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_CUSTOM_VALIDATOR
import br.com.zup.beagle.android.compiler.mocks.VALID_LIST_CUSTOM_VALIDATOR
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Beagle Annotation Processor")
internal class RegisteredCustomValidatorGeneratorTest : RegisteredComponentGeneratorBaseTest() {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register custom adapter")
    @Nested
    inner class CustomValidators {

        @Test
        @DisplayName("Then should create class with valid getValidator")
        fun testGenerateSingleValidatorCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_CUSTOM_VALIDATOR + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CUSTOM_VALIDATOR_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_SINGLE_CUSTOM_VALIDATOR_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with valid getValidator")
        fun testGenerateListOfCustomValidatorsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_LIST_CUSTOM_VALIDATOR + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CUSTOM_VALIDATOR_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_LIST_CUSTOM_VALIDATOR_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with valid getValidator with registrar validators")
        fun testGenerateListOfCustomValidatorsWithRegistrarCorrect() {
            // GIVEN
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    REGISTERED_CUSTOM_VALIDATOR_GENERATED,
                    GenerateFunctionCustomValidator.REGISTERED_CUSTOM_VALIDATOR,
                )
            } returns listOf(
                Pair("moduleCustomValidator", "br.com.test.beagle.otherModule.ModuleCustomValidator"),
            )

            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_LIST_CUSTOM_VALIDATOR + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CUSTOM_VALIDATOR_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_LIST_CUSTOM_VALIDATOR_WITH_REGISTRAR_GENERATED_EXPECTED
                    .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
        }

    }

    @DisplayName("When register custom adapter")
    @Nested
    inner class InvalidCustomValidator {

        @Test
        @DisplayName("Then should show error with invalid custom validator")
        fun testInvalidCustomAdapter() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + INVALID_CUSTOM_VALIDATOR + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_CUSTOM_ADAPTER_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid custom validator")
        fun testInvalidCustomValidatorWithInherit() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + INVALID_CUSTOM_VALIDATOR_WITH_INHERITANCE + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_CUSTOM_ADAPTER_ERROR))
        }
    }

    @DisplayName("When build application with beagle.generateSetupClasses kapt argument")
    @Nested
    inner class KaptArgument {

        @Test
        @DisplayName("Then should not generate RegisteredCustomValidator class")
        fun testGenerateRegisteredCustomValidatorClassFalse() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_CUSTOM_VALIDATOR + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "false")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CUSTOM_VALIDATOR_GENERATED.kt")
            }
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            Assertions.assertNull(file)
        }

        @Test
        @DisplayName("Then should generate RegisteredCustomValidator class")
        fun testGenerateRegisteredCustomValidatorClassTrue() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_CUSTOM_VALIDATOR + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "true")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CUSTOM_VALIDATOR_GENERATED.kt")
            }

            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            Assertions.assertNotNull(file)
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val MESSAGE_INVALID_CUSTOM_ADAPTER_ERROR = "The class br.com.test.beagle.InvalidCustomValidator" +
            " need to inherit from the class Validator when annotate class with @RegisterValidator."
        private const val KAPT_OPTION_NAME = "beagle.generateSetupClasses"
    }
}