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
import br.com.zup.beagle.android.logger.BeagleLoggerDefault
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

enum class Mode {
    append, prepend, replace
}

data class AddChildrenAction(
    var componentId: String,
    var value: List<ServerDrivenComponent>,
    var mode: Mode? = Mode.append
) : Action {
    override fun execute(rootView: RootView, origin: View) {
        try {
            val view = (rootView.getContext() as AppCompatActivity).findViewById<ViewGroup>(componentId.toAndroidId())
            addValueToView(view, rootView)
        } catch (exception: Exception) {
            BeagleLoggerDefault().error("This view cannot receive children")
        }
    }

    private fun addValueToView(view: ViewGroup, rootView: RootView) {
        when (mode) {
            Mode.append -> {
                appendValue(view, rootView)
            }
            Mode.prepend -> {
                prependValue(view, rootView)
            }
            Mode.replace -> {
                replaceValue(view, rootView)
                //fazer testes de todos modos
                //Fazer Unit test

            }
        }
    }

    private fun appendValue(view: ViewGroup, rootView: RootView) {
        value.forEach {
            view.addView(it.toView(rootView))
        }
    }

    private fun prependValue(view: ViewGroup, rootView: RootView) {
        value.forEach {
            view.addView(it.toView(rootView), 0)
        }
    }

    private fun replaceValue(view: ViewGroup, rootView: RootView){
        view.removeAllViews()
        value.forEach {
            view.addView(it.toView(rootView))
        }
    }

}