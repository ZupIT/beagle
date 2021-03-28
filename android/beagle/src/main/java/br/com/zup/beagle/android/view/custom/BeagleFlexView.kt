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

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
class BeagleFlexView(
    rootView: RootView,
    style: Style = Style(),
    styleId: Int = 0,
) : FrameLayout(rootView.getContext()) {

    private val internalView: InternalBeagleFlexView by lazy {
        InternalBeagleFlexView(
            rootView = rootView,
            style = style,
            styleId = styleId,
        )
    }

    init {
        this.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        super.addView(internalView, -1, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    override fun addView(child: View?) {
        internalView.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        internalView.addView(child, index)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        throw RuntimeException(ADD_VIEW_EXCEPTION_MESSAGE)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        throw RuntimeException(ADD_VIEW_EXCEPTION_MESSAGE)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        throw RuntimeException(ADD_VIEW_EXCEPTION_MESSAGE)
    }

    override fun removeView(view: View?) {
        internalView.removeView(view)
    }

    override fun removeViewAt(index: Int) {
        internalView.removeViewAt(index)
    }

    override fun removeViewInLayout(view: View?) {
        internalView.removeViewInLayout(view)
    }

    override fun removeViews(start: Int, count: Int) {
        internalView.removeViews(start, count)
    }

    override fun removeViewsInLayout(start: Int, count: Int) {
        internalView.removeViewsInLayout(start, count)
    }

    override fun removeAllViews() {
        internalView.removeAllViews()
    }

    override fun removeAllViewsInLayout() {
        internalView.removeAllViewsInLayout()
    }

    /**
     * Adds a child view with the specified style.
     * @property child view will be added
     * @property style style will be applied in this child.
     */
    fun addView(
        child: View,
        style: Style = Style(),
    ) {
        internalView.addView(child, style)
    }

    /**
     * Adds a list of components in view.
     * @property components views will be added
     * @property addLayoutChangeListener force recalculate layout when view change, prefer use true always
     */
    fun addView(
        components: List<ServerDrivenComponent>,
        addLayoutChangeListener: Boolean = true,
    ) {
        components.forEach {
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
        internalView.addServerDrivenComponent(component, addLayoutChangeListener)
    }

    internal fun addListenerOnViewDetachedFromWindow(listener: (() -> Unit)) {
        internalView.listenerOnViewDetachedFromWindow = listener
    }

    fun setWidthAutoAndDirtyAllViews() {
        internalView.setWidthAutoAndDirtyAllViews()
    }

    fun setWidthAndHeightAutoAndDirtyAllViews() {
        internalView.setWidthAndHeightAutoAndDirtyAllViews()
    }

    fun setHeightAutoAndDirtyAllViews() {
        internalView.setHeightAutoAndDirtyAllViews()
    }

    companion object {
        private const val ADD_VIEW_EXCEPTION_MESSAGE = "You should call addView(view) or addView(view, style)"
    }
}