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

package br.com.zup.beagle.android.utils

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.utils.viewExtensionsViewFactory
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleView
import br.com.zup.beagle.android.view.custom.OnServerStateChanged
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI

private val REQUEST_DATA_FAKE = RequestData(
    uri = URI(""),
)

@DisplayName("Given a View Group")
internal class ViewGroupExtensionsKtTest : BaseTest() {

    private val viewGroup: ViewGroup = mockk(relaxed = true, relaxUnitFun = true)

    private val beagleView: BeagleView = mockk(relaxed = true, relaxUnitFun = true)

    private val viewFactory: ViewFactory = mockk(relaxed = true, relaxUnitFun = true)

    @BeforeEach
    override fun setUp() {
        super.setUp()

        viewExtensionsViewFactory = viewFactory

        every { viewFactory.makeBeagleView(any()) } returns beagleView

    }

    @DisplayName("When load view with activity")
    @Nested
    inner class LoadViewActivityTest {

        private val activity: AppCompatActivity = mockk(relaxed = true, relaxUnitFun = true)

        @DisplayName("Then should create a Beagle View")
        @Test
        fun testBeagleViewAddInViewGroup() {
            // When
            viewGroup.loadView(activity, REQUEST_DATA_FAKE)

            // Then
            verifySequence {
                viewFactory.makeBeagleView(any<ActivityRootView>())
                beagleView.stateChangedListener = null
                beagleView.serverStateChangedListener = null
                beagleView.loadView(REQUEST_DATA_FAKE)
                beagleView.loadCompletedListener = any()
                beagleView.listenerOnViewDetachedFromWindow = any()
            }
        }
    }

    @DisplayName("When load view with activity and listener")
    @Nested
    inner class LoadViewActivityAndListenerTest {

        private val activityMock: AppCompatActivity = mockk(relaxed = true, relaxUnitFun = true)
        private val listenerMock: OnServerStateChanged = mockk(relaxed = true, relaxUnitFun = true)

        @DisplayName("Then should create a Beagle View")
        @Test
        fun testBeagleViewAddInViewGroup() {
            // When
            viewGroup.loadView(activityMock, REQUEST_DATA_FAKE, listener = listenerMock)

            // Then
            verifySequence {
                viewFactory.makeBeagleView(any<ActivityRootView>())
                beagleView.stateChangedListener = null
                beagleView.serverStateChangedListener = listenerMock
                beagleView.loadView(REQUEST_DATA_FAKE)
                beagleView.loadCompletedListener = any()
                beagleView.listenerOnViewDetachedFromWindow = any()
            }
        }
    }

    @DisplayName("When load view with fragment")
    @Nested
    inner class LoadViewFragmentTest {

        private val fragmentMock: Fragment = mockk(relaxed = true, relaxUnitFun = true)

        @DisplayName("Then should create a Beagle View")
        @Test
        fun testBeagleViewAddInViewGroup() {
            // When
            viewGroup.loadView(fragmentMock, REQUEST_DATA_FAKE)

            // Then
            verifySequence {
                viewFactory.makeBeagleView(any<FragmentRootView>())
                beagleView.stateChangedListener = null
                beagleView.serverStateChangedListener = null
                beagleView.loadView(REQUEST_DATA_FAKE)
                beagleView.loadCompletedListener = any()
                beagleView.listenerOnViewDetachedFromWindow = any()
            }
        }
    }

    @DisplayName("When load view with activity and listener")
    @Nested
    inner class LoadViewFragmentAndListenerTest {

        private val fragmentMock: Fragment = mockk(relaxed = true, relaxUnitFun = true)
        private val listenerMock: OnServerStateChanged = mockk(relaxed = true, relaxUnitFun = true)

        @DisplayName("Then should create a Beagle View")
        @Test
        fun testBeagleViewAddInViewGroup() {
            // When
            viewGroup.loadView(fragmentMock, REQUEST_DATA_FAKE, listener = listenerMock)

            // Then
            verifySequence {
                viewFactory.makeBeagleView(any<FragmentRootView>())
                beagleView.stateChangedListener = null
                beagleView.serverStateChangedListener = listenerMock
                beagleView.loadView(REQUEST_DATA_FAKE)
                beagleView.loadCompletedListener = any()
                beagleView.listenerOnViewDetachedFromWindow = any()
            }
        }
    }
}