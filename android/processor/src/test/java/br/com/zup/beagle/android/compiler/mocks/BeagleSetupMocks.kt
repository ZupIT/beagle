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

const val COMPLETE_BEAGLE_CUSTOM_CLASS =
    """
        package br.com.test.beagle
        
        import br.com.zup.beagle.android.annotation.BeagleComponent
        import br.com.zup.beagle.android.setup.BeagleConfig
        import br.com.zup.beagle.android.annotation.RegisterController
        import br.com.zup.beagle.android.view.BeagleActivity
        import br.com.zup.beagle.analytics.Analytics
        import br.com.zup.beagle.analytics2.AnalyticsProvider
        import br.com.zup.beagle.annotation.RegisterAction
        import br.com.zup.beagle.android.action.Action
        import br.com.zup.beagle.android.annotation.RegisterBeagleAdapter
        import br.com.zup.beagle.android.data.serializer.adapter.generic.BeagleTypeAdapter
        import br.com.zup.beagle.android.annotation.RegisterValidator
        import br.com.zup.beagle.android.components.form.core.Validator
        import br.com.zup.beagle.android.navigation.DeepLinkHandler
        import br.com.zup.beagle.android.setup.DesignSystem
        import br.com.zup.beagle.android.action.FormLocalActionHandler
        import br.com.zup.beagle.android.networking.HttpClient
        import br.com.zup.beagle.android.imagedownloader.BeagleImageDownloader
        import br.com.zup.beagle.android.logger.BeagleLogger
        import br.com.zup.beagle.annotation.RegisterOperation
        import br.com.zup.beagle.android.operation.Operation
        import br.com.zup.beagle.android.store.StoreHandler
        import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
        import br.com.zup.beagle.annotation.RegisterWidget
        import br.com.zup.beagle.android.widget.WidgetView
        
        @BeagleComponent
        class BeagleConfigImpl: BeagleConfig { }
        
        @BeagleComponent
        class AnalyticsTest: Analytics { }
        
        @BeagleComponent
        class AnalyticsProviderTest: AnalyticsProvider { }
        
        @RegisterAction
        class ActionTest: Action { }
        
        @RegisterController("otherController")
        class AppBeagleActivity : BeagleActivity 
    
        interface Person
        
        @RegisterBeagleAdapter
        class PersonAdapter : BeagleTypeAdapter<Person> {
        
            override fun fromJson(json: String): Person = object: Person {}
        
            override fun toJson(type: Person): String  = ""
        }
        
        @RegisterValidator("text-is-not-blank")
        class TextNotBlankValidator : Validator<String, String> {
            override fun isValid(input: String, widget: String): Boolean {
                return !input.isBlank()
            }
        }
        
        @BeagleComponent
        class DeepLinkHandlerTest: DeepLinkHandler { }
        
        @BeagleComponent
        class DesignSystemTest: DesignSystem { }
        
        @BeagleComponent
        class FormLocalActionHandlerTest: FormLocalActionHandler { }
        
        @BeagleComponent
        class HttpClientTest: HttpClient { }
        
        @BeagleComponent
        class ImageDownloaderTest: BeagleImageDownloader { }
        
        @BeagleComponent
        class LoggerTest: BeagleLogger { }
        
        @RegisterOperation("OperationTestName")
        class OperationTest: Operation { }
        
        @BeagleComponent
        class StoreHandlerTest: StoreHandler { }
        
        @BeagleComponent
        class UrlBuilderTest: UrlBuilder { }
        
        @RegisterWidget
        class TextTest: WidgetView() { }
    """

const val BEAGLE_SETUP_COMPLETE =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST")

        package br.com.test.beagle
        
        import br.com.test.beagle.BeagleConfigImpl
        import br.com.zup.beagle.analytics.Analytics
        import br.com.zup.beagle.analytics2.AnalyticsProvider
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
          public override val formLocalActionHandler: FormLocalActionHandler =
              br.com.test.beagle.FormLocalActionHandlerTest()
        
          public override val deepLinkHandler: DeepLinkHandler = br.com.test.beagle.DeepLinkHandlerTest()
        
          public override val httpClient: HttpClient = br.com.test.beagle.HttpClientTest()
        
          public override val designSystem: DesignSystem = br.com.test.beagle.DesignSystemTest()
        
          public override val storeHandler: StoreHandler = br.com.test.beagle.StoreHandlerTest()
        
          public override val urlBuilder: UrlBuilder = br.com.test.beagle.UrlBuilderTest()
        
          public override val analytics: Analytics = br.com.test.beagle.AnalyticsTest()

          public override val analyticsProvider: AnalyticsProvider = br.com.test.beagle.AnalyticsProviderTest()

          public override val logger: BeagleLogger = br.com.test.beagle.LoggerTest()
        
          public override val controllerReference: BeagleControllerReference =
              ControllerReferenceGenerated()
        
          public override val imageDownloader: BeagleImageDownloader =
              br.com.test.beagle.ImageDownloaderTest()
        
          public override val serverDrivenActivity: Class<BeagleActivity> =
              br.com.zup.beagle.android.view.ServerDrivenActivity::class.java as Class<BeagleActivity>
        
          public override val config: BeagleConfig = BeagleConfigImpl()
        
          public override val typeAdapterResolver: TypeAdapterResolver = RegisteredCustomTypeAdapter
        
          public override val validatorHandler: ValidatorHandler = RegisteredCustomValidator
        
          public override fun registeredWidgets(): List<Class<WidgetView>> =
              RegisteredWidgets.registeredWidgets()
        
          public override fun registeredOperations(): Map<String, Operation> =
              RegisteredOperations.registeredOperations()
        
          public override fun registeredActions(): List<Class<Action>> =
              RegisteredActions.registeredActions()
        }

    """