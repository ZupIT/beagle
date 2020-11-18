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

import br.com.zup.beagle.android.compiler.generatefunction.CONTROLLER_REFERENCE_GENERATED
import br.com.zup.beagle.android.compiler.PropertySpecifications
import br.com.zup.beagle.android.compiler.FORM_LOCAL_ACTION_HANDLER
import br.com.zup.beagle.android.compiler.DEEP_LINK_HANDLER
import br.com.zup.beagle.android.compiler.HTTP_CLIENT_HANDLER
import br.com.zup.beagle.android.compiler.STORE_HANDLER
import br.com.zup.beagle.android.compiler.URL_BUILDER_HANDLER
import br.com.zup.beagle.android.compiler.BEAGLE_LOGGER
import br.com.zup.beagle.android.compiler.BEAGLE_IMAGE_DOWNLOADER
import br.com.zup.beagle.android.compiler.DESIGN_SYSTEM
import br.com.zup.beagle.android.compiler.ANALYTICS
import br.com.zup.beagle.android.compiler.CONTROLLER_REFERENCE
import br.com.zup.beagle.compiler.shared.BeagleClass
import javax.lang.model.element.TypeElement

internal object PropertyImplementationManager {

    fun manage(propertySpecifications: PropertySpecifications?) =
        listOf(
            propertySpec(
                propertySpecifications?.formLocalActionHandler,
                FORM_LOCAL_ACTION_HANDLER
            ),
            propertySpec(
                propertySpecifications?.deepLinkHandler,
                DEEP_LINK_HANDLER
            ),
            propertySpec(
                propertySpecifications?.httpClient,
                HTTP_CLIENT_HANDLER
            ),
            propertySpec(
                propertySpecifications?.designSystem,
                DESIGN_SYSTEM
            ),
            propertySpec(
                propertySpecifications?.storeHandler,
                STORE_HANDLER
            ),
            propertySpec(
                propertySpecifications?.urlBuilder,
                URL_BUILDER_HANDLER
            ),
            propertySpec(
                propertySpecifications?.analytics,
                ANALYTICS
            ),
            propertySpec(
                propertySpecifications?.logger,
                BEAGLE_LOGGER,
                "logger"
            ),
            propertySpec(
                CONTROLLER_REFERENCE_GENERATED,
                CONTROLLER_REFERENCE,
                "controllerReference"
            ),
            propertySpec(
                propertySpecifications?.imageDownloader,
                BEAGLE_IMAGE_DOWNLOADER,
                "imageDownloader"
            )
        )

    private fun propertySpec(
        propertySpecificationsElement: TypeElement?,
        beagleClass: BeagleClass,
        customPropertyName: String? = null
    ) = propertySpec(
        propertySpecificationsElement.toString(),
        beagleClass,
        customPropertyName
    )

    private fun propertySpec(
        property: String,
        beagleClass: BeagleClass,
        customPropertyName: String? = null
    ) = GenericPropertyManagement(
        property,
        beagleClass
    ).getPropertySpec(customPropertyName)
}