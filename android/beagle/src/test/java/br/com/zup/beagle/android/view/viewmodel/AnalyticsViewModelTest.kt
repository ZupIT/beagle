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
import androidx.lifecycle.viewModelScope
import br.com.zup.beagle.analytics2.AnalyticsService
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.testutil.CoroutinesTestExtension
import br.com.zup.beagle.android.widget.RootView
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(CoroutinesTestExtension::class)
@DisplayName("Given an Analytics View Model")
internal class AnalyticsViewModelTest {

    private val analyticsViewModel = AnalyticsViewModel()
    var rootView: RootView = mockk()
    var origin: View = mockk()
    var action: ActionAnalytics = mockk()
    var analyticsValue: String = "any"

    @BeforeEach
    fun setUp() {
        mockkObject(AnalyticsService)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(AnalyticsService)
    }

    @DisplayName("When create action report")
    @Nested
    inner class CreateActionReport {

        @DisplayName("Should call Analytics Service with correct parameters")
        @Test
        fun testCreateActionReportShouldCallCorrectFun() = runBlockingTest {
            //given
            every { AnalyticsService.createActionRecord(rootView, origin, action, analyticsValue) } just Runs

            //when
            analyticsViewModel.createActionReport(rootView, origin, action, analyticsValue)

            //then
            verify(exactly = 1) {
                AnalyticsService.createActionRecord(rootView, origin, action, analyticsValue)
            }
        }
    }

    @DisplayName("When create screen report")
    @Nested
    inner class CreateScreenReport {

        @DisplayName("Should call Analytics Service with correct parameters")
        @Test
        fun testCreateScreenReportShouldCallCorrectFun() = runBlockingTest {
            //given
            every { AnalyticsService.createScreenRecord(false, "screenId") } just Runs

            //when
            analyticsViewModel.createScreenReport(false, "screenId")

            //then
            verify(exactly = 1) {
                AnalyticsService.createScreenRecord(false, "screenId")
            }
            analyticsViewModel.viewModelScope
        }
    }

}