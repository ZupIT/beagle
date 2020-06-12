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

package br.com.zup.beagle.android.components

import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.android.data.PreFetchHelper
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.ui.RootView
import br.com.zup.beagle.android.widget.ui.WidgetView

data class Button(private val text: String,
                  val styleId: String? = null,
                  private val action: Action? = null,
                  private val clickAnalyticsEvent: ClickEvent? = null)
    : WidgetView() {

    @Transient
    private val viewFactory = ViewFactory()

    @Transient
    private val actionExecutor: ActionExecutor = ActionExecutor()

    @Transient
    private val preFetchHelper: PreFetchHelper = PreFetchHelper()

    override fun buildView(rootView: RootView): View {
        action?.let {
            preFetchHelper.handlePreFetch(rootView, it)
        }
        val button = viewFactory.makeButton(rootView.getContext())

        button.setOnClickListener {
            actionExecutor.doAction(rootView.getContext(), action)
            clickAnalyticsEvent?.let {
                BeagleEnvironment.beagleSdk.analytics?.sendClickEvent(it)
            }
        }
        button.setData(text, styleId)
        return button
    }

    private fun Button.setData(text: String, styleId: String?) {
        val typedArray = styleManagerFactory.getButtonTypedArray(context, styleId)
        typedArray?.let {
            background = it.getDrawable(R.styleable.BeagleButtonStyle_background)
            isAllCaps = it.getBoolean(R.styleable.BeagleButtonStyle_textAllCaps, true)
            it.recycle()
        }
        styleManagerFactory.getButtonStyle(styleId)?.let { buttonStyle ->
            TextViewCompat.setTextAppearance(this, buttonStyle)
        }
        this.text = text
    }

}