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
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.widget.RootView
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

internal object AnalyticsService {

    private val queueOfReportsWaitingConfig: Queue<DataReport> = ConcurrentLinkedQueue()
    private val isQueueReported = AtomicBoolean(false)

    private fun queueIsNotEmpty() = !queueOfReportsWaitingConfig.isEmpty()

    fun createActionRecord(
        rootView: RootView,
        origin: View,
        action: ActionAnalytics,
        analyticsValue: String? = null
    ) {
        action.analytics?.let{
            if(it is ActionAnalyticsConfig.Disabled){
                return
            }
        }
        val analyticsProvider = BeagleEnvironment.beagleSdk.analyticsProvider
        analyticsProvider?.let {
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue
            )
            reportDataReport(dataActionReport, it)
        }
    }

    fun createScreenRecord(isLocalScreen: Boolean, screenIdentifier: String) {
        val analyticsProvider: AnalyticsProvider? = BeagleEnvironment.beagleSdk.analyticsProvider
        analyticsProvider?.let {
            val dataScreenReport = DataScreenReport(isLocalScreen, screenIdentifier)
            reportDataReport(dataScreenReport, it)
        }
    }

    private fun reportDataReport(dataReport: DataReport, analyticsProvider: AnalyticsProvider) {
        val analyticsConfig = analyticsProvider.getConfig()
        val queueSize = analyticsProvider.getMaximumItemsInQueue()
        if (analyticsConfig == null) {
            reportWithConfigNull(dataReport, queueSize)
        } else {
            reportWithConfigNotNull(dataReport, analyticsConfig, analyticsProvider)
        }
    }

    private fun reportWithConfigNull(dataReport: DataReport, queueSize: Int) {
        addReportOnQueue(dataReport, queueSize)
        isQueueReported.set(false)
    }

    private fun addReportOnQueue(dataReport: DataReport, queueSize: Int) {
        if (isNotQueueFull(queueSize)) {
            queueOfReportsWaitingConfig.add(dataReport)
        } else {
            addItemOnFullQueue(dataReport, queueSize)
        }
    }

    private fun isNotQueueFull(queueSize : Int) = queueOfReportsWaitingConfig.size < queueSize

    private fun addItemOnFullQueue(dataReport: DataReport, queueSize: Int) {
        BeagleMessageLogs.analyticsQueueIsFull(queueSize)
        queueOfReportsWaitingConfig.remove()
        queueOfReportsWaitingConfig.add(dataReport)
    }

    private fun reportWithConfigNotNull(
        dataReport: DataReport,
        analyticsConfig: AnalyticsConfig,
        analyticsProvider: AnalyticsProvider
    ) {
        val report = dataReport.report(analyticsConfig)
        report?.let {
            analyticsProvider.createRecord(report)
        }
        if (!isQueueReported.get()) {
            isQueueReported.set(true)
            reportElementsOnQueue(analyticsConfig, analyticsProvider)
        }
    }

    private fun reportElementsOnQueue(analyticsConfig: AnalyticsConfig, analyticsProvider: AnalyticsProvider) {
        while (queueIsNotEmpty()) {
            val report = queueOfReportsWaitingConfig.poll()?.report(analyticsConfig)
            report?.let {
                analyticsProvider.createRecord(it)
            }
        }
    }
}
