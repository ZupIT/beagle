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

package br.com.zup.beagle.android.engine.renderer.ui

import android.graphics.Color
import android.view.View
import br.com.zup.beagle.android.engine.renderer.UIViewRenderer
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.Environment
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.ui.UndefinedWidget

internal class UndefinedViewRenderer(
    override val component: UndefinedWidget,
    private val viewFactory: ViewFactory = ViewFactory()
) : UIViewRenderer<UndefinedWidget>() {

    override fun buildView(rootView: RootView): View {
        return if (BeagleEnvironment.beagleSdk.config.environment == Environment.DEBUG) {
            viewFactory.makeTextView(rootView.getContext()).apply {
                text = "undefined component"
                setTextColor(Color.RED)
                setBackgroundColor(Color.YELLOW)
            }
        } else {
            viewFactory.makeView(rootView.getContext())
        }
    }
}
