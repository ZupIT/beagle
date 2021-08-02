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

package br.com.zup.beagle.android.internal.processor

import br.com.zup.beagle.android.internal.processor.BeagleInternalAnnotationProcessor.Companion.CLASS_NAME_OPERATION
import br.com.zup.beagle.android.internal.processor.BeagleInternalAnnotationProcessor.Companion.CLASS_NAME_WIDGET
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Internal Annotation Processor")
internal class BeagleInternalAnnotationProcessorTest {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register widget")
    @Nested
    inner class Widgets {

        @Test
        @DisplayName("Then should create class with list of widgets")
        fun testGenerateListOfWidgetsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, VALID_LIST_WIDGETS)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            val file = compilationResult.generatedFiles.find { file -> file.name.startsWith(CLASS_NAME_WIDGET) }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_LIST_WIDGET_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with list of widgets")
        fun testGenerateSingleWidgetCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, VALID_WIDGET_WITH_INHERITANCE_WIDGET_VIEW)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            val file = compilationResult.generatedFiles.find { file -> file.name.startsWith(CLASS_NAME_WIDGET) }!!
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
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, INVALID_WIDGET)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid widget")
        fun testInvalidWidgetWithInherit() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, INVALID_WIDGET_WITH_INHERITANCE)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
        }

    }

    @DisplayName("When register operations")
    @Nested
    inner class Operations {

        @Test
        @DisplayName("Then should create class with map of operations")
        fun testGenerateSingleOperationsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, VALID_OPERATION)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            val file = compilationResult.generatedFiles.find { file -> file.name.startsWith(CLASS_NAME_OPERATION) }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_SINGLE_OPERATION_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

        @Test
        @DisplayName("Then should create class with map of operations")
        fun testGenerateListOperationsCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, VALID_LIST_OPERATIONS)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            val file = compilationResult.generatedFiles.find { file -> file.name.startsWith(CLASS_NAME_OPERATION) }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_LIST_OPERATION_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
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
                FILE_NAME, INVALID_OPERATION)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_OPERATION_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid operation")
        fun testInvalidOperationWithInherit() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, INVALID_OPERATION_WITH_INHERITANCE)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_OPERATION_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid operation")
        fun testInvalidOperationWithSameName() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, INVALID_OPERATION + INVALID_OPERATION_TWO)

            // WHEN
            val compilationResult = compile(kotlinSource)

            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_OPERATION_ERROR_THE_SAME_NAME))
        }

    }

    private fun prepareCompilation(vararg sourceFiles: SourceFile): KotlinCompilation {
        return KotlinCompilation()
            .apply {
                annotationProcessors = listOf(BeagleInternalAnnotationProcessor())
                inheritClassPath = true
                sources = sourceFiles.asList()
                verbose = false
                workingDir = tempPath.toFile()
            }
    }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(*sourceFiles).compile()
    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val MESSAGE_INVALID_WIDGET_ERROR = "The class br.com.test.beagle.InvalidWidget need to inherit" +
            " from the class WidgetView when annotate class with @RegisterWidget."
        private const val MESSAGE_INVALID_OPERATION_ERROR = "The class br.com.test.beagle.InvalidOperation need to inherit" +
            " from the class Operation when annotate class with @RegisterOperation."
        private const val MESSAGE_INVALID_OPERATION_ERROR_THE_SAME_NAME = "The class " +
            "br.com.test.beagle.InvalidOperationTwo need to inherit from " +
            "the class Operation when annotate class with @RegisterOperation."
    }
}