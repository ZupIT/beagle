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

package br.com.zup.beagle.compiler

val FORM_LOCAL_ACTION_HANDLER = BeagleClass(
    "br.com.zup.beagle.android.action",
    "FormLocalActionHandler"
)
val DEEP_LINK_HANDLER = BeagleClass(
    "br.com.zup.beagle.android.navigation",
    "DeepLinkHandler"
)
val HTTP_CLIENT_HANDLER = BeagleClass(
    "br.com.zup.beagle.android.networking",
    "HttpClient"
)
val VALIDATOR_HANDLER = BeagleClass(
    "br.com.zup.beagle.android.components.form.core",
    "ValidatorHandler"
)
val VALIDATOR = BeagleClass(
    "br.com.zup.beagle.android.components.form.core",
    "Validator"
)
val DESIGN_SYSTEM = BeagleClass(
    "br.com.zup.beagle.android.setup",
    "DesignSystem"
)
val STORE_HANDLER = BeagleClass(
    "br.com.zup.beagle.android.store",
    "StoreHandler"
)
val BEAGLE_CONFIG = BeagleClass(
    "br.com.zup.beagle.android.setup",
    "BeagleConfig"
)
val BEAGLE_SDK = BeagleClass(
    "br.com.zup.beagle.android.setup",
    "BeagleSdk"
)
val WIDGET_VIEW = BeagleClass(
    "br.com.zup.beagle.android.widget",
    "WidgetView"
)
val BEAGLE_ACTIVITY = BeagleClass(
    "br.com.zup.beagle.android.view",
    "BeagleActivity"
)
val URL_BUILDER_HANDLER = BeagleClass(
    "br.com.zup.beagle.android.networking.urlbuilder",
    "UrlBuilder"
)
val ANALYTICS = BeagleClass(
    "br.com.zup.beagle.analytics",
    "Analytics"
)
val BEAGLE_LOGGER = BeagleClass(
    "br.com.zup.beagle.android.logger",
    "BeagleLogger"
)

val BINDING_ADAPTER = BeagleClass(
    "br.com.zup.beagle.android.setup",
    "BindingAdapter"
)

val BIND_ANDROID = BeagleClass(
    packageName = "br.com.zup.beagle.android.widget.core",
    className = "Bind"
)

val BIND_BACKEND = BeagleClass(
    packageName = "br.com.zup.beagle.core",
    className = "Bind"
)

val ANDROID_ACTION = BeagleClass(
    packageName = "br.com.zup.beagle.android.action",
    className = "Action"
)

val GET_VALUE_NULL = BeagleClass(
    packageName = "br.com.zup.beagle.android.utils",
    className = "getValueNull"
)

val GET_VALUE_NOT_NULL = BeagleClass(
    packageName = "br.com.zup.beagle.android.utils",
    className = "getValueNotNull"
)

val WIDGET = BeagleClass(
    packageName = "br.com.zup.beagle.widget",
    className = "Widget"
)

val INPUT_WIDGET = BeagleClass(
    packageName = "br.com.zup.beagle.widget.form",
    className = "InputWidget"
)

val ANDROID_VIEW = BeagleClass(
    packageName = "android.view",
    className = "View"
)

val ROOT_VIEW = BeagleClass(
    packageName = "br.com.zup.beagle.android.widget",
    className = "RootView"
)