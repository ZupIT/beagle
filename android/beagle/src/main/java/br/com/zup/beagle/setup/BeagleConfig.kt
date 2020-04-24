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

package br.com.zup.beagle.setup

import android.app.Application
import android.util.LruCache
import br.com.zup.beagle.action.CustomActionHandler
import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.expression.Binding
import br.com.zup.beagle.expression.cache.CacheProvider
import br.com.zup.beagle.expression.config.BindingSetup
import br.com.zup.beagle.expression.config.BindingSetup.BindingConfig
import br.com.zup.beagle.form.ValidatorHandler
import br.com.zup.beagle.navigation.DeepLinkHandler
import br.com.zup.beagle.networking.HttpClient
import br.com.zup.beagle.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.widget.core.WidgetView
import com.facebook.soloader.SoLoader

enum class Environment {
    DEBUG,
    PRODUCTION
}

internal object BeagleEnvironment {
    lateinit var beagleSdk: BeagleSdk
    lateinit var application: Application
}

interface BeagleSdk {
    val config: BeagleConfig
    val customActionHandler: CustomActionHandler?
    val deepLinkHandler: DeepLinkHandler?
    val validatorHandler: ValidatorHandler?
    val httpClient: HttpClient?
    val designSystem: DesignSystem?
    val serverDrivenActivity: Class<BeagleActivity>
    val urlBuilder: UrlBuilder?
    val analytics: Analytics?

    fun registeredWidgets(): List<Class<WidgetView>>

    fun init(application: Application) {
        BeagleEnvironment.beagleSdk = this
        BeagleEnvironment.application = application
        SoLoader.init(application, false)
        BindingSetup.bindingConfig = createBindingConfig()
    }

    fun createBindingConfig(): BindingConfig {
        return object : BindingConfig {
            private val cache = LruCache<String, Binding>(64)
            override val cacheProvider: CacheProvider<String, Binding> = object : CacheProvider<String, Binding> {
                override fun get(key: String): Binding? =
                    cache.get(key)

                override fun put(key: String, value: Binding) {
                    cache.put(key, value)
                }
            }
        }
    }
}

interface BeagleConfig {
    val environment: Environment
    val baseUrl: String
}
