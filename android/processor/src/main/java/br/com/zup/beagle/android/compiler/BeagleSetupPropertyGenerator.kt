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
import br.com.zup.beagle.android.compiler.beaglesetupmanage.PropertyImplementationManager
import br.com.zup.beagle.android.compiler.beaglesetupmanage.TypeElementImplementationManager
import br.com.zup.beagle.android.compiler.processor.forEachRegisteredDependency
import br.com.zup.beagle.compiler.shared.PROPERTIES_REGISTRAR_CLASS_NAME
import br.com.zup.beagle.compiler.shared.PROPERTIES_REGISTRAR_METHOD_NAME
import br.com.zup.beagle.compiler.shared.REGISTRAR_COMPONENTS_PACKAGE
import br.com.zup.beagle.compiler.shared.error
import br.com.zup.beagle.compiler.shared.implements
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

internal class BeagleSetupPropertyGenerator(private val processingEnv: ProcessingEnvironment) {

    fun generate(
        roundEnvironment: RoundEnvironment,
    ): List<PropertySpec> {
        val propertySpecifications: PropertySpecifications? = PropertySpecifications()

        // Get properties possibly registered in dependencies modules
        // TODO: refatorar
//            processingEnv.elementUtils.getPackageElement(REGISTRAR_COMPONENTS_PACKAGE)?.enclosedElements?.forEach {
//                val fullClassName = it.toString()
//                if (fullClassName.contains(PROPERTIES_REGISTRAR_CLASS_NAME)) {
//                    val cls = Class.forName(fullClassName)
//                    val kotlinClass = cls.kotlin
//                    try {
//                        (cls.getMethod(PROPERTIES_REGISTRAR_METHOD_NAME).invoke(kotlinClass.objectInstance) as List<Pair<String, String>>).forEach { registeredDependency ->
//                            // TODO: ver esse negócio de remover o () do final
//                            // TODO: ver o que fazer com nosso amigo a querida propriedade serverDrivenActivity
//                            val typeElement = processingEnv.elementUtils.getTypeElement(registeredDependency.second.removeSuffix("()"))
//                            checkIfHandlersExists(typeElement, propertySpecifications)
//                            checkIfOtherAttributesExists(typeElement, propertySpecifications)
//                        }
//                    } catch (e: NoSuchMethodException) {
//                        // intentionally left blank
//                    }
//                }
//            }
        forEachRegisteredDependency(
            processingEnv,
            PROPERTIES_REGISTRAR_CLASS_NAME,
            PROPERTIES_REGISTRAR_METHOD_NAME) { registeredDependency ->
            //TODO: não verificar duplicidade nos registrar
            val typeElement = processingEnv.elementUtils.getTypeElement(registeredDependency.second.removeSuffix("()"))
            checkIfHandlersExists(typeElement, propertySpecifications)
            checkIfOtherAttributesExists(typeElement, propertySpecifications)
        }

        roundEnvironment.getElementsAnnotatedWith(BeagleComponent::class.java).forEach { element ->
            val typeElement = element as TypeElement
            checkIfHandlersExists(typeElement, propertySpecifications)
            checkIfOtherAttributesExists(typeElement, propertySpecifications)
        }

        return createListOfPropertySpec(propertySpecifications)
    }

    private fun checkIfHandlersExists(
        typeElement: TypeElement,
        propertySpecifications: PropertySpecifications?,
    ) {
        TypeElementImplementationManager.manage(processingEnv, typeElement, propertySpecifications)
    }

    private fun checkIfOtherAttributesExists(
        typeElement: TypeElement,
        propertySpecifications: PropertySpecifications?,
    ) {
        when {
            typeElement.implements(DESIGN_SYSTEM, processingEnv) -> {
                if (propertySpecifications?.designSystem == null) {
                    propertySpecifications?.designSystem = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "DesignSystem")
                }
            }
            typeElement.implements(BEAGLE_ACTIVITY, processingEnv) -> {
                if (propertySpecifications?.defaultBeagleActivity == null) {
                    propertySpecifications?.defaultBeagleActivity = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "BeagleActivity")
                }
            }
            typeElement.implements(ANALYTICS, processingEnv) -> {
                if (propertySpecifications?.analytics == null) {
                    propertySpecifications?.analytics = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "Analytics")
                }
            }
            typeElement.implements(ANALYTICS_PROVIDER, processingEnv) -> {
                if (propertySpecifications?.analyticsProvider == null) {
                    propertySpecifications?.analyticsProvider = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "AnalyticsProvider")
                }
            }
        }
    }

    private fun logImplementationErrorMessage(typeElement: TypeElement, element: String) {
        processingEnv.messager.error(typeElement, "$element already " +
            "defined, remove one implementation from the application.")
    }

    private fun createListOfPropertySpec(
        propertySpecifications: PropertySpecifications?,
    ): List<PropertySpec> {
        return PropertyImplementationManager.manage(propertySpecifications).toMutableList().apply {
            add(implementServerDrivenActivityProperty(propertySpecifications?.defaultBeagleActivity))
        }
    }

    private fun implementServerDrivenActivityProperty(typeElement: TypeElement?): PropertySpec {
        return implementServerDrivenActivityProperty(typeElement.toString())
    }

    fun implementServerDrivenActivityProperty(activity: String, isFormatted: Boolean = false): PropertySpec {
        val initializer = if (!isFormatted) "$activity::class.java as Class<BeagleActivity>" else activity
        return PropertySpec.builder(
            "serverDrivenActivity",
            Class::class.asClassName().parameterizedBy(
                ClassName(BEAGLE_ACTIVITY.packageName, BEAGLE_ACTIVITY.className)
            ),
            KModifier.OVERRIDE
        ).initializer(initializer).build()
    }
}

internal data class PropertySpecifications(
    var deepLinkHandler: TypeElement? = null,
    var formLocalActionHandler: TypeElement? = null,
    var httpClient: TypeElement? = null,
    var httpClientFactory: TypeElement? = null,
    var designSystem: TypeElement? = null,
    var defaultBeagleActivity: TypeElement? = null,
    var beagleActivities: List<TypeElement>? = null,
    var urlBuilder: TypeElement? = null,
    var storeHandler: TypeElement? = null,
    var analytics: TypeElement? = null,
    var analyticsProvider: TypeElement? = null,
    var logger: TypeElement? = null,
    var imageDownloader: TypeElement? = null,
    var config: TypeElement? = null,
)
