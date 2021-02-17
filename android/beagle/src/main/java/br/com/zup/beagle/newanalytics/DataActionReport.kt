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

import br.com.zup.beagle.android.action.AnalyticsAction

internal data class DataActionReport(
    var originX: Float? = null,
    var originY: Float? = null,
    var attributes: HashMap<String, Any>,
    var id: String? = null,
    var type: String? = null,
    var analyticsValue: String? = null,
    var action: AnalyticsAction,
    var screenId: String? = null,
    var actionType: String,
    var additionalEntries: Map<String, Any>? = null,
) : DataReport() {

    override fun report(analyticsConfig: AnalyticsConfig): AnalyticsRecord? {
        updateActionAttributes(analyticsConfig)
        if (shouldReport()) {
            return ActionReportFactory.generateAnalyticsRecord(this)
        }
        return null
    }

    private fun updateActionAttributes(analyticsConfig: AnalyticsConfig) {
        var attributes: List<String>? = null
        action.analytics?.let {
            val actionAnalyticsProperties = getActionProperties(it.value)
            updateAdditionalEntries(actionAnalyticsProperties)
            attributes = getAttributeOnActionAnalyticsProperties(actionAnalyticsProperties)
        }

        if (attributes == null) {
            attributes = getAttributeOnAnalyticsConfig(analyticsConfig)
        }
        updateAttributes(attributes)
    }

    private fun getActionProperties(value: Any?) =
        if (value != null && value is ActionAnalyticsProperties) value else null

    private fun getAttributeOnActionAnalyticsProperties(
        actionAnalyticsProperties: ActionAnalyticsProperties?,
    ): List<String>? {
        actionAnalyticsProperties?.let {
            return it.attributes
        }
        return null
    }

    private fun getAttributeOnAnalyticsConfig(
        analyticsConfig: AnalyticsConfig,
    ): List<String>? {
        val key = analyticsConfig.actions?.keys?.find {
            it.equals(actionType, ignoreCase = true)
        }
        return analyticsConfig.actions?.get(key)
    }

    private fun updateAttributes(
        expectedAttributes: List<String>?,
    ) {
        val actualAttributes = attributes
        attributes = hashMapOf()
        expectedAttributes?.forEach { key ->
            val result = actualAttributes[key]
            result?.let { value ->
                attributes[key] = value
            }
        }
    }

    private fun updateAdditionalEntries(actionAnalyticsProperties: ActionAnalyticsProperties?) {
        actionAnalyticsProperties?.let {
            additionalEntries = it.additionalEntries
        }
    }

    private fun shouldReport() = action.analytics != null || attributes.size > 0
}
