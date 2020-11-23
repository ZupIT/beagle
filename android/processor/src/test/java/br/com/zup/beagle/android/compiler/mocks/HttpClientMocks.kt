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

const val VALID_HTTP_CLIENT =
    """
        import br.com.zup.beagle.android.networking.HttpClient

        @BeagleComponent
        class HttpClientTest: HttpClient { }
    """

const val VALID_SECOND_HTTP_CLIENT =
    """
        @BeagleComponent
        class HttpClientTestTwo: HttpClient { }
    """

const val LIST_OF_HTTP_CLIENT = VALID_HTTP_CLIENT + VALID_SECOND_HTTP_CLIENT

const val VALID_HTTP_CLIENT_BEAGLE_SDK =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST")
        
        package br.com.test.beagle
        
        import br.com.test.beagle.BeagleConfigImpl
        import br.com.zup.beagle.analytics.Analytics
        import br.com.zup.beagle.android.`data`.serializer.adapter.generic.TypeAdapterResolver
        import br.com.zup.beagle.android.action.Action
        import br.com.zup.beagle.android.action.FormLocalActionHandler
        import br.com.zup.beagle.android.components.form.core.ValidatorHandler
        import br.com.zup.beagle.android.imagedownloader.BeagleImageDownloader
        import br.com.zup.beagle.android.logger.BeagleLogger
        import br.com.zup.beagle.android.navigation.BeagleControllerReference
        import br.com.zup.beagle.android.navigation.DeepLinkHandler
        import br.com.zup.beagle.android.networking.HttpClient
        import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
        import br.com.zup.beagle.android.operation.Operation
        import br.com.zup.beagle.android.setup.BeagleConfig
        import br.com.zup.beagle.android.setup.BeagleSdk
        import br.com.zup.beagle.android.setup.DesignSystem
        import br.com.zup.beagle.android.store.StoreHandler
        import br.com.zup.beagle.android.view.BeagleActivity
        import br.com.zup.beagle.android.widget.WidgetView
        import java.lang.Class
        import kotlin.String
        import kotlin.Suppress
        import kotlin.collections.List
        import kotlin.collections.Map
        
        public final class BeagleSetup : BeagleSdk { 
        
            public override val formLocalActionHandler : FormLocalActionHandler? = null 
            
            public override val deepLinkHandler : DeepLinkHandler? = null
            
            public override val httpClient : HttpClient = br.com.test.beagle.HttpClientTest()

            public override val designSystem : DesignSystem? = null

            public override val storeHandler : StoreHandler? = null

            public override val urlBuilder : UrlBuilder? = null

            public override val analytics : Analytics? = null

            public override val logger : BeagleLogger? = null

            public override val controllerReference : BeagleControllerReference = ControllerReferenceGenerated()

            public override val imageDownloader : BeagleImageDownloader? = null

            public override val serverDrivenActivity : Class<BeagleActivity> = 
                br.com.zup.beagle.android.view.ServerDrivenActivity::class.java as Class<BeagleActivity>

            public override val config : BeagleConfig = BeagleConfigImpl()
            
            public override val typeAdapterResolver : TypeAdapterResolver = RegisteredCustomTypeAdapter
            
            public override val validatorHandler : ValidatorHandler = RegisteredCustomValidator

            public override fun registeredWidgets() : List<Class<WidgetView>> = RegisteredWidgets.registeredWidgets()

            public override fun registeredOperations() : Map<String, Operation> = 
                RegisteredOperations.registeredOperations()
            
            public override fun registeredActions() : List<Class<Action>> = RegisteredActions.registeredActions()
        }
    """