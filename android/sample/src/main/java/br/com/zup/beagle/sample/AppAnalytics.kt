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

package br.com.zup.beagle.sample

import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.annotation.BeagleComponent

@BeagleComponent
class AppAnalytics: Analytics {

    companion object {
        const val TAG = "AppAnalytics"
    }

    override fun trackEventOnClick(event: ClickEvent) {
        println("$TAG: CLICK_EVENT " +
            "category: ${event.category}, " +
            "label: ${event.label ?: ""}, " +
            "value: ${event.value ?: ""}")

    }

    override fun trackEventOnScreenAppeared(event: ScreenEvent) {
        println("$TAG: VIEW_WILL_APPEAR screenName: ${event.screenName}")
    }

    override fun trackEventOnScreenDisappeared(event: ScreenEvent) {
        println("$TAG: VIEW_WILL_DISAPPEAR screenName: ${event.screenName}")
    }

}