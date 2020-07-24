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

import android.graphics.drawable.GradientDrawable
import android.view.View
import br.com.zup.beagle.android.manager.StyleManager
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.StyleComponent

const val FLOAT_ZERO = 0.0f

internal var styleManagerFactory = StyleManager()

internal fun View.applyStyle(component: ServerDrivenComponent) {
    (component as? StyleComponent)?.let {
        if (it.style?.backgroundColor != null) {
            this.background = GradientDrawable()
            applyBackgroundColor(it)
            applyCornerRadius(it)
        } else {
            styleManagerFactory.applyStyleComponent(component = it, view = this)
        }
    }
}

internal fun View.applyViewBackgroundAndCorner(backgroundColor: Int?, component: StyleComponent) {
    if (backgroundColor != null) {
        this.background = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(backgroundColor, backgroundColor)
        )

        this.applyCornerRadius(component)
    }
}

internal fun View.applyBackgroundColor(styleWidget: StyleComponent) {
    styleWidget.style?.backgroundColor?.toAndroidColor()?.let { androidColor ->
        (this.background as? GradientDrawable)?.setColor(androidColor)
    }

}

internal fun View.applyCornerRadius(styleWidget: StyleComponent) {
    styleWidget.style?.cornerRadius?.let { cornerRadius ->
        if (cornerRadius.radius > FLOAT_ZERO) {
            (this.background as? GradientDrawable)?.cornerRadius = cornerRadius.radius.toFloat()
        }
    }
}