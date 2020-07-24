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

package br.com.zup.beagle.android.manager

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import br.com.zup.beagle.android.base.Button
import br.com.zup.beagle.android.base.TabView
import br.com.zup.beagle.android.base.Text
import br.com.zup.beagle.android.core.design.system.R
import br.com.zup.beagle.android.designsystem.constant.DesignSystemConstant
import br.com.zup.beagle.android.setup.DesignSystem
import br.com.zup.beagle.android.utils.applyViewBackgroundAndCorner
import br.com.zup.beagle.core.StyleComponent

class StyleManager(
    private val designSystem: DesignSystem? = DesignSystemConstant.designSystem,
    private val typedValue: TypedValue = TypedValue()
) {

    fun applyStyleComponent(component: StyleComponent, view: View) {
        if (view.background == null) {
            view.applyViewBackgroundAndCorner(Color.TRANSPARENT, component)
        } else when (component) {
            //TODO this need to be refactored in the next versions
            is Text -> applyStyleId(view, component)
            is Button -> applyStyleId(view, component)
            is TabView -> applyStyleId(view, component)
            else -> {
                val colorInt = fetchDrawableColor(view.background)
                view.applyViewBackgroundAndCorner(colorInt, component)
            }
        }
    }

    private fun applyStyleId(view: View, component: StyleComponent) {
        val colorInt = fetchDrawableColor(view.background)
        view.applyViewBackgroundAndCorner(colorInt, component)
    }

    private fun fetchDrawableColor(background: Drawable? = null) = (background as? ColorDrawable)?.color

    fun getTypedValueByResId(resId: Int, context: Context): TypedValue {
        context.theme.resolveAttribute(resId, typedValue, true)
        return typedValue
    }

    fun getTabBarTypedArray(context: Context, styleId: String?): TypedArray {
        return context.obtainStyledAttributes(getTabViewStyle(styleId), R.styleable.BeagleTabBarStyle)
    }

    fun getButtonStyle(styleId: String?) = designSystem?.buttonStyle(styleId ?: "") ?: 0

    fun getTextStyle(styleId: String?) = designSystem?.textStyle(styleId ?: "") ?: 0

    fun getInputTextStyle(styleId: String?) = designSystem?.inputTextStyle(styleId ?: "")
        ?: R.style.Widget_AppCompat_EditText

    fun getTabViewStyle(styleId: String?) = designSystem?.tabViewStyle(styleId ?: "") ?: 0
}
