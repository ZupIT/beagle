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
        fun testGenerateWidgetsCorrect() {
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, VALID_WIDGETS)


            val compilationResult = compile(kotlinSource)
            val file = compilationResult.generatedFiles.find { file -> file.name.startsWith(InternalWidgetFactoryProcessor.CLASS_NAME) }!!
            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = INTERNAL_WIDGET_GENERATED_EXPECTED.replace(REGEX_REMOVE_SPACE, "")
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
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, INVALID_WIDGET)

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
        }

        @Test
        @DisplayName("Then should show error with invalid widget")
        fun testInvalidWidgetWithInherit() {
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, INVALID_WIDGET_WITH_INHERITANCE)

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            assertTrue(compilationResult.messages.contains(MESSAGE_INVALID_WIDGET_ERROR))
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
    }
}