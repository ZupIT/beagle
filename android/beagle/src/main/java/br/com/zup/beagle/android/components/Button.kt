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
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.data.PreFetchHelper
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.get
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView

data class Button(
    val text: Bind<String>,
    val styleId: Bind<String>? = null,
    val onPress: List<Action>? = null,
    val clickAnalyticsEvent: ClickEvent? = null
) : WidgetView() {
    constructor(
        text: String,
        styleId: String? = null,
        onPress: List<Action>? = null,
        clickAnalyticsEvent: ClickEvent? = null
    ) : this(
        Bind.valueOf(text),
        Bind.valueOfNullable(styleId),
        onPress,
        clickAnalyticsEvent
    )

    @Transient
    private val viewFactory = ViewFactory()

    @Transient
    private val preFetchHelper: PreFetchHelper = PreFetchHelper()

    override fun buildView(rootView: RootView): View {
        onPress?.let {
            preFetchHelper.handlePreFetch(rootView, it)
        }
        val button = viewFactory.makeButton(rootView.getContext())

        button.setOnClickListener {
            onPress?.let {
                this@Button.handleEvent(rootView, it, "onPress")
            }
            clickAnalyticsEvent?.let {
                BeagleEnvironment.beagleSdk.analytics?.trackEventOnClick(it)
            }
        }
        button.setData(text, styleId, rootView)
        return button
    }

    private fun Button.setData(text: Bind<String>, styleId: Bind<String>?, rootView: RootView) {
        styleId?.let { bind ->
            bind.get(rootView = rootView) {
                val typedArray = styleManagerFactory.getButtonTypedArray(context, it)
                typedArray?.let { typeArray ->
                    background = typeArray.getDrawable(R.styleable.BeagleButtonStyle_background)
                    isAllCaps = typeArray.getBoolean(R.styleable.BeagleButtonStyle_textAllCaps, true)
                    typeArray.recycle()
                }

                styleManagerFactory.getButtonStyle(it)?.let { buttonStyle ->
                    TextViewCompat.setTextAppearance(this, buttonStyle)
                }
            }
        }

        text.get(rootView = rootView) {
            this.text = it
        }
    }

}