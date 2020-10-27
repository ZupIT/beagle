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

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import br.com.zup.beagle.R
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.NavigationBarItem
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ToolbarManagerTest : BaseTest() {

    @MockK
    private lateinit var screenComponent: ScreenComponent

    @MockK(relaxed = true)
    private lateinit var navigationBar: NavigationBar

    @RelaxedMockK
    private lateinit var context: BeagleActivity

    @RelaxedMockK
    private lateinit var beagleFlexView: BeagleFlexView

    @MockK(relaxed = true)
    private lateinit var actionBar: ActionBar

    @RelaxedMockK
    private lateinit var toolbar: Toolbar

    @MockK
    private lateinit var action: Action

    @RelaxedMockK
    private lateinit var menu: Menu

    @MockK
    private lateinit var designSystemMock: DesignSystem

    @MockK
    private lateinit var navigationIcon: Drawable

    @MockK
    private lateinit var typedArray: TypedArray

    @MockK
    private lateinit var icon: Drawable

    @RelaxedMockK
    private lateinit var resources: Resources

    private lateinit var toolbarManager: ToolbarManager

    private val style = RandomData.string()
    private val styleInt = RandomData.int()
    private val titleTextAppearance = RandomData.int()
    private val backgroundColorInt = RandomData.int()
    private val listenerSlot = slot<View.OnClickListener>()

    override fun setUp() {
        super.setUp()
        mockkStatic(ResourcesCompat::class)
        every { rootView.getContext() } returns context
        every { context.resources } returns resources
        every {
            context.obtainStyledAttributes(styleInt, R.styleable.BeagleToolbarStyle)
        } returns typedArray
        every {
            typedArray.getDrawable(R.styleable.BeagleToolbarStyle_navigationIcon)
        } returns navigationIcon
        every {
            typedArray.getResourceId(R.styleable.BeagleToolbarStyle_titleTextAppearance, 0)
        } returns titleTextAppearance
        every {
            typedArray.getColor(R.styleable.BeagleToolbarStyle_backgroundColor, 0)
        } returns backgroundColorInt
        every {
            typedArray.getBoolean(R.styleable.BeagleToolbarStyle_centerTitle, false)
        } returns false
        every { typedArray.recycle() } just Runs

        toolbarManager = ToolbarManager()

        every { toolbar.setNavigationOnClickListener(capture(listenerSlot)) } returns Unit
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    @Test
    fun `configureNavigationBarForScreen should set toolbar setNavigationOnClickListener when navigationBar showBackButton is true`() {
        // Given
        val showBackButton = true
        every { navigationBar.showBackButton } returns showBackButton
        every { context.getToolbar() } returns toolbar

        // When
        toolbarManager.configureNavigationBarForScreen(context, navigationBar)

        // Then
        assertTrue { listenerSlot.isCaptured }
    }

    @Test
    fun `configureNavigationBarForScreen should set NavigationIcon when navigationBar showBackButton is true`() {
        // Given
        val toolbarManagerSpy = spyk(toolbarManager, recordPrivateCalls = true)
        val homeAsUpIndicatorAttr = R.attr.homeAsUpIndicator
        val showBackButton = true
        toolbar.navigationIcon = null
        every { navigationBar.showBackButton } returns showBackButton
        every { context.getToolbar() } returns toolbar
        every { toolbar.navigationIcon } returns null


        // When
        toolbarManagerSpy.configureNavigationBarForScreen(context, navigationBar)

        // Then
        verify(exactly = 1) { toolbarManagerSpy["setupNavigationIcon"](context, toolbar) }
        verify(exactly = 1) { toolbarManagerSpy["getDrawableFromAttribute"](context, homeAsUpIndicatorAttr) }
    }

    @Test
    fun configure_toolbar_style_when_toolbar_is_not_null() {
        // Given
        val title = RandomData.string()
        val textView: TextView = mockk()
        every { toolbar.findViewById<TextView>(any()) } returns textView
        every { navigationBar.title } returns title
        every { beagleSdk.designSystem } returns designSystemMock
        every { designSystemMock.toolbarStyle(style) } returns styleInt
        every { navigationBar.styleId } returns style
        every { context.getToolbar() } returns toolbar
        every { navigationBar.showBackButton } returns true

        // When
        toolbarManager.configureToolbar(rootView, navigationBar, beagleFlexView, screenComponent)

        // Then
        verify(exactly = once()) { toolbar.removeView(textView) }
        verify(atLeast = once()) { toolbar.navigationIcon = navigationIcon }
        verify(atLeast = once()) { toolbar.title = title }
        verify(atLeast = once()) { toolbar.setTitleTextAppearance(context, titleTextAppearance) }
        verify(atLeast = once()) { toolbar.setBackgroundColor(backgroundColorInt) }
        verify(atLeast = once()) { typedArray.recycle() }
        verify(atLeast = once()) { toolbar.visibility = View.VISIBLE }
    }

    @Test
    fun should_not_set_toolbar_navigationIcon_when_showBackButton_is_false() {
        // Given
        every { beagleSdk.designSystem } returns designSystemMock
        every { designSystemMock.toolbarStyle(style) } returns styleInt
        every { navigationBar.styleId } returns style
        every { screenComponent.navigationBar } returns navigationBar
        every { context.supportActionBar } returns actionBar
        every { context.getToolbar() } returns toolbar
        every { navigationBar.showBackButton } returns false
        every { toolbar.findViewById<TextView>(any()) } returns mockk()


        // When
        toolbarManager.configureToolbar(rootView, navigationBar, beagleFlexView, screenComponent)

        // Then
        verify(atLeast = once()) { toolbar.navigationIcon = null }
    }

    @Test
    fun should_configToolbarItems_when_navigationBarItems_is_not_null_and_image_is_null() {
        // GIVEN
        every { screenComponent.navigationBar } returns navigationBar
        every { context.supportActionBar } returns null
        every { context.getToolbar() } returns toolbar
        every { toolbar.menu } returns menu
        val navigationBarItems = listOf(
            NavigationBarItem(text = "Stub", action = action)
        )
        every { navigationBar.navigationBarItems } returns navigationBarItems
        val menuItem = spyk<MenuItem>()
        every { menu.add(any(), any(), any(), any<String>()) } returns menuItem
        every { toolbar.findViewById<TextView>(any()) } returns mockk()


        // WHEN
        toolbarManager.configureToolbar(rootView, navigationBar, beagleFlexView, screenComponent)

        // THEN
        assertEquals(View.VISIBLE, toolbar.visibility)
        verify(exactly = once()) { menu.clear() }
        verify(exactly = navigationBarItems.size) { menu.add(Menu.NONE, 0, Menu.NONE, "Stub") }
        verify(exactly = navigationBarItems.size) { menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER) }
    }

    @Test
    fun should_configToolbarItems_when_navigationBarItems_is_not_null_and_image_is_not_null() {
        // GIVEN
        every { beagleSdk.designSystem } returns designSystemMock
        every { designSystemMock.toolbarStyle(any()) } returns styleInt
        every { designSystemMock.image(any()) } returns RandomData.int()
        every { context.getToolbar() } returns toolbar
        every { toolbar.menu } returns menu
        val navigationBarItems = listOf(
            NavigationBarItem(text = "Stub", image = ImagePath.Local("image"), action = action)
        )
        every { navigationBar.navigationBarItems } returns navigationBarItems
        val menuItem = spyk<MenuItem>()
        every { menu.add(any(), any(), any(), any<String>()) } returns menuItem
        every { ResourcesCompat.getDrawable(any(), any(), any()) } returns icon
        every { toolbar.findViewById<TextView>(any()) } returns mockk()


        // WHEN
        toolbarManager.configureToolbar(rootView, navigationBar, beagleFlexView, screenComponent)

        // THEN
        verify(exactly = once()) { menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS) }
        verify(exactly = once()) { menuItem.icon = icon }
    }
}