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
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.NavigationBarItem
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.custom.BeagleNavigator
import br.com.zup.beagle.android.widget.RootView

internal class ToolbarManager {

    fun configureNavigationBarForScreen(
        context: BeagleActivity,
        navigationBar: NavigationBar
    ) {
        if (navigationBar.showBackButton) {
            context.getToolbar().apply {
                navigationBar.backButtonAccessibility?.accessibilityLabel?.let { backButtonAccessibilityLabel ->
                    navigationContentDescription = backButtonAccessibilityLabel
                }

                setNavigationOnClickListener {
                    BeagleNavigator.popView(context)
                }

                setupNavigationIcon(context, this)

            }
        }
    }

    fun configureToolbar(
        rootView: RootView,
        navigationBar: NavigationBar,
        container: BeagleFlexView,
        screenComponent: ScreenComponent) {
        (rootView.getContext() as BeagleActivity).getToolbar().apply {
            visibility = View.VISIBLE
            menu.clear()
            configToolbarStyle(rootView.getContext(), this, navigationBar)
            navigationBar.navigationBarItems?.let { items ->
                configToolbarItems(rootView, this, items, container, screenComponent)
            }
        }
    }

    private fun configToolbarStyle(
        context: Context,
        toolbar: Toolbar,
        navigationBar: NavigationBar
    ) {
        val designSystem = BeagleEnvironment.beagleSdk.designSystem
        val style = navigationBar.styleId ?: ""
        designSystem?.toolbarStyle(style)?.let { toolbarStyle ->
            val typedArray = context.obtainStyledAttributes(
                toolbarStyle,
                R.styleable.BeagleToolbarStyle
            )
            if (navigationBar.showBackButton) {
                typedArray.getDrawable(R.styleable.BeagleToolbarStyle_navigationIcon)?.let {
                    toolbar.navigationIcon = it
                }
            } else {
                toolbar.navigationIcon = null
            }
            val textAppearance = typedArray.getResourceId(
                R.styleable.BeagleToolbarStyle_titleTextAppearance, 0
            )
            if (typedArray.getBoolean(R.styleable.BeagleToolbarStyle_centerTitle, false)) {
                removePreviousToolbarTitle(toolbar)
                val titleTextView = generateCenterTitle(context, navigationBar, textAppearance, toolbar)
                toolbar.addView(titleTextView)
                centerTitle(toolbar, titleTextView)
                toolbar.title = ""
            } else {
                toolbar.title = navigationBar.title
                if (textAppearance != 0) {
                    toolbar.setTitleTextAppearance(context, textAppearance)
                }
            }
            val backgroundColor = typedArray.getColor(
                R.styleable.BeagleToolbarStyle_backgroundColor, 0
            )
            if (backgroundColor != 0) {
                toolbar.setBackgroundColor(backgroundColor)
            }
            typedArray.recycle()
        }
    }

    private fun centerTitle(
        toolbar: Toolbar,
        titleTextView: TextView
    ) {
        toolbar.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val idealX = ((toolbar.width - titleTextView.width) / 2).toFloat()
            val lastToolbarView = toolbar.children.find {
                it.right == toolbar.width
            }
            val lastToolbarViewStart = lastToolbarView?.left ?: 0
            if (idealX + titleTextView.width > lastToolbarViewStart) {
                val idealXAdjusted = idealX - (idealX + titleTextView.width - lastToolbarViewStart)
                titleTextView.x = idealXAdjusted
            } else {
                titleTextView.x = idealX
            }
        }
    }

    private fun removePreviousToolbarTitle(toolbar: Toolbar) {
        val centeredTitle = toolbar.findViewById<TextView>(R.id.beagle_toolbar_text)
        toolbar.removeView(centeredTitle)
    }

    private fun generateCenterTitle(
        context: Context,
        navigationBar: NavigationBar,
        textAppearance: Int,
        toolbar: Toolbar
    ) = TextView(context).apply {
        id = R.id.beagle_toolbar_text
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
        layoutParams = params
        text = navigationBar.title
        if (textAppearance != 0) {
            TextViewCompat.setTextAppearance(this, textAppearance)
        }
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setContentInsetsAbsolute(0, 0)
    }

    private fun configToolbarItems(
        rootView: RootView,
        toolbar: Toolbar,
        items: List<NavigationBarItem>,
        container: BeagleFlexView,
        screenComponent: ScreenComponent
    ) {
        val designSystem = BeagleEnvironment.beagleSdk.designSystem
        for (i in items.indices) {
            toolbar.menu.add(Menu.NONE, items[i].id?.toAndroidId() ?: i, Menu.NONE, items[i].text).apply {
                setOnMenuItemClickListener {
                    val action = items[i].action
                    action.handleEvent(rootView, toolbar, action)
                    return@setOnMenuItemClickListener true
                }

                setContentDescription(items, i)

                if (items[i].image == null) {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                } else {
                    configMenuItem(designSystem, items, i, rootView, container, screenComponent)
                }
            }
        }
    }

    private fun MenuItem.setContentDescription(
        items: List<NavigationBarItem>,
        i: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            items[i].accessibility?.accessibilityLabel?.let { accessibilityLabel ->
                this.contentDescription = accessibilityLabel
            }
        }
    }
    
    @Suppress("LongParameterList")
    private fun MenuItem.configMenuItem(
        design: DesignSystem?,
        items: List<NavigationBarItem>,
        i: Int,
        rootView: RootView,
        container: BeagleFlexView,
        screenComponent: ScreenComponent
    ) {
        design?.let { designSystem ->
            items[i].image?.let { image ->
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

                screenComponent.observeBindChanges(rootView, container, image.mobileId) { mobileId ->
                    mobileId?.let {
                        icon = designSystem.image(it)?.let { iconRes ->
                            ResourcesCompat.getDrawable(
                                rootView.getContext().resources,
                                iconRes,
                                null
                            )
                        }
                    }
                }


            }
        }
    }

    private fun getDrawableFromAttribute(context: Context, attributeId: Int): Drawable? {
        val typedValue = TypedValue().also { context.theme.resolveAttribute(attributeId, it, true) }
        return ContextCompat.getDrawable(context, typedValue.resourceId)
    }

    private fun setupNavigationIcon(context: Context, toolbar: Toolbar) {
        if (toolbar.navigationIcon == null) {
            toolbar.navigationIcon = getDrawableFromAttribute(context, androidx.appcompat.R.attr.homeAsUpIndicator)
        }
    }
}
