// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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
package br.com.zup.beagle.generated

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.analytics.TouchableAnalytics
import br.com.zup.beagle.android.context.Bind

/**
 * Define a button natively using the server driven information received through Beagle
 *
 * @property text define the button text content.
 * @property styleId reference a native style in your local styles file to be applied on this button.
 * @property onPress attribute to define action when onPress
 * @property enabled attribute to set enabled
 * @property clickAnalyticsEvent attribute to define click event name
 *
 */
interface Button : TouchableAnalytics  {
    val text: Bind<String>
    val styleId: String?
    val onPress: List<Action>?
    val enabled: Bind<Boolean>?
}