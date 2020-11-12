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

import br.com.zup.beagle.annotation.RegisterOperation
import br.com.zup.beagle.annotation.RegisterWidget
import com.google.auto.service.AutoService
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import java.util.TreeSet
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

const val BEAGLE_PACKAGE_INTERNAL = "br.com.zup.beagle.android.setup"

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.ISOLATING)
class BeagleInternalAnnotationProcessor : AbstractProcessor() {
    private lateinit var internalWidgetFactoryProcessor: InternalWidgetFactoryProcessor
    private lateinit var internalOperationFactoryProcessor: InternalOperationFactoryProcessor

    override fun getSupportedAnnotationTypes(): Set<String> {
        return TreeSet(listOf(
            RegisterWidget::class.java.canonicalName,
            RegisterOperation::class.java.canonicalName))
    }

    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        internalWidgetFactoryProcessor = InternalWidgetFactoryProcessor(processingEnvironment)
        internalOperationFactoryProcessor = InternalOperationFactoryProcessor(processingEnv)
    }

    override fun process(
        annotations: Set<TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return false
        internalWidgetFactoryProcessor.process(BEAGLE_PACKAGE_INTERNAL, roundEnvironment)
        internalOperationFactoryProcessor.process(BEAGLE_PACKAGE_INTERNAL, roundEnvironment)
        return false
    }
}