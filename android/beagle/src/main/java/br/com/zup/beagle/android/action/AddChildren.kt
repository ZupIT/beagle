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

package br.com.zup.beagle.android.action

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

/**
 * Defines the placement of where the children will be inserted in the list or if the contents
 * of the list will be replaced.
 *
 * @property APPEND
 * @property PREPEND
 * @property REPLACE
 */
enum class Mode {
    /**
     * Adds the view in the end of the children's list.
     */
    APPEND,

    /**
     * Adds the view on the beginning of the children's list.
     */
    PREPEND,

    /**
     * Replaces all children of the widget.
     */
    REPLACE
}

/**
 * The AddChildren class is responsible for adding - at the beginning or in the end - or changing
 * all views that inherit from  Widget  and who accept children.
 *
 * @param componentId Required. Defines the widget's id, in which you want to add the views.
 * @param value Required. Defines the list of children you want to add.
 * @param mode Defines the placement of where the children will be inserted in the list or if the contents of
 * the list will be replaced.
 */
data class AddChildren(
    var componentId: String,
    var value: List<ServerDrivenComponent>,
    var mode: Mode? = Mode.APPEND
) : Action {

    override fun execute(rootView: RootView, origin: View) {
        try {
            val view = (rootView.getContext() as AppCompatActivity).findViewById<ViewGroup>(componentId.toAndroidId())
            val viewList = convertServerDrivenListOnViewList(value, rootView)
            addValueToView(view, viewList)
        } catch (exception: Exception) {
            BeagleMessageLogs.errorWhileTryingToAddViewWithAddChildrenAction(componentId)
        }
    }

    private fun convertServerDrivenListOnViewList(list: List<ServerDrivenComponent>, rootView: RootView): List<View> {
        val result: ArrayList<View> = ArrayList()
        list.forEach {
            result.add(it.toView(rootView))
        }
        return result
    }

    private fun addValueToView(view: ViewGroup, viewList: List<View>) {
        when (mode) {
            Mode.APPEND -> {
                appendListOnViewGroupChildren(view, viewList)
            }
            Mode.PREPEND -> {
                prependValue(view, viewList)
            }
            Mode.REPLACE -> {
                replaceValue(view, viewList)
            }
        }
    }

    private fun prependValue(viewGroup: ViewGroup, viewList: List<View>) {
        viewList.forEach {
            viewGroup.addView(it, 0)
        }
        viewGroup.invalidate()
    }

    private fun replaceValue(viewGroup: ViewGroup, viewList: List<View>) {
        viewGroup.removeAllViews()
        appendListOnViewGroupChildren(viewGroup, viewList)
    }

    private fun appendListOnViewGroupChildren(viewGroup: ViewGroup, list: List<View>) {
        list.forEach {
            viewGroup.addView(it)
        }
    }

}