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

import br.com.zup.beagle.android.action.ActionAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

object AnalyticsService {

    private val queueOfReportsWaitingConfig: Queue<DataReport> = LinkedList()
    private var analyticsProvider: AnalyticsProvider? = null

    private lateinit var analyticsConfig: AnalyticsConfig

    fun initialConfig(analyticsProvider: AnalyticsProvider? = null, coroutineScope: CoroutineScope) {
        this.analyticsProvider = analyticsProvider
        startSessionAndGetConfig(coroutineScope)


    }

    private fun startSessionAndGetConfig(coroutineScope: CoroutineScope) {
        analyticsProvider?.let { analyticsProvider ->
            analyticsProvider.startSession {
                coroutineScope.launch {
                    async {
                        analyticsProvider.getConfig { analyticsConfig ->
                            this@AnalyticsService.analyticsConfig = analyticsConfig
                        }
                    }.await()
                    reportElementsOnQueue()
                }
            }
        }
    }

    private fun reportElementsOnQueue() {
        while (queueIsNotEmpty()) {
            queueOfReportsWaitingConfig.remove().report()
        }
    }

    private fun queueIsNotEmpty() = !queueOfReportsWaitingConfig.isEmpty()

    fun createActionRecord(
        dataActionReport: DataActionReport
    ) {
        if (isAnalyticsConfigInitialized()) {
            reportActionIfShould(dataActionReport)
        } else {
            queueOfReportsWaitingConfig.add(dataActionReport)
        }
    }

    private fun reportActionIfShould(dataActionReport: DataActionReport){
        val config = createAConfigFromActionAnalyticsOrAnalyticsConfig(dataActionReport.action)
        if (shouldReport(config)) {
            reportAction(dataActionReport, config)
        }
    }

    private fun createAConfigFromActionAnalyticsOrAnalyticsConfig(action: ActionAnalytics): ActionAnalyticsConfig {
        action.analytics?.let { actionAnalytics ->
            return ActionAnalyticsConfig(
                enable = actionAnalytics.enable,
                attributes = actionAnalytics.attributes,
                additionalEntries = actionAnalytics.additionalEntries)
        }
        return actionAnalyticsFromConfig(action)
    }

    private fun actionAnalyticsFromConfig(action: ActionAnalytics): ActionAnalyticsConfig {
        val key = action.type
        val attributeList = analyticsConfig.actions[key]
        return ActionAnalyticsConfig(enable = attributeList != null, attributes = attributeList)
    }

    private fun shouldReport(actionAnalyticsConfig: ActionAnalyticsConfig) = actionAnalyticsConfig.enable

    private fun reportAction(
        dataActionReport: DataActionReport,
        actionAnalyticsConfig: ActionAnalyticsConfig
    ) {
        analyticsProvider?.createRecord(
            ActionRecordFactory.generateActionAnalyticsConfig(dataActionReport, actionAnalyticsConfig)
        )
    }

    fun createScreenRecord(dataScreenReport: DataScreenReport) {
        if (isAnalyticsConfigInitialized()) {
            reportScreen(dataScreenReport)
        } else {
            queueOfReportsWaitingConfig.add(dataScreenReport)
        }
    }

    private fun reportScreen(dataScreenReport: DataScreenReport) {
        if (shouldReportScreen()) {
            val screenIdentifier = dataScreenReport.screenIdentifier
            if (dataScreenReport.isLocalScreen) {
                analyticsProvider?.createRecord(
                    ScreenReportFactory.generateLocalScreenAnalyticsRecord(screenIdentifier)
                )
            } else {
                analyticsProvider?.createRecord(
                    ScreenReportFactory.generateRemoteScreenAnalyticsRecord(screenIdentifier)
                )
            }
        }
    }

    private fun isAnalyticsConfigInitialized() = this::analyticsConfig.isInitialized

    private fun shouldReportScreen() = analyticsConfig.enableScreenAnalytics ?: false

}
