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

import br.com.zup.beagle.android.compiler.DependenciesRegistrarComponentsProvider
import br.com.zup.beagle.android.compiler.PROPERTIES_REGISTRAR_CLASS_NAME
import br.com.zup.beagle.android.compiler.PROPERTIES_REGISTRAR_METHOD_NAME
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.generatefunction.REGISTERED_CONTROLLERS_GENERATED
import br.com.zup.beagle.android.compiler.generatefunction.RegisterControllerProcessor
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.DEFAULT_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_DEFAULT_CONTROLLER_BEAGLE_COMPONENT_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_DEFAULT_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_CONTROLLER_WITH_REGISTRAR_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_SINGLE_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_UNDEFINED_DEFAULT_CONTROLLER_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_CONTROLLER
import br.com.zup.beagle.android.compiler.mocks.VALID_DEFAULT_CONTROLLER
import br.com.zup.beagle.android.compiler.mocks.VALID_DEFAULT_CONTROLLER_BEAGLE_COMPONENT
import br.com.zup.beagle.android.compiler.mocks.VALID_LIST_CONTROLLERS
import br.com.zup.beagle.android.compiler.mocks.VALID_SECOND_DEFAULT_CONTROLLER
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Beagle Annotation Processor")
internal class RegisteredControllerGeneratorTest : RegisteredComponentGeneratorBaseTest() {

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
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
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
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
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
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
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
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_LIST_CONTROLLER_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with list of controllers with registrar controllers")
        fun testGenerateListOfControllersWithRegistrarCorrect() {
            // GIVEN
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    REGISTERED_CONTROLLERS_GENERATED,
                    RegisterControllerProcessor.REGISTERED_CONTROLLERS,
                )
            } returns listOf(
                Pair("""controllerFromOtherModule""", "br.com.test.beagle.otherModule.ModuleBeagleActivity"),
            )
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    PROPERTIES_REGISTRAR_CLASS_NAME,
                    PROPERTIES_REGISTRAR_METHOD_NAME,
                )
            } returns listOf(
                Pair("serverDrivenActivity",
                    "br.com.test.beagle.AppDefaultBeagleActivityBeagleComponent::class.java as Class<BeagleActivity>"),
            )
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_LIST_CONTROLLERS +
                    VALID_DEFAULT_CONTROLLER_BEAGLE_COMPONENT + SIMPLE_BEAGLE_CONFIG
            )

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_LIST_CONTROLLER_WITH_REGISTRAR_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
        }

        @Test
        @DisplayName("Then should create class with list of controllers with controller registered with BeagleComponent")
        fun testGenerateListOfControllersRegisteredWithBeagleComponentCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + DEFAULT_IMPORTS + VALID_DEFAULT_CONTROLLER_BEAGLE_COMPONENT +
                    VALID_CONTROLLER + SIMPLE_BEAGLE_CONFIG
            )

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_DEFAULT_CONTROLLER_BEAGLE_COMPONENT_GENERATED_EXPECTED
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
        }

        @Test
        @DisplayName("Then should show error with duplicate default controllers")
        fun testDuplicate() {
            // GIVEN
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    REGISTERED_CONTROLLERS_GENERATED,
                    RegisterControllerProcessor.REGISTERED_CONTROLLERS,
                )
            } returns listOf(
                Pair("""""", "br.com.test.beagle.otherModule.ModuleDefaultBeagleActivity"),
            )

            val kotlinSource = SourceFile.kotlin(
                FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_DEFAULT_CONTROLLER +
                    VALID_SECOND_DEFAULT_CONTROLLER + SIMPLE_BEAGLE_CONFIG
            )

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            Assertions.assertTrue(compilationResult.messages.contains(MESSAGE_DUPLICATE_DEFAULT_CONTROLLER))
        }

    }

    @DisplayName("When build application with beagle.generateSetupClasses kapt argument")
    @Nested
    inner class KaptArgument {

        @Test
        @DisplayName("Then should not generate RegisteredControllers class")
        fun testGenerateRegisteredControllersClassFalse() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_DEFAULT_CONTROLLER + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "false")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
            }
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            Assertions.assertNull(file)
        }

        @Test
        @DisplayName("Then should generate RegisteredControllers class")
        fun testGenerateRegisteredControllersClassTrue() {
            //GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_DEFAULT_CONTROLLER + SIMPLE_BEAGLE_CONFIG)

            val kaptArguments = mutableMapOf(KAPT_OPTION_NAME to "true")

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath, kaptArguments)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith("$REGISTERED_CONTROLLERS_GENERATED.kt")
            }

            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            Assertions.assertNotNull(file)
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val KAPT_OPTION_NAME = "beagle.generateSetupClasses"
        private const val MESSAGE_DUPLICATE_DEFAULT_CONTROLLER = "Default controller defined multiple times: " +
            "1 - br.com.test.beagle.AppDefaultBeagleActivity " +
            "2 - br.com.test.beagle.otherModule.ModuleDefaultBeagleActivity. " +
            "You must remove one implementation from the application."
    }

}