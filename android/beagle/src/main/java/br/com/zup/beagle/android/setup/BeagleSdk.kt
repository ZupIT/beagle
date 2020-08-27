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

package br.com.zup.beagle.android.setup

import android.app.Application
import androidx.annotation.VisibleForTesting
import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.FormLocalActionHandler
import br.com.zup.beagle.android.components.form.core.ValidatorHandler
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.logger.BeagleLogger
import br.com.zup.beagle.android.navigation.BeagleControllerReference
import br.com.zup.beagle.android.navigation.DeepLinkHandler
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.store.StoreHandler
import br.com.zup.beagle.android.utils.NewIntentDeprecatedConstants
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import com.facebook.soloader.SoLoader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface BeagleSdk {
    val config: BeagleConfig
    val formLocalActionHandler: FormLocalActionHandler?
    val deepLinkHandler: DeepLinkHandler?
    val validatorHandler: ValidatorHandler?
    val httpClient: HttpClient?
    val designSystem: DesignSystem?
    val storeHandler: StoreHandler?
    val controllerReference: BeagleControllerReference?

    @Deprecated(NewIntentDeprecatedConstants.BEAGLE_ACTIVITY_COMPONENT)
    val serverDrivenActivity: Class<BeagleActivity>
    val urlBuilder: UrlBuilder?
    val analytics: Analytics?
    val logger: BeagleLogger?

    fun registeredWidgets(): List<Class<WidgetView>>
    fun registeredActions(): List<Class<Action>>

    fun init(application: Application) {
        BeagleEnvironment.beagleSdk = this
        BeagleEnvironment.application = application
        SoLoader.init(application, false)
        GlobalScope.launch {
            BeagleMoshi.moshi.adapter(ServerDrivenComponent::class.java)
        }
    }

    companion object {
        @VisibleForTesting
        fun setInTestMode() {
            SoLoader.setInTestMode()
        }

        @VisibleForTesting
        fun deinitForTest() {
            SoLoader.deinitForTest()
        }
    }
}
