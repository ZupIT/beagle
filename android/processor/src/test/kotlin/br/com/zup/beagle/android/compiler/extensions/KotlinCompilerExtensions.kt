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

package br.com.zup.beagle.android.compiler.extensions

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.OptionName
import com.tschuchort.compiletesting.OptionValue
import com.tschuchort.compiletesting.SourceFile
import java.nio.file.Path
import javax.annotation.processing.Processor

private fun prepareCompilation(
    sourceFiles: List<SourceFile>? = null,
    processor: Processor,
    path: Path,
    kaptArguments: MutableMap<OptionName, OptionValue>? = null
): KotlinCompilation =
    KotlinCompilation()
        .apply {
            annotationProcessors = listOf(processor)
            inheritClassPath = true
            sources = sourceFiles ?: emptyList()
            verbose = false
            workingDir = path.toFile()
            kaptArgs = kaptArguments ?: mutableMapOf()
        }

fun compile(
    sourceFile: SourceFile,
    processor: Processor,
    path: Path,
    kaptArguments: MutableMap<OptionName, OptionValue>? = null
): KotlinCompilation.Result {
    return prepareCompilation(listOf(sourceFile), processor, path, kaptArguments).compile()
}

fun compile(
    sourceFiles: List<SourceFile>,
    processor: Processor,
    path: Path,
    kaptArguments: MutableMap<OptionName, OptionValue>? = null
): KotlinCompilation.Result {
    return prepareCompilation(sourceFiles, processor, path, kaptArguments).compile()
}

fun compile(
    processor: Processor,
    path: Path,
    kaptArguments: MutableMap<OptionName, OptionValue>? = null
): KotlinCompilation.Result {
    return prepareCompilation(processor = processor, path = path, kaptArguments = kaptArguments).compile()
}