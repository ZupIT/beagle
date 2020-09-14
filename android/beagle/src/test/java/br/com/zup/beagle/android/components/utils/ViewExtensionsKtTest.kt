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

package br.com.zup.beagle.android.components.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.loadView
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleView
import br.com.zup.beagle.android.view.custom.OnLoadCompleted
import br.com.zup.beagle.android.view.custom.OnStateChanged
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test

private val URL = RandomData.httpUrl()
private val screenRequest = ScreenRequest(URL)

class ViewExtensionsKtTest : BaseTest() {

    @RelaxedMockK
    private lateinit var viewGroup: ViewGroup

    @MockK(relaxUnitFun = true, relaxed = true)
    private lateinit var contextViewModel: ScreenContextViewModel

    @MockK(relaxUnitFun = true, relaxed = true)
    private lateinit var generateIdViewModel: GenerateIdViewModel

    @RelaxedMockK
    private lateinit var fragment: Fragment

    @RelaxedMockK
    private lateinit var activity: AppCompatActivity

    @MockK
    private lateinit var viewFactory: ViewFactory

    @RelaxedMockK
    private lateinit var beagleView: BeagleView

    @MockK
    private lateinit var onStateChanged: OnStateChanged

    @RelaxedMockK
    private lateinit var inputMethodManager: InputMethodManager

    @MockK
    private lateinit var designSystem: DesignSystem

    @MockK
    private lateinit var imageView: ImageView

    private val viewSlot = slot<View>()

    override fun setUp() {
        super.setUp()

        mockkStatic(TextViewCompat::class)

        viewExtensionsViewFactory = viewFactory

        prepareViewModelMock(contextViewModel)
        prepareViewModelMock(generateIdViewModel)
        every { viewFactory.makeBeagleView(any()) } returns beagleView
        every { viewFactory.makeView(any()) } returns beagleView
        every { viewGroup.addView(capture(viewSlot)) } just Runs
        every { viewGroup.context } returns activity
        every { beagleView.loadView(any()) } just Runs
        every { activity.getSystemService(Activity.INPUT_METHOD_SERVICE) } returns inputMethodManager
        every { beagleSdk.designSystem } returns designSystem
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs
        every { imageView.scaleType = any() } just Runs
        every { imageView.setImageResource(any()) } just Runs
    }

    @Test
    fun loadView_should_create_BeagleView_and_call_loadView_with_fragment() {
        // Given When
        viewGroup.loadView(fragment, screenRequest, onStateChanged)

        // Then
        verifySequence {
            generateIdViewModel.createIfNotExisting(0)
            viewFactory.makeBeagleView(any<FragmentRootView>())
            beagleView.stateChangedListener = any()
            beagleView.serverStateChangedListener = any()
            beagleView.loadView(screenRequest)
            beagleView.loadCompletedListener = any()
            beagleView.listenerOnViewDetachedFromWindow = any()
        }
    }

    @Test
    fun loadView_should_create_BeagleView_and_call_loadView_with_activity() {
        // When
        viewGroup.loadView(activity, screenRequest, onStateChanged)

        // Then
        verify { viewFactory.makeBeagleView(any<ActivityRootView>()) }
        verify { beagleView.loadView(screenRequest) }
    }

    @Test
    fun `loadView should addView when load complete`() {
        // Given
        val slot = slot<OnLoadCompleted>()
        every { beagleView.loadCompletedListener = capture(slot) } just Runs

        // When
        viewGroup.loadView(fragment, screenRequest, onStateChanged)
        slot.captured.invoke()

        // Then
        assertEquals(beagleView, viewSlot.captured)
        verify(exactly = once()) { viewGroup.addView(beagleView) }
    }


    @Test
    fun `loadView without state should addView when load complete`() {
        // Given
        val slot = slot<OnLoadCompleted>()
        every { beagleView.loadCompletedListener = capture(slot) } just Runs

        // When
        viewGroup.loadView(fragment, screenRequest)
        slot.captured.invoke()

        // Then
        assertEquals(beagleView, viewSlot.captured)
        verify(exactly = once()) { viewGroup.addView(beagleView) }
    }

    @Test
    fun `loadView should set stateChangedListener to beagleView`() {
        // Given
        val slot = slot<OnStateChanged>()
        every { beagleView.stateChangedListener = capture(slot) } just Runs

        // When
        viewGroup.loadView(fragment, screenRequest, onStateChanged)

        // Then
        assertEquals(slot.captured, onStateChanged)
    }

    @Test
    fun hideKeyboard_should_call_hideSoftInputFromWindow_with_currentFocus() {
        // Given
        every { activity.currentFocus } returns beagleView

        // When
        viewGroup.hideKeyboard()

        // Then
        verify(exactly = once()) { inputMethodManager.hideSoftInputFromWindow(any(), 0) }
    }

    @Test
    fun hideKeyboard_should_call_hideSoftInputFromWindow_with_created_view() {
        // Given
        every { activity.currentFocus } returns null
        every { viewExtensionsViewFactory.makeView(activity) } returns beagleView

        // When
        viewGroup.hideKeyboard()

        // Then
        verify(exactly = once()) { inputMethodManager.hideSoftInputFromWindow(any(), 0) }
    }
}
