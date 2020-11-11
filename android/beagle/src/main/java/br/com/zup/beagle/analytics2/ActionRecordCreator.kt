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
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

object ActionRecordCreator {

    fun createRecord(
        rootView: RootView,
        origin: View,
        actionAnalyticsConfig: ActionAnalyticsConfig,
        action: ActionAnalytics,
        analyticsHandleEvent: AnalyticsHandleEvent?
    ) = object : AnalyticsRecord {
        override val type: String
            get() = "action"
        override val platform: String
            get() = "android"
        override val attributes: HashMap<String, Any>
            get() = generateAttributes(rootView, origin, actionAnalyticsConfig, action, analyticsHandleEvent)
    }

    fun generateAttributes(
        rootView: RootView,
        origin: View,
        actionAnalyticsConfig: ActionAnalyticsConfig,
        action: ActionAnalytics,
        analyticsHandleEvent: AnalyticsHandleEvent?
    ): HashMap<String, Any> {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["screen"] = rootView.getScreenId()
        analyticsHandleEvent?.analyticsValue?.let {
            hashMap["event"] = it
        }
        actionAnalyticsConfig.attributes?.let {
            hashMap.putAll(createAnalyticsConfigAttributesHashMap(it, rootView, origin, action))
        }
        actionAnalyticsConfig.additionalEntries?.let {
            hashMap.putAll(it)
        }
        action.type?.let { type ->
            hashMap["beagleAction"] = type
        }
        analyticsHandleEvent?.originComponent?.let{
            hashMap["component"] = createComponentHashMap(origin, it)
        }
        return hashMap
    }

    private fun createComponentHashMap(origin: View, originComponent : ServerDrivenComponent): HashMap<String, Any> {
        val hashMap: HashMap<String, Any> = HashMap()
        if (originComponent is IdentifierComponent) {
            originComponent.id?.let { componentId ->
                hashMap["id"] = componentId
            }
        }
        if (originComponent is Widget) {
            originComponent.beagleType?.let { type ->
                hashMap["type"] = type
            }
        }
        hashMap["position"] = hashMapOf( "x" to origin.x,"y" to origin.y)
        return hashMap
    }

    private fun createAnalyticsConfigAttributesHashMap(
        attributes: List<String>,
        rootView: RootView,
        origin: View,
        action: Action
    ): HashMap<String, Any> {
        val hashMap: HashMap<String, Any> = HashMap()
        val splitAttributesOfAttributes = attributes.map {
            it.split(".")
        }
        splitAttributesOfAttributes.let { simpleAttribute ->
            for ((position, value) in simpleAttribute.withIndex()) {
                attributes[position].let { key ->
                    hashMap[key] = getValue(rootView, origin, value, action)
                }
            }
        }
        return hashMap
    }

    private fun getValue(rootView: RootView, origin: View, attributes: List<String>, action: Action): Any {
        var cnt = 0
        var value: Any = action
        do {
            value = getPropertyValue(value, attributes, cnt)
            cnt++
        } while (cnt < attributes.size)

        return evaluateValueIfNecessary(rootView, origin, value, action)
    }

    private fun getPropertyValue(value: Any, attributes: List<String>, cnt: Int): Any {
        (value::class as KClass<Any>).memberProperties.forEach { property ->
            if (property.name == attributes[cnt]) {
                property.get(value)?.let { propertyValue ->
                    return propertyValue
                }
            }
        }
        return value
    }

    private fun evaluateValueIfNecessary(rootView: RootView, origin: View, value: Any, action: Action): Any {
        if (value is Bind<*>) {
            action.evaluateExpression(rootView, origin, value)?.let {
                return it
            }
        }
        return value
    }


}