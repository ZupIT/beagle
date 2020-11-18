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

import br.com.zup.beagle.android.compiler.BeagleSetupProcessor.Companion.REGISTERED_WIDGETS_GENERATED
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_LIST_WIDGET_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INTERNAL_SINGLE_WIDGET_GENERATED_EXPECTED
import br.com.zup.beagle.android.compiler.mocks.INVALID_WIDGET
import br.com.zup.beagle.android.compiler.mocks.INVALID_WIDGET_WITH_INHERITANCE
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_LIST_WIDGETS
import br.com.zup.beagle.android.compiler.mocks.VALID_WIDGET_WITH_INHERITANCE_WIDGET_VIEW
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Beagle Annotation Processor")
internal class RegisteredWidgetGeneratorTest {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register widget")
    @Nested
    inner class Widgets {

        @Test
        @DisplayName("Then should create class with list of widgets")
        fun testGenerateListOfWidgetsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_LIST_WIDGETS + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file -> file.name.startsWith(REGISTERED_WIDGETS_GENERATED) }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_LIST_WIDGET_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with list of widgets")
        fun testGenerateSingleWidgetCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + VALID_WIDGET_WITH_INHERITANCE_WIDGET_VIEW + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file -> file.name.startsWith(REGISTERED_WIDGETS_GENERATED) }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_SINGLE_WIDGET_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

    }

    @DisplayName("When register widget")
    @Nested
    inner class InvalidWidgets {

        @Test
        @DisplayName("Then should show error with invalid widget")
        fun testInvalidWidget() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + INVALID_WIDGET + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid widget")
        fun testInvalidWidgetWithInherit() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(FILE_NAME,
                BEAGLE_CONFIG_IMPORTS + INVALID_WIDGET_WITH_INHERITANCE + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
        }
    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val MESSAGE_INVALID_WIDGET_ERROR = "The class br.com.test.beagle.InvalidWidget need to inherit" +
            " from the class WidgetView when annotate class with @RegisterWidget."
    }
}