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
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.beagleSerializerFactory
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.utils.loadView
import br.com.zup.beagle.android.utils.renderScreen
import br.com.zup.beagle.android.utils.toAndroidColor
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleView
import br.com.zup.beagle.android.view.custom.OnLoadCompleted
import br.com.zup.beagle.android.view.custom.OnServerStateChanged
import br.com.zup.beagle.android.view.custom.OnStateChanged
import br.com.zup.beagle.android.view.mapper.toRequestData
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.ActivityRootView
import br.com.zup.beagle.android.widget.FragmentRootView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private val URL = RandomData.httpUrl()
private val screenRequest = ScreenRequest(URL)

@DisplayName("Given a View Extension")
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

    @RelaxedMockK
    private lateinit var beagleView: BeagleView

    @MockK
    private lateinit var onStateChanged: OnStateChanged

    @MockK
    private lateinit var onServerStateChanged: OnServerStateChanged

    @RelaxedMockK
    private lateinit var inputMethodManager: InputMethodManager

    @MockK
    private lateinit var designSystem: DesignSystem

    @MockK
    private lateinit var imageView: ImageView

    private val viewSlot = slot<View>()

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkStatic(TextViewCompat::class)
        mockkStatic("br.com.zup.beagle.android.utils.StringExtensionsKt")
        mockkStatic("br.com.zup.beagle.android.utils.NumberExtensionsKt")

        mockkObject(ViewFactory)

        prepareViewModelMock(contextViewModel)
        prepareViewModelMock(generateIdViewModel)
        every { ViewFactory.makeBeagleView(any()) } returns beagleView
        every { ViewFactory.makeView(any()) } returns beagleView
        every { viewGroup.addView(capture(viewSlot)) } just Runs
        every { viewGroup.context } returns activity
        every { beagleView.loadView(any<RequestData>()) } just Runs
        every { activity.getSystemService(Activity.INPUT_METHOD_SERVICE) } returns inputMethodManager
        every { beagleSdk.designSystem } returns designSystem
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs
        every { imageView.scaleType = any() } just Runs
        every { imageView.setImageResource(any()) } just Runs

    }

    @DisplayName("When hideKeyboard")
    @Nested
    inner class HideKeyboard {

        @DisplayName("Then should call hideSoftInputFromWindow with currentFocus")
        @Test
        fun testHideKeyboardShouldCallHideSoftInputFromWindowWithCurrentFocus() {
            // Given
            every { activity.currentFocus } returns beagleView

            // When
            viewGroup.hideKeyboard()

            // Then
            verify(exactly = once()) { inputMethodManager.hideSoftInputFromWindow(any(), 0) }
        }

        @DisplayName("Then should call hideSoftInputFromWindow with created view")
        @Test
        fun testHideKeyboardShouldCallHideSoftInputFromWindowWithCreatedView() {
            // Given
            every { activity.currentFocus } returns null
            every { ViewFactory.makeView(activity) } returns beagleView

            // When
            viewGroup.hideKeyboard()

            // Then
            verify(exactly = once()) { inputMethodManager.hideSoftInputFromWindow(any(), 0) }
        }
    }

    @DisplayName("When applyStroke")
    @Nested
    inner class ApplyStroke {

        @DisplayName("Then must call setStroke")
        @Test
        fun testGivenColorValuesAndBorderSizeWhenApplyStrokeIsCalledThenMustCallSetStroke() {
            // Given
            val defaultColor = "#000000"
            val resultWidth = 5
            val resultColor = 0
            val styleWidget = mockk<StyleComponent>()
            val style = Style(borderWidth = resultWidth.toDouble(), borderColor = defaultColor)
            val gradientDrawable = mockk<GradientDrawable>(relaxUnitFun = true, relaxed = true)

            every { viewGroup.background } returns gradientDrawable
            every { styleWidget.style } returns style
            every { resultWidth.dp() } returns resultWidth
            every { defaultColor.toAndroidColor() } returns resultColor

            // When
            viewGroup.applyStroke(styleWidget)

            // Then
            verify {
                gradientDrawable.setStroke(resultWidth, resultColor)
            }
        }

        @DisplayName("Then should not call setStroke")
        @Test
        fun testGivenBorderWidthWithNullValueWhenApplyStrokeIsCalledThenShouldNotCallSetStroke() {
            // Given
            val defaultColor = "#000000"
            val resultWidth = 5
            val resultColor = 0
            val styleWidget = mockk<StyleComponent>()
            val style = Style(borderWidth = null, borderColor = defaultColor)
            val gradientDrawable = mockk<GradientDrawable>(relaxUnitFun = true, relaxed = true)

            every { viewGroup.background } returns gradientDrawable
            every { styleWidget.style } returns style
            every { defaultColor.toAndroidColor() } returns resultColor

            // When
            viewGroup.applyStroke(styleWidget)

            // Then
            verify(exactly = 0) {
                gradientDrawable.setStroke(resultWidth, resultColor)
            }
        }

        @DisplayName("Then should not call setStroke")
        @Test
        fun testGivenBorderColorWithNullValueWhenApplyStrokeIsCalledThenShouldNotCallSetStroke() {
            // Given
            val resultWidth = 5
            val resultColor = 0
            val styleWidget = mockk<StyleComponent>()
            val style = Style(borderWidth = resultWidth.toDouble(), borderColor = null)
            val gradientDrawable = mockk<GradientDrawable>(relaxUnitFun = true, relaxed = true)

            every { viewGroup.background } returns gradientDrawable
            every { styleWidget.style } returns style
            every { resultWidth.dp() } returns resultWidth

            // When
            viewGroup.applyStroke(styleWidget)

            // Then
            verify(exactly = 0) {
                gradientDrawable.setStroke(resultWidth, resultColor)
            }
        }

        @DisplayName("Then create a new instance")
        @Test
        fun testGivenBackgroundWithNullValueWhenValueIsNullThenCreateANewInstance() {
            // Given
            val defaultColor = "#gf5487"
            val resultWidth = 5
            val resultColor = 0
            val styleWidget = mockk<StyleComponent>()
            val style = Style(borderWidth = resultWidth.toDouble(), borderColor = defaultColor)
            mockkConstructor(GradientDrawable::class)

            every { viewGroup.background } returns null
            every { styleWidget.style } returns style
            every { resultWidth.dp() } returns resultWidth
            every { defaultColor.toAndroidColor() } returns resultColor
            every { anyConstructed<GradientDrawable>().setStroke(resultWidth, resultColor) } just Runs

            // When
            viewGroup.applyStroke(styleWidget)

            // Then
            verify(exactly = 1) {
                anyConstructed<GradientDrawable>().setStroke(resultWidth, resultColor)
            }
        }
    }

    @DisplayName("When load view")
    @Nested
    inner class LoadView {

        @DisplayName("Then should create BeagleView and call loadView with fragment")
        @Test
        fun testLoadViewShouldCreateBeagleViewAndCallLoadViewWithFragment() {
            // Given When
            viewGroup.loadView(fragment, screenRequest, onStateChanged)

            // Then
            val requestData = screenRequest.toRequestData()
            verifySequence {
                generateIdViewModel.createIfNotExisting(0)
                ViewFactory.makeBeagleView(any<FragmentRootView>())
                beagleView.stateChangedListener = any()
                beagleView.serverStateChangedListener = any()
                beagleView.loadView(requestData)
                beagleView.loadCompletedListener = any()
                beagleView.listenerOnViewDetachedFromWindow = any()
            }
        }

        @DisplayName("Then should create BeagleView and call loadView with activity")
        @Test
        fun testLoadViewShouldCreateBeagleViewAndCallLoadViewWithActivity() {
            // When
            viewGroup.loadView(activity, screenRequest, onStateChanged)

            // Then
            val requestData = screenRequest.toRequestData()
            verify {
                ViewFactory.makeBeagleView(any<ActivityRootView>())
                beagleView.loadView(requestData)
            }
        }

        @DisplayName("Then should call removeAllViews and call addView when load comples")
        @Test
        fun testLoadViewShouldCalRemoveAllViewsAndCallAddViewWhenLoadComplete() {
            // Given
            val slot = slot<OnLoadCompleted>()
            every { beagleView.loadCompletedListener = capture(slot) } just Runs

            // When
            viewGroup.loadView(fragment, screenRequest, onStateChanged)
            slot.captured.invoke()

            // Then
            assertEquals(beagleView, viewSlot.captured)
            verifyOrder {
                viewGroup.removeAllViews()
                viewGroup.addView(beagleView)
            }
        }

        @DisplayName("Then should call addView when load complete")
        @Test
        fun testLoadViewWwithoutStateShouldAddViewWhenLoadComplete() {
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

        @DisplayName("Then should set stateChangedListener to beagleView")
        @Test
        fun testLoadViewShouldSetStateChangedListenerToBeagleView() {
            // Given
            val slot = slot<OnStateChanged>()
            every { beagleView.stateChangedListener = capture(slot) } just Runs

            // When
            viewGroup.loadView(fragment, screenRequest, onStateChanged)

            // Then
            assertEquals(slot.captured, onStateChanged)
        }

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testDeprecatedLoadViewWithActivity() {
            //given
            val slot = commonGiven()

            //when
            viewGroup.loadView(activity, screenRequest, onStateChanged)

            //then
            assertEquals(screenRequest.url, slot.captured.getScreenId())
        }

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testLoadViewWithActivity() {
            //given
            val slot = commonGiven()

            //when
            viewGroup.loadView(activity, screenRequest)

            //then
            assertEquals(screenRequest.url, slot.captured.getScreenId())
        }

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testLoadViewWithActivityAndOnServerStateChanged() {
            //given
            val slot = commonGiven()

            //when
            viewGroup.loadView(activity, screenRequest, onServerStateChanged)

            //then
            assertEquals(screenRequest.url, slot.captured.getScreenId())
        }

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testDeprecatedLoadViewWithFragment() {
            //given
            val slot = commonGiven()

            //when
            viewGroup.loadView(fragment, screenRequest, onStateChanged)

            //then
            assertEquals(screenRequest.url, slot.captured.getScreenId())
        }

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testLoadViewWithFragment() {
            //given
            val slot = commonGiven()

            //when
            viewGroup.loadView(fragment, screenRequest)

            //then
            assertEquals(screenRequest.url, slot.captured.getScreenId())
        }

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testLoadViewWithFragmentAndOnServerStateChanged() {
            //given
            val slot = commonGiven()

            //when
            viewGroup.loadView(fragment, screenRequest, onServerStateChanged)

            //then
            assertEquals(screenRequest.url, slot.captured.getScreenId())
        }

        private fun commonGiven(): CapturingSlot<RootView> {
            val slot = slot<RootView>()
            every { ViewFactory.makeBeagleView(capture(slot)) } returns beagleView
            return slot
        }
    }

    @DisplayName("When render screen")
    @Nested
    inner class RenderScreen {
        private val json = """{
                "_beagleComponent_" : "beagle:screenComponent",
                "child" : {
                "_beagleComponent_" : "beagle:text",
                "text" : "hello"
            }
            }"""

        private val component: ServerDrivenComponent = mockk(relaxed = true)
        private val serializerFactory: BeagleSerializer = mockk(relaxed = true)

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testRenderScreenWithActivity() {
            // Given
            beagleSerializerFactory = serializerFactory
            every { serializerFactory.deserializeComponent(any()) } returns component

            // When
            viewGroup.renderScreen(activity, json, "screenId")

            // Then
            verifySequence {
                viewGroup.id
                serializerFactory.deserializeComponent(json)
                ViewFactory.makeBeagleView(any<ActivityRootView>())
                beagleView.addServerDrivenComponent(component)
                beagleView.listenerOnViewDetachedFromWindow = any()
                viewGroup.removeAllViews()
                viewGroup.addView(beagleView)
            }
        }

        @DisplayName("Then should create the rootView with right parameters")
        @Test
        fun testRenderScreenWithFragment() {
            // Given
            beagleSerializerFactory = serializerFactory
            every { serializerFactory.deserializeComponent(any()) } returns component

            // When
            viewGroup.renderScreen(fragment, json, "screenId")

            // Then
            verifySequence {
                viewGroup.id
                serializerFactory.deserializeComponent(json)
                ViewFactory.makeBeagleView(any<FragmentRootView>())
                beagleView.addServerDrivenComponent(component)
                beagleView.listenerOnViewDetachedFromWindow = any()
                viewGroup.removeAllViews()
                viewGroup.addView(beagleView)
            }
        }
    }
}

