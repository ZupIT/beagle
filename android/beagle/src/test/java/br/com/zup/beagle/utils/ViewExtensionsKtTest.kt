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

package br.com.zup.beagle.utils

import android.app.Activity
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.mapper.ViewMapper
import br.com.zup.beagle.engine.renderer.ActivityRootView
import br.com.zup.beagle.engine.renderer.FragmentRootView
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.setup.DesignSystem
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.BeagleButtonView
import br.com.zup.beagle.view.BeagleView
import br.com.zup.beagle.view.ScreenRequest
import br.com.zup.beagle.view.StateChangedListener
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.ui.Button
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFails

private val URL = RandomData.httpUrl()
private const val ERROR_MESSAGE = "Did you miss to call loadView()?"
private val screenRequest = ScreenRequest(URL)

class ViewExtensionsKtTest : BaseTest() {

    @MockK
    private lateinit var viewGroup: ViewGroup
    @MockK
    private lateinit var fragment: Fragment
    @MockK
    private lateinit var activity: AppCompatActivity
    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var beagleView: BeagleView
    @MockK
    private lateinit var stateChangedListener: StateChangedListener
    @MockK(relaxed = true)
    private lateinit var inputMethodManager: InputMethodManager
    @MockK
    private lateinit var iBinder: IBinder
    @MockK
    private lateinit var designSystem: DesignSystem
    @MockK
    private lateinit var beagleButton: BeagleButtonView
    @MockK
    private lateinit var imageView: ImageView

    private val viewSlot = slot<View>()

    override fun setUp() {
        super.setUp()

        mockkStatic(TextViewCompat::class)

        viewExtensionsViewFactory = viewFactory

        every { viewFactory.makeBeagleView(any()) } returns beagleView
        every { viewFactory.makeView(any()) } returns beagleView
        every { viewGroup.addView(capture(viewSlot)) } just Runs
        every { viewGroup.context } returns activity
        every { beagleView.loadView(any(), any()) } just Runs
        every { beagleView.windowToken } returns iBinder
        every { activity.getSystemService(Activity.INPUT_METHOD_SERVICE) } returns inputMethodManager
        every { BeagleEnvironment.beagleSdk.designSystem } returns designSystem
        every { beagleButton.setBackgroundResource(any()) } just Runs
        every { beagleButton.isAllCaps = any() } just Runs
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs
        every { imageView.scaleType = any() } just Runs
        every { imageView.setImageResource(any()) } just Runs
        every { beagleButton.context } returns activity
        every { beagleButton.background = any() } just Runs
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    @Test
    fun loadView_should_create_BeagleView_and_call_loadView_with_fragment() {
        viewGroup.loadView(fragment, screenRequest)

        assertEquals(beagleView, viewSlot.captured)
        verify { viewFactory.makeBeagleView(activity) }
        verify { beagleView.loadView(any<FragmentRootView>(), screenRequest) }
    }

    @Test
    fun loadView_should_create_BeagleView_and_call_loadView_with_activity() {
        viewGroup.loadView(activity, screenRequest)

        assertEquals(beagleView, viewSlot.captured)
        verify { viewFactory.makeBeagleView(activity) }
        verify { beagleView.loadView(any<ActivityRootView>(), screenRequest) }
    }

    @Test
    fun setBeagleStateChangedListener_should_throws_exception_when_no_child_has_found() {
        // Given
        every { viewGroup.childCount } returns 0

        // When Then
        assertFails(ERROR_MESSAGE) { viewGroup.setBeagleStateChangedListener(stateChangedListener) }
    }

    @Test
    fun setBeagleStateChangedListener_should_throws_exception_when_no_BeagleView_has_found() {
        // Given
        every { viewGroup.childCount } returns 1
        every { viewGroup.getChildAt(any()) } returns mockk()

        // When Then
        assertFails(ERROR_MESSAGE) { viewGroup.setBeagleStateChangedListener(stateChangedListener) }
    }

    @Test
    fun setBeagleStateChangedListener_should_set_stateChangedListener_to_BeagleView() {
        // Given
        every { viewGroup.childCount } returns 1
        every { viewGroup.getChildAt(any()) } returns beagleView

        // When Then
        assertFails(ERROR_MESSAGE) { viewGroup.setBeagleStateChangedListener(stateChangedListener) }
    }

    @Test
    fun hideKeyboard_should_call_hideSoftInputFromWindow_with_currentFocus() {
        // Given
        every { activity.currentFocus } returns beagleView

        // When
        viewGroup.hideKeyboard()

        // Then
        verify(exactly = once()) { inputMethodManager.hideSoftInputFromWindow(iBinder, 0) }
    }

    @Test
    fun hideKeyboard_should_call_hideSoftInputFromWindow_with_created_view() {
        // Given
        every { activity.currentFocus } returns null
        every { viewExtensionsViewFactory.makeView(activity) } returns beagleView

        // When
        viewGroup.hideKeyboard()

        // Then
        verify(exactly = once()) { inputMethodManager.hideSoftInputFromWindow(iBinder, 0) }
    }
}
