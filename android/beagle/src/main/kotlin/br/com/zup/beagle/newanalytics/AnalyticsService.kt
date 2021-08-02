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
import br.com.zup.beagle.android.action.AnalyticsAction
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.widget.RootView

internal object AnalyticsService {

    fun createActionRecord(
        rootView: RootView,
        origin: View,
        action: AnalyticsAction,
        analyticsValue: String? = null,
    ) {
        action.analytics?.let {
            if (it is ActionAnalyticsConfig.Disabled) {
                return
            }
        }
        val analyticsProvider = BeagleEnvironment.beagleSdk.analyticsProvider
        analyticsProvider?.getConfig()?.let {
            val dataActionReport = ActionReportFactory.generateDataActionReport(
                rootView,
                origin,
                action,
                analyticsValue
            )
            reportData(dataActionReport, analyticsProvider, it)
        }
    }

    fun createScreenRecord(screenIdentifier: String) {
        val analyticsProvider: AnalyticsProvider? = BeagleEnvironment.beagleSdk.analyticsProvider
        analyticsProvider?.getConfig()?.let {
            val dataScreenReport = DataScreenReport(screenIdentifier)
            reportData(dataScreenReport, analyticsProvider, it)
        }
    }

    private fun reportData(
        dataReport: DataReport, 
        analyticsProvider: AnalyticsProvider, 
        analyticsConfig: AnalyticsConfig
    ) {
        val report = dataReport.report(analyticsConfig)
        report?.let {
            analyticsProvider.createRecord(report)
        }
    }
}
