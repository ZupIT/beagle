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

package br.com.zup.beagle.android.view.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.zup.beagle.analytics2.AnalyticsService
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.widget.RootView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class AnalyticsViewModel : ViewModel() {

    fun createActionReport(
        rootView: RootView,
        origin: View,
        action: ActionAnalytics,
        analyticsValue : String?
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            AnalyticsService.createActionRecord(
                rootView,
                origin,
                action,
                analyticsValue
            )
        }
    }

    fun createScreenReport(isLocalScreen: Boolean, screenIdentifier: String){
        viewModelScope.launch(Dispatchers.Default) {
            AnalyticsService.createScreenRecord(isLocalScreen, screenIdentifier)
        }
    }
}