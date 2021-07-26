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

package br.com.zup.beagle.android.compiler.beaglesdk

import br.com.zup.beagle.android.compiler.BeagleSetupProcessor.Companion.BEAGLE_SETUP_GENERATED
import br.com.zup.beagle.android.compiler.DependenciesRegistrarComponentsProvider
import br.com.zup.beagle.android.compiler.PROPERTIES_REGISTRAR_CLASS_NAME
import br.com.zup.beagle.android.compiler.PROPERTIES_REGISTRAR_METHOD_NAME
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.FORM_LOCAL_ACTION_HANDLER_IMPORT
import br.com.zup.beagle.android.compiler.mocks.LIST_OF_FORM_LOCAL_ACTION_HANDLER
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_FORM_LOCAL_ACTION_HANDLER
import br.com.zup.beagle.android.compiler.mocks.VALID_FORM_LOCAL_ACTION_HANDLER_BEAGLE_SDK
import br.com.zup.beagle.android.compiler.mocks.VALID_FORM_LOCAL_ACTION_HANDLER_BEAGLE_SDK_FROM_REGISTRAR
import br.com.zup.beagle.android.compiler.mocks.VALID_THIRD_FORM_LOCAL_ACTION_HANDLER
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
internal class FormLocalActionHandlerTest : BeagleSdkBaseTest() {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register form local action handler")
    @Nested
    inner class RegisterFormLocalActionHandler {

        @Test
        @DisplayName("Then should add the form local action handler in beagle sdk")
        fun testGenerateFormLocalActionHandlerCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_FORM_LOCAL_ACTION_HANDLER + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(BEAGLE_SETUP_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = VALID_FORM_LOCAL_ACTION_HANDLER_BEAGLE_SDK
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

    }

    @DisplayName("When already registered in other module PropertiesRegistrar")
    @Nested
    inner class RegisterFromOtherModule {
        @Test
        @DisplayName("Then should add the form local action handler in beagle sdk")
        fun testGenerateFormLocalActionHandlerFromRegistrarCorrect() {
            // GIVEN
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    PROPERTIES_REGISTRAR_CLASS_NAME,
                    PROPERTIES_REGISTRAR_METHOD_NAME,
                )
            } returns listOf(
                Pair("""formLocalActionHandler""", "br.com.test.beagle.FormLocalActionHandlerTestThree()"),
            )
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + FORM_LOCAL_ACTION_HANDLER_IMPORT +
                    VALID_THIRD_FORM_LOCAL_ACTION_HANDLER + SIMPLE_BEAGLE_CONFIG
            )

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(BEAGLE_SETUP_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = VALID_FORM_LOCAL_ACTION_HANDLER_BEAGLE_SDK_FROM_REGISTRAR
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }
    }

    @DisplayName("When register form local action handler")
    @Nested
    inner class InvalidFormLocalActionHandler {

        @Test
        @DisplayName("Then should show error with duplicate form local action handler")
        fun testDuplicate() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + LIST_OF_FORM_LOCAL_ACTION_HANDLER + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)


            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            Assertions.assertTrue(compilationResult.messages.contains(MESSAGE_DUPLICATE_FORM_LOCAL_ACTION_HANDLER))
        }

        @Test
        @DisplayName("Then should show error with duplicate form local action handler in PropertiesRegistrar")
        fun testDuplicateInRegistrar() {
            // GIVEN
            every {
                DependenciesRegistrarComponentsProvider.getRegisteredComponentsInDependencies(
                    any(),
                    PROPERTIES_REGISTRAR_CLASS_NAME,
                    PROPERTIES_REGISTRAR_METHOD_NAME,
                )
            } returns listOf(
                Pair("""formLocalActionHandler""", "br.com.test.beagle.FormLocalActionHandlerTestThree()"),
            )
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_FORM_LOCAL_ACTION_HANDLER +
                    VALID_THIRD_FORM_LOCAL_ACTION_HANDLER + SIMPLE_BEAGLE_CONFIG
            )

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            Assertions.assertTrue(compilationResult.messages.contains(MESSAGE_DUPLICATE_FORM_LOCAL_ACTION_HANDLER_REGISTRAR))
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val MESSAGE_DUPLICATE_FORM_LOCAL_ACTION_HANDLER =
            "error: FormLocalActionHandler defined multiple times: " +
                "1 - br.com.test.beagle.FormLocalActionHandlerTest " +
                "2 - br.com.test.beagle.FormLocalActionHandlerTestTwo. " +
                "You must remove one implementation from the application."

        private const val MESSAGE_DUPLICATE_FORM_LOCAL_ACTION_HANDLER_REGISTRAR =
            "error: FormLocalActionHandler defined multiple times: " +
                "1 - br.com.test.beagle.FormLocalActionHandlerTest " +
                "2 - br.com.test.beagle.FormLocalActionHandlerTestThree. " +
                "You must remove one implementation from the application."
    }

}