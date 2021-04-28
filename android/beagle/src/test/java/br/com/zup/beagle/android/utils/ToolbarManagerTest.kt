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

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
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
import br.com.zup.beagle.android.utils.ToolbarManager
import br.com.zup.beagle.android.utils.ToolbarTextManager
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a ToolbarManager")
internal class ToolbarManagerTest : BaseTest() {

    private var toolbarTextManagerMock = mockk<ToolbarTextManager>()
    private var screenComponent = mockk<ScreenComponent>()
    private var action = mockk<Action>()
    private var designSystemMock = mockk<DesignSystem>()
    private var navigationIcon = mockk<Drawable>()
    private var typedArray = mockk<TypedArray>()
    private var icon = mockk<Drawable>()
    private var navigationBar = mockk<NavigationBar>(relaxed = true)
    private var context = mockk<BeagleActivity>(relaxed = true)
    private var beagleFlexView = mockk<BeagleFlexView>(relaxed = true)
    private var actionBar = mockk<ActionBar>(relaxed = true)
    private var toolbar = mockk<Toolbar>(relaxed = true)
    private var menu = mockk<Menu>(relaxed = true)
    private var resources = mockk<Resources>(relaxed = true)
    private lateinit var toolbarManager: ToolbarManager
    private val style = RandomData.string()
    private val styleInt = RandomData.int()
    private val titleTextAppearance = RandomData.int()
    private val backgroundColorInt = RandomData.int()
    private val listenerSlot = slot<View.OnClickListener>()
    private val textView: TextView = mockk()
    private val textAppearanceMock = 1
    private val textViewMock = mockk<TextView>()
    val title = RandomData.string()

    @BeforeEach
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

        toolbarManager = ToolbarManager(toolbarTextManager = toolbarTextManagerMock)

        every { toolbar.setNavigationOnClickListener(capture(listenerSlot)) } returns Unit
    }

    @AfterEach
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

    @DisplayName("When configure a toolbar")
    @Nested
    inner class ConfigureToolbar {

        @Test
        @DisplayName("Then should check the center title settings")
        fun verifyCallTheGenerateCenterTitle() {
            // GIVEN
            every { toolbar.findViewById<TextView>(any()) } returns textView
            every { navigationBar.title } returns title
            every { beagleSdk.designSystem } returns designSystemMock
            every { designSystemMock.toolbarStyle(style) } returns styleInt
            every { navigationBar.styleId } returns style
            every { context.getToolbar() } returns toolbar
            every { typedArray.getBoolean(R.styleable.BeagleToolbarStyle_centerTitle, false) } returns true
            every {
                typedArray.getResourceId(R.styleable.BeagleToolbarStyle_titleTextAppearance, 0)
            } returns textAppearanceMock
            every {
                toolbarTextManagerMock.generateTitle(context, navigationBar, textAppearanceMock)
            } returns textViewMock
            every { toolbar.addView(textViewMock) } just runs
            every { toolbarTextManagerMock.centerTitle(toolbar, textViewMock) } just runs

            // WHEN
            toolbarManager.configureToolbar(rootView, navigationBar, beagleFlexView, screenComponent)

            // THEN
            verify(exactly = once()) {
                toolbarTextManagerMock.generateTitle(context, navigationBar, textAppearanceMock)
                toolbar.addView(textViewMock)
                toolbarTextManagerMock.centerTitle(toolbar, textViewMock)
            }
        }

        @Test
        @DisplayName("Then you should check if the title and the style have been applied")
        fun shouldToolbarWithTitleAndStyleWhenCallConfigureToolbarThenTheToolbarMustHaveATitleAndStyle() {
            // GIVEN
            toolbar = mockk()
            val beagleActivityMock = mockk<BeagleActivity>(relaxed = true)
            every { (rootView.getContext() as BeagleActivity) } returns beagleActivityMock
            val slotStyle = slot<Int>()
            every { beagleActivityMock.obtainStyledAttributes(capture(slotStyle),R.styleable.BeagleToolbarStyle) } returns mockk(relaxed = true)
            every { toolbar.title } returns title
            every { beagleActivityMock.getToolbar() } returns toolbar
            every { navigationBar.styleId } returns style
            every { beagleSdk.designSystem } returns designSystemMock
            every { designSystemMock.toolbarStyle(style) } returns styleInt
            every { toolbar.visibility = View.VISIBLE } just runs
            every { toolbar.menu } returns menu
            every { toolbar.navigationIcon = null } just runs
            every { toolbar.findViewById<TextView>(any()) } returns textView
            every { toolbar.removeView(textView) } just runs
            every { toolbar.title  = title } just runs
            every { toolbar.setTitleTextAppearance(beagleActivityMock, styleInt)  } just runs
            every { navigationBar.title } returns title
            every {
                typedArray.getResourceId(R.styleable.BeagleToolbarStyle_titleTextAppearance, 0)
            } returns textAppearanceMock

            // WHEN
            toolbarManager.configureToolbar(rootView, navigationBar, beagleFlexView, screenComponent)

            // THEN
            assertEquals(title, toolbar.title.toString())
            assertEquals(styleInt, slotStyle.captured)
        }

        @Test
        @DisplayName("Then you should check if the title has been applied")
        fun shouldToolbarWithTitleWhenCallConfigureToolbarThenTheToolbarMustHaveATitle() {
            // GIVEN
            toolbar = mockk()
            val beagleActivityMock = mockk<BeagleActivity>(relaxed = true)
            every { (rootView.getContext() as BeagleActivity) } returns beagleActivityMock
            every { toolbar.title } returns title
            every { beagleActivityMock.getToolbar() } returns toolbar
            every { toolbar.visibility = View.VISIBLE } just runs
            every { toolbar.menu } returns menu
            every { toolbar.navigationIcon = null } just runs
            every { toolbar.findViewById<TextView>(any()) } returns textView
            every { toolbar.removeView(textView) } just runs
            every { toolbar.title  = title } just runs
            every { navigationBar.title } returns title

            // WHEN
            toolbarManager.configureToolbar(rootView, navigationBar, beagleFlexView, screenComponent)

            // THEN
            assertEquals(title, toolbar.title.toString())
        }
    }
}