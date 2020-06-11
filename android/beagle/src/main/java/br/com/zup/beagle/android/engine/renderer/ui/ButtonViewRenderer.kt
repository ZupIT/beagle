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

import android.view.View
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.android.data.PreFetchHelper
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.UIViewRenderer
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.custom.BeagleButtonView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.ui.Button

internal class ButtonViewRenderer(
    override val component: Button,
    private val viewFactory: ViewFactory = ViewFactory(),
    private val actionExecutor: ActionExecutor = ActionExecutor(),
    private val preFetchHelper: PreFetchHelper = PreFetchHelper()
) : UIViewRenderer<Button>() {

    override fun buildView(rootView: RootView): View {
        component.onPress?.let {
            preFetchHelper.handlePreFetch(rootView, it)
        }

        return viewFactory.makeButton(rootView.getContext()).apply {
            setOnClickListener {
                actionExecutor.doAction(rootView, component.onPress)
                component.clickAnalyticsEvent?.let {
                    BeagleEnvironment.beagleSdk.analytics?.sendClickEvent(it)
                }
            }
            setOnLongClickListener {
                actionExecutor.doAction(rootView, component.onLongPress)
                component.clickAnalyticsEvent?.let {
                    BeagleEnvironment.beagleSdk.analytics?.sendClickEvent(it)
                }

                return@setOnLongClickListener true
            }
            setData(component)
        }
    }
}

internal fun BeagleButtonView.setData(component: Button) {
    val typedArray = styleManagerFactory.getButtonTypedArray(context, component.styleId)
    typedArray?.let {
        background = it.getDrawable(R.styleable.BeagleButtonStyle_background)
        isAllCaps = it.getBoolean(R.styleable.BeagleButtonStyle_textAllCaps, true)
        it.recycle()
    }
    styleManagerFactory.getButtonStyle(component.styleId)?.let { buttonStyle ->
        TextViewCompat.setTextAppearance(this, buttonStyle)
    }
    text = component.text
}
