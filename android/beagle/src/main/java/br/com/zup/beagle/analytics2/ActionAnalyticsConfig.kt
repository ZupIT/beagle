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

package br.com.zup.beagle.analytics2

/**
 * This class is used to config the report of an action directly on an action.
 * @param enable is a Boolean, if this param is true, the action will be reported, else, the action will not
 * be reported.
 * @param attributes is a List of String to indicate what attributes of the action will be reported.
 * @param additionalEntries is a Map of String to Any, on this param can be passed additional values to be reported
 * with the action.
*/
data class ActionAnalyticsConfig(
    var enable: Boolean = false,
    var attributes: List<String>? = null,
    var additionalEntries: Map<String, Any>? = null
)
