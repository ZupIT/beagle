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

import br.com.zup.beagle.annotation.RegisterAction
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.annotation.internal.InternalAction
import br.com.zup.beagle.annotation.internal.InternalWidget
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.ISOLATING)
class BeagleBackendProcessor : AbstractProcessor() {
    companion object {
        const val WARNING_MESSAGE_TEMPLATE = "This @%s is not a class"
        val ANNOTATIONS = arrayOf(
            InternalAction::class,
            InternalWidget::class,
            RegisterAction::class,
            RegisterWidget::class
        )
    }

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()!!

    override fun getSupportedAnnotationTypes() = ANNOTATIONS.map { it.qualifiedName }.toMutableSet()

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        ANNOTATIONS.forEach { annotation ->
            roundEnvironment?.getElementsAnnotatedWith(annotation.java)?.forEach { element ->
                this.processElement(element, WARNING_MESSAGE_TEMPLATE.format(annotation.simpleName))
            }
        }
        return false
    }

    private fun processElement(element: Element, warning: String) {
        if (element is TypeElement && element.kind.isClass) {
            try {
                BeagleBindingHandler(this.processingEnv, BIND_BACKEND).also {
                    FileSpec.get(
                        this.processingEnv.elementUtils.getPackageAsString(element),
                        it.createBindingClass(element).build()
                    ).writeTo(this.processingEnv.filer)
                }
            } catch (e: Exception) {
                this.processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, e.localizedMessage, element)
            }
        } else {
            this.processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, warning, element)
        }
    }
}