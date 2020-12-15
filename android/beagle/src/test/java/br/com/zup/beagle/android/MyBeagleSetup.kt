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

package br.com.zup.beagle.android

import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.analytics2.AnalyticsProvider
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.FormLocalActionHandler
import br.com.zup.beagle.android.components.form.core.ValidatorHandler
import br.com.zup.beagle.android.data.serializer.adapter.generic.TypeAdapterResolver
import br.com.zup.beagle.android.imagedownloader.BeagleImageDownloader
import br.com.zup.beagle.android.logger.BeagleLogger
import br.com.zup.beagle.android.navigation.BeagleControllerReference
import br.com.zup.beagle.android.navigation.DeepLinkHandler
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.operation.Operation
import br.com.zup.beagle.android.setup.*
import br.com.zup.beagle.android.store.StoreHandler
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenActivity
import br.com.zup.beagle.android.widget.WidgetView

class MyBeagleSetup(override val analyticsProvider: AnalyticsProvider? = null) : BeagleSdk {

    override val config: BeagleConfig = object : BeagleConfig {
        override val environment: Environment = Environment.DEBUG
        override val baseUrl: String = ""
        override val cache: Cache = Cache(enabled = false, maxAge = 0)

        override val isLoggingEnabled: Boolean = true

    }

    override val formLocalActionHandler: FormLocalActionHandler? = null

    override val deepLinkHandler: DeepLinkHandler? = null

    override val validatorHandler: ValidatorHandler? = null

    override val httpClient: HttpClient? = null

    override val designSystem: DesignSystem? = null

    override val imageDownloader: BeagleImageDownloader? = null

    override val storeHandler: StoreHandler? = null

    override val controllerReference: BeagleControllerReference? = null

    override val typeAdapterResolver: TypeAdapterResolver? = null

    override val serverDrivenActivity: Class<BeagleActivity> =
        ServerDrivenActivity::class.java as Class<BeagleActivity>

    override val urlBuilder: UrlBuilder? = null

    override val analytics: Analytics? = null

    override val logger: BeagleLogger? = null

    override fun registeredWidgets(): List<Class<WidgetView>> = emptyList()

    override fun registeredActions(): List<Class<Action>> = emptyList()

    override fun registeredOperations(): Map<String, Operation> = emptyMap()
}