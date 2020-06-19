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

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import br.com.zup.beagle.R
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.utils.applyViewBackgroundAndCorner
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.core.StyleComponent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class StyleManagerTest : BaseTest() {

    @InjectMockKs
    private lateinit var styleManager: StyleManager

    @RelaxedMockK
    private lateinit var view: View

    @RelaxedMockK
    private lateinit var typedArray: TypedArray

    @RelaxedMockK
    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var serverDrivenComponent: StyleComponent

    @RelaxedMockK
    private lateinit var designSystem: DesignSystem

    @RelaxedMockK
    private lateinit var colorDrawable: ColorDrawable

    @RelaxedMockK
    private lateinit var drawable: Drawable

    @RelaxedMockK
    private lateinit var typedValue: TypedValue

    private var textAppearanceInt: Int = 0

    override fun setUp() {
        super.setUp()
        mockkStatic("br.com.zup.beagle.android.components.utils.ViewExtensionsKt")
        every { beagleSdk.designSystem } returns mockk()
        every { view.background } returns mockk()
        every { designSystem.textStyle(any()) } returns textAppearanceInt
        every { context.obtainStyledAttributes(any<Int>(), any()) } returns mockk()
    }

    override fun tearDown() {
        super.tearDown()
        unmockkStatic("br.com.zup.beagle.android.components.utils.ViewExtensionsKt")
    }

    @Test
    fun test_getBackgroundColor_when_view_has_a_null_background() {
        //Given
        val expected = Color.TRANSPARENT
        every { view.background } returns null

        //When
        styleManager.applyStyleComponent(
            context,
            serverDrivenComponent,
            view
        )

        //Then
        verify(exactly = once()) { view.applyViewBackgroundAndCorner(expected, serverDrivenComponent) }
    }

    @Test
    fun test_getBackgroundColor_when_text_has_a_color_drawable_background() {
        //Given
        serverDrivenComponent = Text("")
        every { context.obtainStyledAttributes(any<Int>(), any()) } returns typedArray
        every { typedArray.getDrawable(R.styleable.BackgroundStyle_background) } returns colorDrawable
        every { colorDrawable.color } returns Color.WHITE

        //When
        styleManager.applyStyleComponent(
            context,
            serverDrivenComponent,
            view
        )

        //Then
        verify(exactly = once()) { view.applyViewBackgroundAndCorner(Color.WHITE, serverDrivenComponent) }
    }

    @Test
    fun test_getBackgroundColor_when_text_not_is_color_drawable() {
        //Given
        serverDrivenComponent = Text("")
        every { context.obtainStyledAttributes(any<Int>(), any()) } returns typedArray
        every { typedArray.getDrawable(R.styleable.BackgroundStyle_background) } returns drawable

        //When
        styleManager.applyStyleComponent(
            context,
            serverDrivenComponent,
            view
        )

        //Then
        verify(exactly = once()) { view.applyViewBackgroundAndCorner(null, serverDrivenComponent) }
    }

    @Test
    fun test_getBackgroundColor_when_button_has_a_color_drawable_background() {
        //Given
        serverDrivenComponent = Button("")
        every { context.obtainStyledAttributes(any<Int>(), any()) } returns typedArray
        every { typedArray.getDrawable(R.styleable.BackgroundStyle_background) } returns colorDrawable
        every { colorDrawable.color } returns Color.WHITE

        //When
        styleManager.applyStyleComponent(
            context,
            serverDrivenComponent,
            view
        )

        //Then
        verify(exactly = once()) { view.applyViewBackgroundAndCorner(Color.WHITE, serverDrivenComponent) }
    }

    @Test
    fun test_getBackgroundColor_when_Button_not_is_color_drawable() {
        //Given
        serverDrivenComponent = Button("")
        every { context.obtainStyledAttributes(any<Int>(), any()) } returns typedArray
        every { typedArray.getDrawable(R.styleable.BackgroundStyle_background) } returns drawable

        //When
        styleManager.applyStyleComponent(
            context,
            serverDrivenComponent,
            view
        )

        //Then
        verify(exactly = once()) { view.applyViewBackgroundAndCorner(null, serverDrivenComponent) }
    }

    @Test
    fun test_getBackgroundColor_when_view_has_a_color_drawable_background() {
        //Given
        serverDrivenComponent = Container(mockk())
        every { colorDrawable.color } returns Color.BLACK
        every { view.background } returns colorDrawable

        //When
        styleManager.applyStyleComponent(
            context,
            serverDrivenComponent,
            view
        )

        //Then
        verify(exactly = once()) { view.applyViewBackgroundAndCorner(Color.BLACK, serverDrivenComponent) }
    }

    @Test
    fun test_getBackgroundColor_when_view_has_not_a_color_drawable_background() {
        //Given
        serverDrivenComponent = Container(mockk())
        every { view.background } returns drawable

        //When
        styleManager.applyStyleComponent(
            context,
            serverDrivenComponent,
            view
        )

        //Then
        verify(exactly = once()) { view.applyViewBackgroundAndCorner(null, serverDrivenComponent) }
    }

    @Test
    fun getTypedValueByResId_should_return_TypedValue() {
        // GIVEN
        val resId = 0
        val theme = mockk<Resources.Theme>(relaxed = true)
        every { context.theme } returns theme

        // WHEN
        val result = styleManager.getTypedValueByResId(resId, context)

        // THEN
        verify(exactly = once()) { theme.resolveAttribute(resId, typedValue, true) }
        assertEquals(typedValue, result)
    }

    @Test
    fun getButtonStyle_should_return_button_style() {
        // GIVEN
        val buttonStyle = "stub"
        val buttonStyleResource = 0
        every { designSystem.buttonStyle(buttonStyle) } returns buttonStyleResource

        // WHEN
        val result = styleManager.getButtonStyle(buttonStyle)

        // THEN
        assertEquals(buttonStyleResource, result)
    }

    @Test
    fun getButtonStyle_should_call_empty_when_not_pass_style() {
        // GIVEN
        val buttonStyle = null
        val buttonStyleResource = 0
        every { designSystem.buttonStyle("") } returns buttonStyleResource

        // WHEN
        val result = styleManager.getButtonStyle(buttonStyle)

        // THEN
        verify(exactly = once()) { designSystem.buttonStyle("") }
        assertEquals(buttonStyleResource, result)
    }

    @Test
    fun getButtonTypedArray_should_call_BeagleButtonStyle() {
        // GIVEN
        val buttonStyle = "stub"
        val buttonStyleResource = 0
        every { styleManager.getButtonStyle(buttonStyle) } returns buttonStyleResource

        // WHEN
        styleManager.getButtonTypedArray(context, buttonStyle)

        // THEN
        verify(exactly = once()) {
            context.obtainStyledAttributes(buttonStyleResource, R.styleable.BeagleButtonStyle)
        }
    }

    @Test
    fun getTabBarTypedArray_should_call_BeagleTabBarStyle() {
        // GIVEN
        val tabStyle = null
        val tabStyleResource = 0
        every { designSystem.tabViewStyle("") } returns tabStyleResource

        // WHEN
        styleManager.getTabBarTypedArray(context, tabStyle)

        // THEN
        verify(exactly = once()) {
            context.obtainStyledAttributes(tabStyleResource, R.styleable.BeagleTabBarStyle)
        }
    }
}