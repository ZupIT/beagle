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
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.widget.RootView
import java.util.*

internal object AnalyticsService {

    private val queueOfReportsWaitingConfig: Queue<DataReport> = LinkedList()
    private var analyticsProvider: AnalyticsProvider? = null
    private var queueSize: Int = 0
    private lateinit var analyticsConfig: AnalyticsConfig

    fun initialConfig(analyticsProvider: AnalyticsProvider? = null) {
        this.analyticsProvider = analyticsProvider
        analyticsProvider?.let {
            queueSize = it.getMaximumItemsInQueue()
            startSessionAndGetConfig(it)
        }
    }

    private fun startSessionAndGetConfig(analyticsProvider: AnalyticsProvider) {
        analyticsProvider.startSession {
            analyticsProvider.getConfig { analyticsConfig ->
                this@AnalyticsService.analyticsConfig = analyticsConfig
                reportElementsOnQueue()
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
        rootView: RootView,
        origin: View,
        action: ActionAnalytics,
        analyticsValue: String? = null
    ) {
        analyticsProvider?.let {
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue
            )
            if (isAnalyticsConfigInitialized()) {
                reportActionIfShould(dataActionReport)
            } else {
                addReportOnQueue(dataActionReport)
            }
        }
    }

    fun reportActionIfShould(dataActionReport: DataActionReport) {
        val config = createAConfigFromActionAnalyticsOrAnalyticsConfig(dataActionReport)
        if (shouldReport(config)) {
            analyticsProvider?.createRecord(ActionReportFactory.generateActionAnalyticsConfig(dataActionReport, config))
        }
    }

    private fun createAConfigFromActionAnalyticsOrAnalyticsConfig(
        dataActionReport: DataActionReport
    ): ActionAnalyticsConfig {
        dataActionReport.action.analytics?.let { actionAnalytics ->
            return ActionAnalyticsConfig(
                enable = actionAnalytics.enable,
                attributes = actionAnalytics.attributes,
                additionalEntries = actionAnalytics.additionalEntries)
        }
        return actionAnalyticsFromConfig(dataActionReport)
    }

    private fun actionAnalyticsFromConfig(dataActionReport: DataActionReport): ActionAnalyticsConfig {
        val key = dataActionReport.actionType
        val attributeList = analyticsConfig.actions?.get(key)
        return ActionAnalyticsConfig(enable = attributeList != null, attributes = attributeList)
    }

    private fun shouldReport(actionAnalyticsConfig: ActionAnalyticsConfig) = actionAnalyticsConfig.enable

    fun createScreenRecord(isLocalScreen: Boolean, screenIdentifier: String) {
        analyticsProvider?.let {
            val dataScreenReport = DataScreenReport(isLocalScreen, screenIdentifier)
            if (isAnalyticsConfigInitialized()) {
                reportScreen(dataScreenReport)
            } else {
                addReportOnQueue(dataScreenReport)
            }
        }
    }

    fun reportScreen(dataScreenReport: DataScreenReport) {
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

    private fun addReportOnQueue(dataReport: DataReport) {
        if (isNotQueueFull()) {
            queueOfReportsWaitingConfig.add(dataReport)
        } else {
            addItemOnFullQueue(dataReport)
        }
    }

    private fun isNotQueueFull() = queueOfReportsWaitingConfig.size < queueSize

    private fun addItemOnFullQueue(dataReport: DataReport) {
        BeagleMessageLogs.analyticsQueueIsFull(queueSize)
        queueOfReportsWaitingConfig.remove()
        queueOfReportsWaitingConfig.add(dataReport)
    }
}
