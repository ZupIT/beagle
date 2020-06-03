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

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.action.ActionExecutor
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.setup.DesignSystem
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem

internal class ToolbarManager(private val actionExecutor: ActionExecutor = ActionExecutor()) {

    fun configureNavigationBarForScreen(
        context: BeagleActivity,
        navigationBar: NavigationBar
    ) {
        context.supportActionBar?.apply {
            val showBackButton = navigationBar.showBackButton
            setDisplayHomeAsUpEnabled(showBackButton)
            setDisplayShowHomeEnabled(showBackButton)
            navigationBar.backButtonAccessibility?.accessibilityLabel?.let { backButtonAccessibilityLabel ->
                setHomeActionContentDescription(backButtonAccessibilityLabel)
            }
            show()
        }
    }

    fun configureToolbar(
        context: BeagleActivity,
        navigationBar: NavigationBar
    ) {
        context.getToolbar().apply {
            visibility = View.VISIBLE
            menu.clear()
            configToolbarStyle(context, this, navigationBar)
            navigationBar.navigationBarItems?.let { items ->
                configToolbarItems(context, this, items)
            }
        }
    }

    private fun configToolbarStyle(
        context: Context,
        toolbar: Toolbar,
        navigationBar: NavigationBar
    ) {
        val designSystem = BeagleEnvironment.beagleSdk.designSystem
        val style = navigationBar.style ?: ""
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
                toolbar.addView(generateCenterTitle(context, navigationBar, textAppearance, toolbar))
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
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
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
        context: Context,
        toolbar: Toolbar,
        items: List<NavigationBarItem>
    ) {
        val designSystem = BeagleEnvironment.beagleSdk.designSystem
        for (i in items.indices) {
            toolbar.menu.add(Menu.NONE, items[i].id?.toAndroidId() ?: i, Menu.NONE, items[i].text).apply {
                setOnMenuItemClickListener {
                    actionExecutor.doAction(context, items[i].action)
                    return@setOnMenuItemClickListener true
                }

                setContentDescription(items, i)

                if (items[i].image == null) {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                } else {
                    configMenuItem(designSystem, items, i, context)
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

    private fun MenuItem.configMenuItem(
        design: DesignSystem?,
        items: List<NavigationBarItem>,
        i: Int,
        context: Context
    ) {
        design?.let { designSystem ->
            items[i].image?.let { image ->
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                icon = designSystem.image(image)?.let {
                    ResourcesCompat.getDrawable(
                        context.resources,
                        it,
                        null
                    )
                }
            }
        }
    }

}