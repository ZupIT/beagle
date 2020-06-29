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
import android.widget.Button
import androidx.core.view.children
import br.com.zup.beagle.android.components.form.SimpleForm
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView

/**
 * action will be a submit handler for a SimpleForm.
 *
 * @param contextID define the id from Context that be set in simpleForm
 *                  todo: the contextID needs to be the same of simpleForm parent
 *
 */
class ButtonSubmitAction constructor(
    private val contextID: String
): Action {

    override fun execute(rootView: RootView) {
        if (rootView is FragmentRootView) {
             getSimpleForm(rootView.fragment.view as ViewGroup)?.submit(rootView)
        }
    }

    private fun getSimpleForm(view : ViewGroup?) : SimpleForm? {
        view?.children!!.forEach { item ->
            if (item is BeagleFlexView) {
                (item as ViewGroup).children.forEach { subItem ->
                     if (subItem is Button) {
                        if (item.tag is SimpleForm && contextID == (item.tag as SimpleForm).context.id) {
                            return item.tag as SimpleForm
                        }
                    } else {
                        return getSimpleForm(item)
                    }
                }
            }
        }

        return null
    }
}