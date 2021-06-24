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

package br.com.zup.beagle.android.components

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.get
import br.com.zup.beagle.R
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.BeagleRetry
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleView
import br.com.zup.beagle.android.view.custom.OnServerStateChanged
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private val URL = RandomData.httpUrl()

@DisplayName("Given a Lazy Component")
class LazyComponentTest : BaseComponentTest() {

    private val initialStateView: View = mockk()
    private val initialState: ServerDrivenComponent = mockk()
    private val beagleView: BeagleView = mockk(relaxed = true)
    private val slot = slot<OnServerStateChanged>()
    private val errorView = mockk<LinearLayout>(relaxed = true)
    private val errorRetryButton = mockk<Button>(relaxed = true, relaxUnitFun = true)

    private lateinit var lazyComponent: LazyComponent

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { ViewFactory.makeBeagleView(any()) } returns beagleView

        every { beagleView[0] } returns initialStateView

        every {
            beagleView.serverStateChangedListener = capture(slot)
        } just Runs

        mockkStatic(LayoutInflater::class)

        every { LayoutInflater.from(any()).inflate(any<Int>(), any(), false) } returns errorView

        every { errorView.findViewById<Button>(R.id.buttonRetry) } returns errorRetryButton

        lazyComponent = LazyComponent(URL, initialState)
    }

    @DisplayName("When call build view")
    @Nested
    inner class BuildViewTest {

        @Test
        @DisplayName("Then should return a beagle view")
        fun testCorrectView() {
            // When
            val actual = lazyComponent.buildView(rootView)

            // Then
            assertTrue(actual is BeagleView)
        }


        @Test
        @DisplayName("Then should update a view")
        fun testUpdateView() {
            // When
            lazyComponent.buildView(rootView)

            // Then
            verify(exactly = once()) {
                beagleView.addServerDrivenComponent(initialState)
                beagleView.updateView(URL, initialStateView)

            }
        }
    }

    @DisplayName("When state of view it is error")
    @Nested
    inner class ErrorViewTest {

        @Test
        @DisplayName("Then should return a correct error view")
        fun testCorrectView() {
            // When
            lazyComponent.buildView(rootView)
            slot.captured.invoke(ServerDrivenState.Error(mockk(), mockk()))

            // Then
            verify(exactly = once()) {
                beagleView.addView(any())
            }
        }

    }

    @DisplayName("When try again in error view")
    @Nested
    inner class ErrorTryAgainViewTest {

        @Test
        @DisplayName("Then should call try again")
        fun testCorrectTryAgain() {
            // Given
            val viewOnClickListenerSlot = slot<View.OnClickListener>()
            val retry = mockk<BeagleRetry>(relaxUnitFun = true, relaxed = true)
            val errorState = ServerDrivenState.Error(mockk(), retry)

            every {
                errorRetryButton.setOnClickListener(capture(viewOnClickListenerSlot))
            } just Runs

            // When
            lazyComponent.buildView(rootView)
            slot.captured.invoke(errorState)
            viewOnClickListenerSlot.captured.onClick(view)

            // Then
            verifyOrder {
                beagleView.removeAllViews()
                beagleView.addServerDrivenComponent(initialState)
                retry.invoke()
            }
        }

    }

}