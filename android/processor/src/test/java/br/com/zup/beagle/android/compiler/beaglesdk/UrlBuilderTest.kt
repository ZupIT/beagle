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
import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_CONFIG_IMPORTS
import br.com.zup.beagle.android.compiler.mocks.LIST_OF_URL_BUILDER
import br.com.zup.beagle.android.compiler.mocks.SIMPLE_BEAGLE_CONFIG
import br.com.zup.beagle.android.compiler.mocks.VALID_URL_BUILDER
import br.com.zup.beagle.android.compiler.mocks.VALID_URL_BUILDER_BEAGLE_SDK
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Beagle Annotation Processor")
internal class UrlBuilderTest {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When register url builder")
    @Nested
    inner class RegisterUrlBuilder {

        @Test
        @DisplayName("Then should add the url builder in beagle sdk")
        fun testGenerateUrlBuilderCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + VALID_URL_BUILDER + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(BEAGLE_SETUP_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = VALID_URL_BUILDER_BEAGLE_SDK
                .replace(REGEX_REMOVE_SPACE, "")

            assertEquals(fileExpectedInString, fileGeneratedInString)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

    }


    @DisplayName("When register url builder")
    @Nested
    inner class InvalidUrlBuilder {

        @Test
        @DisplayName("Then should show error with duplicate url builder")
        fun testDuplicate() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, BEAGLE_CONFIG_IMPORTS + LIST_OF_URL_BUILDER + SIMPLE_BEAGLE_CONFIG)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)


            // THEN
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
            Assertions.assertTrue(compilationResult.messages.contains(MESSAGE_DUPLICATE_URL_BUILDER))
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
        private const val MESSAGE_DUPLICATE_URL_BUILDER = "UrlBuilder defined multiple times: \n" +
            "public final class UrlBuilderTestTwo implements " +
            "br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder {\n" +
            "             ^\n" +
            "  br.com.test.beagle.UrlBuilderTestTwo\n" +
            "  br.com.test.beagle.UrlBuilderTest\n" +
            "  \n" +
            "  You must remove one implementation from the application."
    }

}