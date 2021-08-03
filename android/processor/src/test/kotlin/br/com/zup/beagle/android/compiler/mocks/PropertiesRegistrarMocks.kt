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

package br.com.zup.beagle.android.compiler.mocks

const val BEAGLE_PROPERTIES_IMPORTS =
    """
        import br.com.zup.beagle.android.action.FormLocalActionHandler
        import br.com.zup.beagle.android.navigation.DeepLinkHandler
        import br.com.zup.beagle.android.networking.HttpClient
        import br.com.zup.beagle.android.networking.HttpClientFactory
        import br.com.zup.beagle.android.setup.DesignSystem
        import br.com.zup.beagle.android.store.StoreHandler
        import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
        import br.com.zup.beagle.analytics.Analytics
        import br.com.zup.beagle.newanalytics.AnalyticsProvider
        import br.com.zup.beagle.android.logger.BeagleLogger
        import br.com.zup.beagle.android.imagedownloader.BeagleImageDownloader
    """

const val BEAGLE_PROPERTIES_SOURCE =
    BEAGLE_CONFIG_IMPORTS +
        BEAGLE_PROPERTIES_IMPORTS +
        VALID_SECOND_FORM_LOCAL_ACTION_HANDLER +
        VALID_SECOND_DEEP_LINK_HANDLER +
        VALID_SECOND_HTTP_CLIENT +
        VALID_SECOND_HTTP_CLIENT_FACTORY +
        VALID_SECOND_DESIGN_SYSTEM +
        VALID_SECOND_STORE_HANDLER +
        VALID_SECOND_URL_BUILDER +
        VALID_SECOND_ANALYTICS +
        VALID_SECOND_ANALYTICS_PROVIDER +
        VALID_SECOND_LOGGER +
        VALID_SECOND_IMAGE_DOWNLOAD +
        SIMPLE_BEAGLE_CONFIG

const val INTERNAL_PROPERTIES_REGISTRAR_EXPECTED =
    """
        package br.com.zup.beagle.android.registrar
        
        import kotlin.Pair
        import kotlin.String
        import kotlin.collections.List
        
        public final object PropertiesRegistrarTest {
          public fun registeredProperties(): List<Pair<String, String>> {
            val registeredProperties = listOf<Pair<String, String>>(
                Pair("formLocalActionHandler", ""${'"'}br.com.test.beagle.FormLocalActionHandlerTestTwo()""${'"'}),
                Pair("deepLinkHandler", ""${'"'}br.com.test.beagle.DeepLinkHandlerTestTwo()""${'"'}),
                Pair("httpClient", ""${'"'}br.com.test.beagle.HttpClientTestTwo()""${'"'}),
                Pair("httpClientFactory", ""${'"'}br.com.test.beagle.HttpClientFactoryTestTwo()""${'"'}),
                Pair("designSystem", ""${'"'}br.com.test.beagle.DesignSystemTestTwo()""${'"'}),
                Pair("storeHandler", ""${'"'}br.com.test.beagle.StoreHandlerTestTwo()""${'"'}),
                Pair("urlBuilder", ""${'"'}br.com.test.beagle.UrlBuilderTestTwo()""${'"'}),
                Pair("analytics", ""${'"'}br.com.test.beagle.AnalyticsTestTwo()""${'"'}),
                Pair("analyticsProvider", ""${'"'}br.com.test.beagle.AnalyticsProviderTestTwo()""${'"'}),
                Pair("logger", ""${'"'}br.com.test.beagle.LoggerTestTwo()""${'"'}),
                Pair("imageDownloader", ""${'"'}br.com.test.beagle.ImageDownloaderTestTwo()""${'"'}),
                Pair("config", ""${'"'}br.com.test.beagle.BeagleConfigImpl()""${'"'}),
        
            )
            return registeredProperties
          }
        }
    """