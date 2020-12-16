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

package br.com.zup.beagle.android.setup

import android.app.Application
import br.com.zup.beagle.analytics2.AnalyticsProvider
import br.com.zup.beagle.analytics2.AnalyticsService
import br.com.zup.beagle.android.MyBeagleSetup
import io.mockk.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a BeagleSetup")
class BeagleSetupTest {

    val application : Application = mockk(relaxed = true)
    val analyticsProvider : AnalyticsProvider = mockk()

    @DisplayName("When init")
    @Nested
    inner class Init{

        @DisplayName("Then should call init fun of analytics service")
        @Test
        fun testInitShouldCallAnalyticsServiceInit(){
            //given
            BeagleSdk.setInTestMode()
            mockkObject(AnalyticsService)
            every { AnalyticsService.initialConfig(any()) } just Runs
            //when
            MyBeagleSetup(analyticsProvider).init(application)

            //then
            verify(exactly = 1) { AnalyticsService.initialConfig(analyticsProvider) }
        }
    }
}
