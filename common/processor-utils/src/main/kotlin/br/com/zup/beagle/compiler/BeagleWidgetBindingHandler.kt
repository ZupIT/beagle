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

package br.com.zup.beagle.compiler

import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class BeagleWidgetBindingHandler(
    processingEnvironment: ProcessingEnvironment,
    bindClass: BeagleClass = BIND_ANDROID
) : BeagleBindingHandler(processingEnvironment, bindClass) {

    private val outputDirectory: Filer = processingEnvironment.filer
    private val elementUtils = processingEnvironment.elementUtils

    fun handle(element: TypeElement) =
        FileSpec.get(this.elementUtils.getPackageAsString(element), this.createBindingClass(element).build())
            .writeTo(this.outputDirectory)
}