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

enum class Mode {
    APPEND, PREPEND, REPLACE
}

data class AddChildrenAction(
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