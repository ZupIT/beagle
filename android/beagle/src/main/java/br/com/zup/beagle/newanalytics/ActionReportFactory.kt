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

import android.view.View
import br.com.zup.beagle.R
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.AnalyticsAction
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.data.serializer.createNamespace
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.annotation.RegisterAction
import br.com.zup.beagle.core.BeagleJson
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

internal object ActionReportFactory {

    fun generateDataActionReport(
        rootView: RootView,
        origin: View,
        action: AnalyticsAction,
        analyticsValue: String? = null,
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
            action = action,
            parameters = (action::class as KClass<AnalyticsAction>)
                .primaryConstructor?.parameters?.associateBy { it.name }
        ),
        action = action,
        screenId = rootView.getScreenId(),
        actionType = getActionType(action)
    )

    private fun getActionType(action: Action): String =
        if (isCustomAction(action)) getActionName("custom", action::class.java)
        else getActionName("beagle", action::class.java)

    private fun isCustomAction(action: Action): Boolean =
        BeagleEnvironment.beagleSdk.registeredActions().contains(action::class.java)

    private fun getActionName(appNameSpace: String, clazz: Class<*>): String {
        var name = ""
        clazz.getAnnotation(RegisterAction::class.java)?.let {
            name = it.name
        }
        if (name.isEmpty()) {
            clazz.getAnnotation(BeagleJson::class.java)?.let {
                name = it.name
            }
        }
        if (name.isEmpty()) {
            name = clazz.simpleName
        }
        return createNamespace(appNameSpace, clazz, name)

    }

    @Suppress("LongParameterList")
    private fun evaluateAllActionAttribute(
        value: Any,
        name: String? = null,
        rootView: RootView,
        origin: View,
        action: AnalyticsAction,
        parameters: Map<String?, KParameter>?,
    ): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        (value::class as KClass<Any>).memberProperties.forEach { property ->
            property.getPropertyValue(value)?.let { it ->
                val keyName = getKeyName(name, property, parameters)
                val propertyValue = evaluateValueIfNecessary(it, rootView, origin, action)
                hashMap[keyName] = propertyValue
                try {
                    val result = evaluateAllActionAttribute(
                        propertyValue,
                        keyName,
                        rootView,
                        origin,
                        action,
                        parameters
                    )
                    hashMap.putAll(result)

                } catch (e: Exception) {
                    BeagleMessageLogs.cannotGetPropertyValue(keyName)
                }

            }
        }
        return hashMap
    }

    //this fun is necessary because using the proguard, when try to get the value of a private field can throw
    //an exception
    private fun KProperty1<Any, *>.getPropertyValue(value: Any): Any? {
        return try {
            this.get(value)
        } catch (e: Exception) {
            null
        }
    }

    private fun evaluateValueIfNecessary(
        value: Any,
        rootView: RootView,
        origin: View,
        action: AnalyticsAction,
    ): Any {
        var propertyValue: Any = value
        if (propertyValue is Bind<*>) {
            action.evaluateExpression(rootView, origin, value)?.let {
                propertyValue = it
            }
        }
        return propertyValue
    }

    private fun getKeyName(
        name: String?,
        property: KProperty1<Any, *>,
        parameters: Map<String?, KParameter>?,
    ): String {
        var propertyName = parameters?.get(property.name)?.findAnnotation<BeagleJson>()?.name

        var nameResult = ""
        name?.let {
            nameResult = "$name."
        }
        if (propertyName.isNullOrEmpty()) {
            propertyName = property.name
        }
        nameResult += propertyName
        return nameResult
    }

    fun generateAnalyticsRecord(
        dataActionReport: DataActionReport,
    ) = AnalyticsRecord(
        type = "action",
        event = dataActionReport.analyticsValue,
        attributes = if (dataActionReport.attributes.size == 0) null else dataActionReport.attributes,
        additionalEntries = dataActionReport.additionalEntries,
        beagleAction = dataActionReport.actionType,
        component = generateComponentHashMap(dataActionReport),
        timestamp = dataActionReport.timestamp,
        screen = dataActionReport.screenId ?: "",
    )

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
}
