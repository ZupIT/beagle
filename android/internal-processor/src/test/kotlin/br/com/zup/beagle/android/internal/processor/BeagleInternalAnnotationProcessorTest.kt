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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Internal Annotation Processor")
internal class BeagleInternalAnnotationProcessorTest {

    @DisplayName("When register widget")
    @Nested
    inner class Widgets {

        @Test
        @DisplayName("Then should create class with list of widgets")
        fun checkOneParameter() {
            val kotlinSource = SourceFile.kotlin(
                "file1.kt", """
  
    """
            )


            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
//            assertThat(compilationResult.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
//            assertThat(compilationResult.messages).contains("@Summable can't be applied to FooSummable: must be a data class")
        }

    }


    private fun prepareCompilation(vararg sourceFiles: SourceFile): KotlinCompilation {
        return KotlinCompilation()
            .apply {
                annotationProcessors = listOf(BeagleInternalAnnotationProcessor())
                inheritClassPath = true
                sources = sourceFiles.asList()
                verbose = false
            }
    }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(*sourceFiles).compile()
    }
}