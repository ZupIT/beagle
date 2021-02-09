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

package br.com.zup.beagle.newanalytics

import br.com.zup.beagle.core.BeagleJson

/**
 * This class is used to pass some properties for analytics
 * @param attributes by default is null, but can be a List of String to indicate what attributes of the action will be
 * reported. When this param is null, the analytics will get the attribute from AnalyticsConfig.
 * @param additionalEntries by default is null, but can be a Map of String to Any, on this param can be passed
 * additional values to be reported with the action.
 */
@BeagleJson(name = "actionAnalyticsProperties")
data class ActionAnalyticsProperties(
    var attributes: List<String>? = null,
    var additionalEntries: Map<String, Any>? = null,
)

/**
 * Class to represent the analytics used on ActionAnalytics
 * @property value is False, when the analytics is Disabled and can be null or have an ActionAnalyticsProperties
 * when is enabled
 */

@BeagleJson(name = "actionAnalyticsConfig")
sealed class ActionAnalyticsConfig(
    var value: Any? = null,
) {
    /**
     * Set the analytics to be disabled
     */
    @BeagleJson(name = "disabled")
    class Disabled : ActionAnalyticsConfig(false)

    /**
     * Set the analytics to be enabled
     * @param analytics can be null or ActionAnalyticsProperties, this attributes is used to create the analytics
     * for the actions, when this param is null, the analytics will get the attribute from AnalyticsConfig
     */
    @BeagleJson(name = "enabled")
    class Enabled(analytics: ActionAnalyticsProperties? = null) : ActionAnalyticsConfig(analytics)
}
