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

import android.view.View
import br.com.zup.beagle.R
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.putFirstCharacterOnLowerCase
import br.com.zup.beagle.android.widget.RootView
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

internal object ActionReportFactory {

    fun preGenerateActionAnalyticsConfig(
        rootView: RootView,
        origin: View,
        action: ActionAnalytics,
        analyticsValue: String? = null
    ) = DataActionReport(
        originX = origin.x,
        originY = origin.y,
        id = origin.getTag(R.id.beagle_component_id)?.toString(),
        type = origin.getTag(R.id.beagle_component_type)?.toString(),
        analyticsValue = analyticsValue,
        attributes = evaluateAllActionAttribute(
            value = action,
            rootView = rootView,
            origin = origin,
            action = action
        ),
        action = action,
        screenId = rootView.getScreenId(),
        actionType = getActionType(action)
    )

    private fun getActionType(action: Action): String =
        if (isCustomAction(action)) "custom:" + action::class.simpleName?.putFirstCharacterOnLowerCase()
        else "beagle:" + action::class.simpleName?.putFirstCharacterOnLowerCase()

    private fun isCustomAction(action: Action): Boolean =
        BeagleEnvironment.beagleSdk.registeredActions().contains(action::class.java)

    private fun evaluateAllActionAttribute(
        value: Any,
        name: String? = null,
        rootView: RootView,
        origin: View,
        action: ActionAnalytics
    ): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        (value::class as KClass<Any>).memberProperties.forEach { property ->
            property.get(value)?.let { it ->
                val keyName = getKeyName(name, property)
                val propertyValue = evaluateValueIfNecessary(it, rootView, origin, action)
                hashMap[keyName] = propertyValue
                try {
                    val result = evaluateAllActionAttribute(propertyValue, keyName, rootView, origin, action)
                    hashMap.putAll(result)

                } catch (e: Exception) {
                    BeagleMessageLogs.canNotGetPropertyValue(keyName)
                }

            }
        }
        return hashMap
    }

    private fun evaluateValueIfNecessary(
        value: Any,
        rootView: RootView,
        origin: View,
        action: ActionAnalytics
    ): Any {
        var propertyValue: Any = value
        if (propertyValue is Bind<*>) {
            action.evaluateExpression(rootView, origin, value)?.let {
                propertyValue = it
            }
        }
        return propertyValue
    }

    private fun getKeyName(name: String?, property: KProperty1<Any, *>): String {
        var nameResult = ""
        name?.let {
            nameResult = "$name."
        }
        nameResult += property.name
        return nameResult
    }

    fun generateActionAnalyticsConfig(
        dataActionReport: DataActionReport,
        actionAnalyticsConfig: ActionAnalyticsConfig
    ) = object : AnalyticsRecord {
        override val type: String
            get() = "action"
        override val platform: String
            get() = "android"
        override val attributes: HashMap<String, Any>
            get() = generateAttributes(dataActionReport, actionAnalyticsConfig)
    }

    private fun generateAttributes(
        dataActionReport: DataActionReport,
        actionAnalyticsConfig: ActionAnalyticsConfig
    ): HashMap<String, Any> {
        val hashMap: HashMap<String, Any> = HashMap()
        setScreenIdAttribute(dataActionReport.screenId, hashMap)
        dataActionReport.analyticsValue?.let {
            hashMap["event"] = it
        }
        actionAnalyticsConfig.attributes?.let {
            hashMap.putAll(
                generateAnalyticsConfigAttributesHashMap(
                    it,
                    dataActionReport.attributes
                )
            )
        }
        actionAnalyticsConfig.additionalEntries?.let {
            hashMap.putAll(it)
        }
        hashMap["beagleAction"] = dataActionReport.actionType
        hashMap["component"] = generateComponentHashMap(dataActionReport)
        return hashMap
    }

    private fun setScreenIdAttribute(screenId: String?, hashMap: HashMap<String, Any>) {
        if (screenId != null && screenId.isNotEmpty()) {
            hashMap["screen"] = screenId
        }
    }

    private fun generateComponentHashMap(dataActionReport: DataActionReport): HashMap<String, Any> {
        val hashMap: HashMap<String, Any> = HashMap()
        dataActionReport.id?.let { id ->
            hashMap["id"] = id
        }
        dataActionReport.type?.let { type ->
            hashMap["type"] = type
        }
        dataActionReport.originX?.let { x ->
            dataActionReport.originY?.let { y ->
                hashMap["position"] = hashMapOf("x" to x, "y" to y)
            }
        }
        return hashMap
    }

    private fun generateAnalyticsConfigAttributesHashMap(
        attributes: List<String>,
        attributeEvaluated: HashMap<String, Any>
    ): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        attributes.forEach { key ->
            val result = attributeEvaluated[key]
            result?.let { value ->
                hashMap[key] = value
            }
        }
        return hashMap
    }


}
