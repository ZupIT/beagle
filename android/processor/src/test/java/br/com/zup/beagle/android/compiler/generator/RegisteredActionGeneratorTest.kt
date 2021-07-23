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

import br.com.zup.beagle.android.compiler.BeagleSetupProcessor.Companion.REGISTERED_ACTIONS_GENERATED
import br.com.zup.beagle.android.compiler.DependenciesRegistrarComponentsProvider
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.generatefunction.GenerateFunctionAction
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_ACTION_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_ACTION_WITH_REGISTRAR_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_SINGLE_ACTION_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INVALID_ACTION
import br.com.zup.beagle.android.compiler.mocks.INVALID_ACTION_WITH_INHERITANCE
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_ACTION
import br.com.zup.beagle.android.compiler.mocks.VALID_LIST_ACTION
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Beagle Annotation Processor")
internal class RegisteredActionGeneratorTest : RegisteredComponentGeneratorBaseTest() {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register action")
    @Nested
    inner class Actions {

        @Test
        @DisplayName("Then should create class with list of action")
        fun testGenerateListOfActionsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_LIST_ACTION + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_ACTIONS_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_LIST_ACTION_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with list of action with registrar actions")
        fun testGenerateListOfActionsWithRegistrarCorrect() {
            // GIVEN
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    REGISTERED_ACTIONS_GENERATED,
                    GenerateFunctionAction.REGISTERED_ACTIONS,
                )
            } returns listOf(
                Pair("", "br.com.test.beagle.otherModule.ModuleAction"),
            )
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_LIST_ACTION + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_ACTIONS_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_LIST_ACTION_WITH_REGISTRAR_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
        }

        @Test
        @DisplayName("Then should create class with list of action")
        fun testGenerateSingleActionCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_ACTION + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_ACTIONS_GENERATED.kt")
            }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString =
                INTERNAL_SINGLE_ACTION_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

    }

    @DisplayName("When register action")
    @Nested
    inner class InvalidActions {

        @Test
        @DisplayName("Then should show error with invalid action")
        fun testInvalidAction() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + INVALID_ACTION + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid action")
        fun testInvalidActionWithInherit() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + INVALID_ACTION_WITH_INHERITANCE + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(listOf(kotlinSource), BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
        }
    }

    @DisplayName("When build application with beagle.generateSetupClasses kapt argument")
    @Nested
    inner class KaptArgument {

        @Test
        @DisplayName("Then should not generate RegisteredActions class")
        fun testGenerateRegisteredActionsClassFalse() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_ACTION + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "false")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_ACTIONS_GENERATED.kt")
            }
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            assertNull(file)
        }

        @Test
        @DisplayName("Then should generate RegisteredActions class")
        fun testGenerateRegisteredActionsClassTrue() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_ACTION + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "true")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_ACTIONS_GENERATED.kt")
            }

            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            assertNotNull(file)
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val MESSAGE_INVALID_WIDGET_ERROR = "The class br.com.test.beagle.InvalidAction need to inherit" +
            " from the class Action when annotate class with @RegisterAction."
        private const val KAPT_OPTION_NAME = "beagle.generateSetupClasses"
    }
}