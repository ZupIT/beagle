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

package br.com.zup.beagle.android.view.custom

import android.annotation.SuppressLint
import android.view.View
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style

/**
 *  The Beagle Flex View is a view group that support style options
 *
 * @param rootView  holder the reference of current context.
 * @param style class will enable a few visual options to be changed.
 *
 */
@SuppressLint("ViewConstructor")
class BeagleFlexView private constructor(
    rootView: RootView,
    style: Style = Style(),
    styleId: Int = 0,
) : InternalBeagleFlexView(
    rootView = rootView,
    style = style,
    styleId = styleId,
) {

    companion object {

        operator fun invoke(
            rootView: RootView,
            style: Style = Style(),
            styleId: Int = 0,
        ) = BeagleFlexView(
            rootView = rootView,
            style = style,
            styleId = styleId,
        )
    }

    /**
     * Adds a child view with the specified style.
     * @property child view will be added
     * @property style style will be applied in this child.
     */
    fun addView(
        child: View,
        style: Style,
    ) {
        addViewWithStyle(child, style)
    }

    /**
     * Adds a list of components in view.
     * @property components views will be added
     * @property addLayoutChangeListener force recalculate layout when view change, prefer use true always
     */
    fun addView(
        components: List<ServerDrivenComponent>? = null,
        addLayoutChangeListener: Boolean = true,
    ) {
        components?.forEach {
            addView(it, addLayoutChangeListener)
        }
    }

    /**
     * Adds a component in view.
     * @property component view will be added
     * @property addLayoutChangeListener force recalculate layout when view change, prefer use true always
     */
    fun addView(
        component: ServerDrivenComponent,
        addLayoutChangeListener: Boolean = true,
    ) {
        addServerDrivenComponent(component, addLayoutChangeListener)
    }

    fun addListenerOnViewDetachedFromWindow(listener: (() -> Unit)) {
        listenerOnViewDetachedFromWindow = listener
    }
}