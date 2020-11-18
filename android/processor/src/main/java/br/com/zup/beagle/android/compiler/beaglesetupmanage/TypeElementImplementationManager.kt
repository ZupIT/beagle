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

package br.com.zup.beagle.android.compiler.beaglesetupmanage

import br.com.zup.beagle.android.compiler.PropertySpecifications
import br.com.zup.beagle.android.compiler.FORM_LOCAL_ACTION_HANDLER
import br.com.zup.beagle.android.compiler.DEEP_LINK_HANDLER
import br.com.zup.beagle.android.compiler.HTTP_CLIENT_HANDLER
import br.com.zup.beagle.android.compiler.STORE_HANDLER
import br.com.zup.beagle.android.compiler.URL_BUILDER_HANDLER
import br.com.zup.beagle.android.compiler.BEAGLE_LOGGER
import br.com.zup.beagle.android.compiler.BEAGLE_IMAGE_DOWNLOADER
import br.com.zup.beagle.compiler.shared.implements
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

internal object TypeElementImplementationManager {

    fun manage(
        processingEnv: ProcessingEnvironment,
        typeElement: TypeElement,
        propertySpecifications: PropertySpecifications?,
    ) {
        val manage = GenericTypeElementManagement(
            processingEnv,
            typeElement
        )

        when {
            typeElement.implements(FORM_LOCAL_ACTION_HANDLER, processingEnv) -> {
                val element = propertySpecifications?.formLocalActionHandler
                propertySpecifications?.formLocalActionHandler = manage.manageTypeElement(
                    element,
                    FORM_LOCAL_ACTION_HANDLER.className
                )
            }
            typeElement.implements(DEEP_LINK_HANDLER, processingEnv) -> {
                val element = propertySpecifications?.deepLinkHandler
                propertySpecifications?.deepLinkHandler = manage.manageTypeElement(element, DEEP_LINK_HANDLER.className)
            }
            typeElement.implements(HTTP_CLIENT_HANDLER, processingEnv) -> {
                val element = propertySpecifications?.httpClient
                propertySpecifications?.httpClient = manage.manageTypeElement(element, HTTP_CLIENT_HANDLER.className)
            }
            typeElement.implements(STORE_HANDLER, processingEnv) -> {
                val element = propertySpecifications?.storeHandler
                propertySpecifications?.storeHandler = manage.manageTypeElement(element, STORE_HANDLER.className)
            }
            typeElement.implements(URL_BUILDER_HANDLER, processingEnv) -> {
                val element = propertySpecifications?.urlBuilder
                propertySpecifications?.urlBuilder = manage.manageTypeElement(element, URL_BUILDER_HANDLER.className)
            }
            typeElement.implements(BEAGLE_LOGGER, processingEnv) -> {
                val element = propertySpecifications?.logger
                propertySpecifications?.logger = manage.manageTypeElement(element, BEAGLE_LOGGER.className)
            }
            typeElement.implements(BEAGLE_IMAGE_DOWNLOADER, processingEnv) -> {
                val element = propertySpecifications?.imageDownloader
                propertySpecifications?.imageDownloader = manage.manageTypeElement(
                    element,
                    BEAGLE_IMAGE_DOWNLOADER.className
                )
            }
        }
    }
}