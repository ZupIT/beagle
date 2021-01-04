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

internal data class DataActionReport(
    var originX : Float? = null,
    var originY : Float? = null,
    var attributes: HashMap<String, Any>,
    var id : String? = null,
    var type : String? = null,
    var analyticsValue: String? = null,
    var action : ActionAnalytics,
    var screenId : String? = null,
    var actionType : String
) : DataReport{
    override fun report() {
        AnalyticsService.reportActionIfShould(this)
    }
}