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
import br.com.zup.beagle.compiler.ANALYTICS
import br.com.zup.beagle.compiler.BEAGLE_ACTIVITY
import br.com.zup.beagle.compiler.BEAGLE_LOGGER
import br.com.zup.beagle.compiler.BeagleClass
import br.com.zup.beagle.compiler.CONTROLLER_REFERENCE
import br.com.zup.beagle.compiler.DEEP_LINK_HANDLER
import br.com.zup.beagle.compiler.DESIGN_SYSTEM
import br.com.zup.beagle.compiler.FORM_LOCAL_ACTION_HANDLER
import br.com.zup.beagle.compiler.HTTP_CLIENT_HANDLER
import br.com.zup.beagle.compiler.STORE_HANDLER
import br.com.zup.beagle.compiler.URL_BUILDER_HANDLER
import br.com.zup.beagle.compiler.VALIDATOR_HANDLER
import br.com.zup.beagle.compiler.error
import br.com.zup.beagle.compiler.extendsFromClass
import br.com.zup.beagle.compiler.implementsInterface
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class BeagleSetupPropertyGenerator(private val processingEnv: ProcessingEnvironment) {

    fun generate(
        basePackageName: String,
        roundEnvironment: RoundEnvironment
    ): List<PropertySpec> {
        val propertySpecifications: PropertySpecifications? = PropertySpecifications()

        roundEnvironment.getElementsAnnotatedWith(BeagleComponent::class.java).forEach { element ->
            val typeElement = element as TypeElement
            checkIfHandlersExists(typeElement, propertySpecifications)
            checkIfOtherAttributesExists(typeElement, propertySpecifications)
        }

        val registerValidatorAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(
            RegisterController::class.java
        )

        if (propertySpecifications?.defaultBeagleActivity == null && registerValidatorAnnotatedClasses.isEmpty()) {
            processingEnv.messager.error("Default Beagle Activity were not defined. " +
                "Did you miss to create your own Activity that extends" +
                " from Beagle Activity and annotate it with @RegisterController and without id?")
        }

        return createListOfPropertySpec(
            basePackageName,
            propertySpecifications
        )
    }

    private fun checkIfHandlersExists(
        typeElement: TypeElement,
        propertySpecifications: PropertySpecifications?
    ) {
        when {
            typeElement.implementsInterface(FORM_LOCAL_ACTION_HANDLER.toString()) -> {
                if (propertySpecifications?.formLocalActionHandler == null) {
                    propertySpecifications?.formLocalActionHandler = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "FormLocalActionHandler")
                }
            }
            typeElement.implementsInterface(DEEP_LINK_HANDLER.toString()) -> {
                if (propertySpecifications?.deepLinkHandler == null) {
                    propertySpecifications?.deepLinkHandler = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "DeepLinkHandler")
                }
            }
            typeElement.implementsInterface(HTTP_CLIENT_HANDLER.toString()) -> {
                if (propertySpecifications?.httpClient == null) {
                    propertySpecifications?.httpClient = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "HttpClient")
                }
            }
            typeElement.implementsInterface(STORE_HANDLER.toString()) -> {
                if (propertySpecifications?.storeHandler == null) {
                    propertySpecifications?.storeHandler = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "StoreHandler")
                }
            }
            typeElement.implementsInterface(URL_BUILDER_HANDLER.toString()) -> {
                if (propertySpecifications?.urlBuilder == null) {
                    propertySpecifications?.urlBuilder = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "UrlBuilder")
                }
            }
            typeElement.implementsInterface(BEAGLE_LOGGER.toString()) -> {
                if (propertySpecifications?.logger == null) {
                    propertySpecifications?.logger = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "BeagleLogger")
                }
            }
        }
    }

    private fun checkIfOtherAttributesExists(
        typeElement: TypeElement,
        propertySpecifications: PropertySpecifications?
    ) {
        when {
            typeElement.extendsFromClass(DESIGN_SYSTEM.toString()) -> {
                if (propertySpecifications?.designSystem == null) {
                    propertySpecifications?.designSystem = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "DesignSystem")
                }
            }
            typeElement.extendsFromClass(BEAGLE_ACTIVITY.toString()) -> {
                if (propertySpecifications?.defaultBeagleActivity == null) {
                    propertySpecifications?.defaultBeagleActivity = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "BeagleActivity")
                }
            }
            typeElement.implementsInterface(ANALYTICS.toString()) -> {
                if (propertySpecifications?.analytics == null) {
                    propertySpecifications?.analytics = typeElement
                } else {
                    logImplementationErrorMessage(typeElement, "Analytics")
                }
            }
        }
    }

    private fun logImplementationErrorMessage(typeElement: TypeElement, element: String) {
        processingEnv.messager.error(typeElement, "$element already " +
            "defined, remove one implementation from the application.")
    }

    private fun createListOfPropertySpec(
        basePackageName: String,
        propertySpecifications: PropertySpecifications?
    ): List<PropertySpec> {
        return listOf(
            implementProperty(
                propertySpecifications?.formLocalActionHandler.toString(),
                "formLocalActionHandler",
                FORM_LOCAL_ACTION_HANDLER
            ),
            implementProperty(
                propertySpecifications?.deepLinkHandler.toString(),
                "deepLinkHandler",
                DEEP_LINK_HANDLER
            ),
            implementProperty(
                propertySpecifications?.httpClient.toString(),
                "httpClient",
                HTTP_CLIENT_HANDLER
            ),
            implementProperty(
                propertySpecifications?.designSystem.toString(),
                "designSystem",
                DESIGN_SYSTEM
            ),
            implementProperty(
                propertySpecifications?.storeHandler.toString(),
                "storeHandler",
                STORE_HANDLER
            ),
            implementProperty(
                "$basePackageName.$VALIDATOR_HANDLER_IMPL_NAME",
                "validatorHandler",
                VALIDATOR_HANDLER
            ),
            implementProperty(
                propertySpecifications?.urlBuilder.toString(),
                "urlBuilder",
                URL_BUILDER_HANDLER
            ),
            implementProperty(
                propertySpecifications?.analytics.toString(),
                "analytics",
                ANALYTICS
            ),
            implementProperty(
                propertySpecifications?.logger.toString(),
                "logger",
                BEAGLE_LOGGER
            ),
            implementProperty(
                CONTROLLER_REFERENCE_GENERATED,
                "controllerReference",
                CONTROLLER_REFERENCE
            ),
            implementServerDrivenActivityProperty(propertySpecifications?.defaultBeagleActivity)
        )
    }

    private fun implementProperty(
        propertyValue: String,
        propertyName: String,
        propertyType: BeagleClass
    ): PropertySpec {

        val propertyValueIsNull = propertyValue == "null"
        val property = PropertySpec.builder(
            propertyName,
            ClassName(
                propertyType.packageName,
                propertyType.className
            ).copy(nullable = propertyValueIsNull),
            KModifier.OVERRIDE
        )

        val value = if (propertyValueIsNull) {
            propertyValue
        } else {
            "$propertyValue()"
        }

        property.initializer(value)

        return property.build()
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
    var designSystem: TypeElement? = null,
    var defaultBeagleActivity: TypeElement? = null,
    var beagleActivities: List<TypeElement>? = null,
    var urlBuilder: TypeElement? = null,
    var storeHandler: TypeElement? = null,
    var analytics: TypeElement? = null,
    var logger: TypeElement? = null
)
