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


package br.com.zup.beagle.android.compiler

import br.com.zup.beagle.android.compiler.extensions.compile
import br.com.zup.beagle.android.compiler.mocks.BEAGLE_SETUP_COMPLETE
import br.com.zup.beagle.android.compiler.mocks.COMPLETE_BEAGLE_CUSTOM_CLASS
import br.com.zup.beagle.android.compiler.processor.BeagleAnnotationProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("Given Beagle Annotation Processor")
internal class BeagleSetupTest {

    @TempDir
    lateinit var tempPath: Path

    @DisplayName("When build application")
    @Nested
    inner class GenerateBeagleSetup {

        @Test
        @DisplayName("Then should generate beagle setup")
        fun testGenerateBeagleSetupCorrect() {
            // GIVEN
            val kotlinSource = SourceFile.kotlin(
                FILE_NAME, COMPLETE_BEAGLE_CUSTOM_CLASS)

            // WHEN
            val compilationResult = compile(kotlinSource, BeagleAnnotationProcessor(), tempPath)

            // THEN
            val file = compilationResult.generatedFiles.find { file ->
                file.name.startsWith(BeagleSetupProcessor.BEAGLE_SETUP_GENERATED)
            }!!

            val fileGeneratedInString = file.readText().replace(REGEX_REMOVE_SPACE, "")
            val fileExpectedInString = BEAGLE_SETUP_COMPLETE
                .replace(REGEX_REMOVE_SPACE, "")

            Assertions.assertEquals(fileExpectedInString, fileGeneratedInString)
            Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        }

    }


    @DisplayName("When build application with empty annotations")
    @Nested
    inner class InvalidBeagleSetup {

        @Test
        @DisplayName("Then should not generate class")
        fun testEmptyAnnotation() {
            // WHEN
            val compilationResult = compile(BeagleAnnotationProcessor(), tempPath)

            // THEN
            Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
            Assertions.assertEquals(compilationResult.generatedFiles.size, 1)
        }

    }

    companion object {
        private const val FILE_NAME = "File1.kt"
        private val REGEX_REMOVE_SPACE = "\\s".toRegex()
    }

}
