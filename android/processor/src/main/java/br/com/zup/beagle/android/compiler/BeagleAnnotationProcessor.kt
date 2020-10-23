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

import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.annotation.RegisterController
import br.com.zup.beagle.android.annotation.RegisterValidator
import br.com.zup.beagle.annotation.RegisterAction
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.compiler.BEAGLE_CONFIG
import br.com.zup.beagle.compiler.error
import br.com.zup.beagle.compiler.implementsInterface
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

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.ISOLATING)
class BeagleAnnotationProcessor : AbstractProcessor() {

    private lateinit var validatorHandlerProcessor: ValidatorHandlerProcessor
    private lateinit var beagleSetupProcessor: BeagleSetupProcessor

    override fun getSupportedAnnotationTypes(): Set<String> {
        return TreeSet(listOf(
            RegisterWidget::class.java.canonicalName,
            BeagleComponent::class.java.canonicalName,
            RegisterValidator::class.java.canonicalName,
            RegisterAction::class.java.canonicalName,
            RegisterController::class.java.canonicalName
        ))
    }

    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)

        validatorHandlerProcessor = ValidatorHandlerProcessor(processingEnvironment)
        beagleSetupProcessor = BeagleSetupProcessor(processingEnvironment)
    }

    override fun process(
        annotations: Set<TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return false

        val beagleConfigElements = roundEnvironment.getElementsAnnotatedWith(
            BeagleComponent::class.java
        ).filter { element ->
            val typeElement = element as TypeElement
            typeElement.implementsInterface(BEAGLE_CONFIG.toString())
        }

        when {
            beagleConfigElements.size > 1 -> {
                processingEnv.messager.error("BeagleConfig already defined, " +
                    "remove one implementation from the application.")
            }
            beagleConfigElements.isEmpty() -> {
                processingEnv.messager.error("Did you miss to annotate your " +
                    "BeagleConfig class with @BeagleComponent?")
            }
            else -> {
                val fullClassName = beagleConfigElements[0].asType().toString()
                val beagleConfigClassName = fullClassName.substring(
                    fullClassName.lastIndexOf(".") + 1
                )
                val basePackageName = fullClassName.replace(".$beagleConfigClassName", "")
                validatorHandlerProcessor.process(basePackageName, roundEnvironment)
                beagleSetupProcessor.process(basePackageName, beagleConfigClassName, roundEnvironment)
            }
        }

        return false
    }
}