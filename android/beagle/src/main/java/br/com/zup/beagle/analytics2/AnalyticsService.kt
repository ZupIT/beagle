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

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.setup.BeagleEnvironment
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class AnalyticsService(private val analyticsProvider: AnalyticsProvider? = null) {

    private lateinit var analyticsConfig: AnalyticsConfig

    init {
        analyticsProvider?.let { analyticsProvider ->
            analyticsProvider.startSession {
                analyticsProvider.getConfig { analyticsConfig ->
                    this.analyticsConfig = analyticsConfig
                }
            }
        }
    }

    fun createActionRecord(action: Action) {
        analyticsProvider?.let { analyticsProvider ->
            generateActionReport(getActionAnalytics(action), action, analyticsProvider)
        }
    }

    private fun generateActionReport(actionAnalyticsConfig: ActionAnalyticsConfig, action: Action, analyticsProvider : AnalyticsProvider) {
        if (shouldReport(actionAnalyticsConfig)) {
            analyticsProvider.createRecord(object : AnalyticsRecord {
                override val type: String
                    get() = "action"
                override val platform: String
                    get() = "android"
                override val attributes: HashMap<String, Any>
                    get() = generateAttributes(actionAnalyticsConfig, action)
            })
        }
    }

    private fun shouldReport(actionAnalyticsConfig: ActionAnalyticsConfig) = actionAnalyticsConfig.enable

    private fun getActionAnalytics(action: Action): ActionAnalyticsConfig {
        action.analytics?.let { actionAnalytics ->
            return ActionAnalyticsConfig(
                enable = actionAnalytics.enable,
                attributes = actionAnalytics.attributes,
                additionalEntries = actionAnalytics.additionalEntries)
        }
        return actionAnalyticsFromConfig(action)
    }

    private fun actionAnalyticsFromConfig(action: Action): ActionAnalyticsConfig {
        val key = createKey(action)
        val attributeList = analyticsConfig.actions[key]
        return ActionAnalyticsConfig(enable = attributeList != null, attributes = attributeList)
    }

    private fun generateAttributes(actionAnalyticsConfig: ActionAnalyticsConfig, action: Action): HashMap<String, Any>{
        val hashMap : HashMap<String, Any> = HashMap()
        actionAnalyticsConfig.attributes?.forEach{
            (action::class as KClass<Action>).memberProperties.forEach { property ->

                val needToReport = actionAnalyticsConfig.attributes?.contains(property.name) ?: false
                if (needToReport){
                    val value = property.get(action)
                    value?.let{

                        hashMap[property.name] = value
                    }
                }

            }
        }
        actionAnalyticsConfig.additionalEntries?.let {
            hashMap.putAll(it)
        }
        return hashMap
    }

    private fun createKey(action: Action): String =
        if (isCustom(action)) "custom:" + action::class.simpleName else "beagle:" + action::class.simpleName

    fun isCustom(action: Action): Boolean =
        BeagleEnvironment.beagleSdk.registeredActions().contains(action::class.java)

    fun createScreenRecord(attribute: HashMap<String, Any>) {
        analyticsProvider?.let { analyticsProvider ->
            if (this::analyticsConfig.isInitialized) {
                val screen = analyticsConfig.enableScreenAnalytics ?: false
                if (screen) {
                    analyticsProvider.createRecord(createScreenRecordReturn(attribute))
                }
            }
        }
    }

    private fun createScreenRecordReturn(attribute: HashMap<String, Any>) = object : AnalyticsRecord {
        override val type: String
            get() = "screen"
        override val platform: String
            get() = "android"
        override val attributes: HashMap<String, Any>
            get() = attribute
    }
}