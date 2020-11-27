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
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

internal object ActionRecordFactory {

    fun preGenerateActionAnalyticsConfig(
        rootView: RootView,
        origin: View,
        action: ActionAnalytics,
        analyticsHandleEvent: AnalyticsHandleEvent? = null
    ) = DataActionReport(
        originX = origin.x,
        originY = origin.y,
        id = getComponentId(analyticsHandleEvent?.originComponent),
        type = getComponentType(analyticsHandleEvent?.originComponent),
        analyticsValue = analyticsHandleEvent?.analyticsValue,
        attributes = evaluateAllActionAttribute(
            value = action,
            rootView = rootView,
            origin = origin,
            action = action
        ),
        action = action,
        screenId = rootView.getScreenId()
    )


    private fun getComponentId(originComponent: ServerDrivenComponent?) = (originComponent as? IdentifierComponent)?.id

    private fun getComponentType(originComponent: ServerDrivenComponent?) = (originComponent as? WidgetView)?.beagleType

    private fun evaluateAllActionAttribute(
        value: Any,
        name: String? = null,
        rootView: RootView,
        origin: View,
        action: ActionAnalytics
    ): HashMap<String, Any> {
        var hashMap = HashMap<String, Any>()
        (value::class as KClass<Any>).memberProperties.forEach { property ->
            property.get(value)?.let { it ->
                try {
                    val propertyValue = evaluateValueIfNecessary(it, rootView, origin, action)
                    val keyName = getKeyName(name, property)
                    hashMap[keyName] = propertyValue
                    hashMap.putAll(evaluateAllActionAttribute(propertyValue, keyName, rootView, origin, action))

                } catch (e: Exception) {
                    BeagleMessageLogs.errorWhileTryingToGetPropertyValue(e)
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
        var nameResult: String = ""
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
        dataActionReport.screenId?.let {
            hashMap["screen"] = it
        }
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
        dataActionReport.action.type?.let { type ->
            hashMap["beagleAction"] = type
        }
        hashMap["component"] = generateComponentHashMap(dataActionReport)
        return hashMap
    }

    private fun generateComponentHashMap(dataActionReport2: DataActionReport): HashMap<String, Any> {
        val hashMap: HashMap<String, Any> = HashMap()
        dataActionReport2.id?.let { id ->
            hashMap["id"] = id
        }
        dataActionReport2.type?.let { type ->
            hashMap["type"] = type
        }
        dataActionReport2.originX?.let { x ->
            dataActionReport2.originY?.let { y ->
                hashMap["position"] = hashMapOf("x" to x, "y" to y)
            }
        }
        return hashMap
    }

    private fun generateAnalyticsConfigAttributesHashMap(
        attributes: List<String>,
        attributeEvaluated: HashMap<String, Any>
    ): HashMap<String, Any> {
        var hashMap = HashMap<String, Any>()
        attributes.forEach { key ->
            var result = attributeEvaluated[key]
            result?.let { value ->
                hashMap[key] = value
            }
        }
        return hashMap
    }

}
