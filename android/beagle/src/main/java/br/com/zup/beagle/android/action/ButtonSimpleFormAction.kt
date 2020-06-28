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

import android.view.ViewGroup
import androidx.core.view.children
import br.com.zup.beagle.android.components.form.SimpleForm
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.widget.RootView

class ButtonSimpleFormAction : Action {

    override fun execute(rootView: RootView) {
        if (rootView is FragmentRootView) {
             getSimpleForm(rootView.fragment.view as ViewGroup)?.submit(rootView)
        }
    }

    private fun getSimpleForm(view : ViewGroup?) : SimpleForm? {
        view?.children!!.forEach { item ->
            return if (item.tag is SimpleForm) {
                item.tag as SimpleForm
            } else {
                getSimpleForm(item as ViewGroup)
            }
        }
        
        return null
    }
}