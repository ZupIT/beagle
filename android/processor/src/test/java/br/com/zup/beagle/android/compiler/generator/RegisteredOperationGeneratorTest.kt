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

import br.com.zup.beagle.android.compiler.BeagleSetupProcessor.Companion.REGISTERED_OPERATIONS_GENERATED
import br.com.zup.beagle.android.compiler.DependenciesRegistrarComponentsProvider
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_OPERATION_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_OPERATION_WITH_REGISTRAR_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_SINGLE_OPERATION_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INVALID_OPERATION
import br.com.zup.beagle.android.compiler.mocks.INVALID_OPERATION_TWO
import br.com.zup.beagle.android.compiler.mocks.INVALID_OPERATION_WITH_INHERITANCE
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_LIST_OPERATIONS
import br.com.zup.beagle.android.compiler.mocks.VALID_OPERATION
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import br.com.zup.beagle.compiler.shared.GenerateFunctionOperation
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
internal class RegisteredOperationGeneratorTest : RegisteredComponentGeneratorBaseTest() {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register operations")
    @Nested
    inner class Operations {

        @Test
        @DisplayName("Then should create class with map of operations")
        fun testGenerateSingleOperationsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_OPERATION + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_OPERATIONS_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_SINGLE_OPERATION_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with map of operations")
        fun testGenerateListOperationsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_LIST_OPERATIONS + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_OPERATIONS_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_LIST_OPERATION_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with map of operations with registrar operations")
        fun testGenerateListOperationsWithRegistrarCorrect() {
            // GIVEN
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    REGISTERED_OPERATIONS_GENERATED,
                    GenerateFunctionOperation.REGISTERED_OPERATIONS,
                )
            } returns listOf(
                Pair("moduleOperation", "br.com.test.beagle.otherModule.ModuleOperation"),
            )
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_LIST_OPERATIONS + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_OPERATIONS_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_LIST_OPERATION_WITH_REGISTRAR_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
        }

    }

    @DisplayName("When register operations")
    @Nested
    inner class InvalidOperations {

        @Test
        @DisplayName("Then should show error with invalid operation")
        fun testInvalidOperation() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + INVALID_OPERATION + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_OPERATION_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid operation")
        fun testInvalidOperationWithInherit() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + INVALID_OPERATION_WITH_INHERITANCE + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_OPERATION_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid operation")
        fun testInvalidOperationWithSameName() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS +
                INVALID_OPERATION + INVALID_OPERATION_TWO + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_OPERATION_ERROR_THE_SAME_NAME))
        }

    }

    @DisplayName("When build application with beagle.generateSetupClasses kapt argument")
    @Nested
    inner class KaptArgument {

        @Test
        @DisplayName("Then should not generate RegisteredOperations class")
        fun testGenerateRegisteredOperationsClassFalse() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_OPERATION + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "false")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_OPERATIONS_GENERATED.kt")
            }
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            Assertions.assertNull(file)
        }

        @Test
        @DisplayName("Then should generate RegisteredOperations class")
        fun testGenerateRegisteredOperationsClassTrue() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_OPERATION + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "true")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_OPERATIONS_GENERATED.kt")
            }

            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            Assertions.assertNotNull(file)
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val MESSAGE_INVALID_OPERATION_ERROR =
            "The class br.com.test.beagle.InvalidOperation need to inherit" +
                " from the class Operation when annotate class with @RegisterOperation."
        private const val MESSAGE_INVALID_OPERATION_ERROR_THE_SAME_NAME = "The class " +
            "br.com.test.beagle.InvalidOperationTwo need to inherit from " +
            "the class Operation when annotate class with @RegisterOperation."
        private const val KAPT_OPTION_NAME = "beagle.generateSetupClasses"
    }

}