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
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import br.com.zup.beagle.R
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.TabView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.utils.applyViewBackgroundAndCorner
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.core.StyleComponent

class StyleManager(
    private val designSystem: DesignSystem? = BeagleEnvironment.beagleSdk.designSystem,
    private val typedValue: TypedValue = TypedValue()
) {

    fun applyStyleComponent(
        context: Context,
        component: StyleComponent,
        view: View
    ) {
        if (view.background == null) {
            view.applyViewBackgroundAndCorner(Color.TRANSPARENT, component)
        } else when (component) {
            is Text -> {
                applyStyleId(context, component.styleId?:"", view, component)
            }
            is Button -> {
                applyStyleId(context, component.styleId?:"", view, component)
            }
            is TabView -> {
                applyStyleId(context, component.styleId?:"", view, component)
            }
            else -> {
                val colorInt = fetchDrawableColor(background = view.background)
                view.applyViewBackgroundAndCorner(colorInt, component)
            }
        }
    }

    private fun applyStyleId(
        context: Context,
        buttonStyle: String,
        view: View,
        component: StyleComponent
    ) {
        val colorInt = fetchDrawableColor(getTypedArray(
            context,
            designSystem?.buttonStyle(buttonStyle),
            R.styleable.BackgroundStyle)
        )
        view.applyViewBackgroundAndCorner(colorInt, component)
    }

    fun getTypedValueByResId(resId: Int, context: Context): TypedValue {
        context.theme.resolveAttribute(resId, typedValue, true)
        return typedValue
    }

    fun getButtonStyle(styleId: String?): Int? {
        return designSystem?.buttonStyle(styleId ?: "")
    }

    fun getInputTextStyle(styleId: String?): Int? {
        return designSystem?.inputTextStyle(styleId ?: "")
    }

    fun getButtonTypedArray(context: Context, styleId: String?): TypedArray? {
        val buttonStyle = getButtonStyle(styleId)
        return getTypedArray(
            context,
            buttonStyle,
            R.styleable.BeagleButtonStyle
        )
    }

    fun getInputTextTypedArray(context: Context, styleId: String?): TypedArray? {
        val inputTextStyle = getInputTextStyle(styleId)
        return getTypedArray(
            context,
            inputTextStyle,
            R.styleable.BeagleInputTextStyle
        )
    }

    fun getTabBarTypedArray(context: Context, styleId: String?): TypedArray? {
        val tabStyle = designSystem?.tabViewStyle(styleId ?: "")
        return getTypedArray(
            context,
            tabStyle,
            R.styleable.BeagleTabBarStyle
        )
    }

    private fun getTypedArray(
        context: Context,
        style: Int?,
        attrStyles: IntArray
    ): TypedArray? {
        var typedArray: TypedArray? = null
        if (designSystem != null && style != null) {
            typedArray = context.obtainStyledAttributes(style, attrStyles)
        }

        return typedArray
    }

    private fun fetchDrawableColor(
        typedArray: TypedArray? = null,
        background: Drawable? = null
    ): Int? {
        val drawable =
            typedArray?.getDrawable(R.styleable.BackgroundStyle_background) ?: background
        if (drawable is ColorDrawable) {
            return drawable.color
        }
        typedArray?.recycle()
        return null
    }
}
