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
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOrValueOf
import br.com.zup.beagle.android.data.PreFetchHelper
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget

/**
 * Define a button natively using the server driven information received through Beagle
 *
 * @param text define the button text content.
 * @param styleId reference a native style in your local styles file to be applied on this button.
 * @param onPress attribute to define actions when this component is pressed
 * @param clickAnalyticsEvent attribute to define click event name
 *
 */
@RegisterWidget
data class Button(
    val text: Bind<String>,
    val styleId: String? = null,
    val onPress: List<Action>? = null,
    val clickAnalyticsEvent: ClickEvent? = null
) : WidgetView() {

    constructor(
        text: String,
        styleId: String? = null,
        onPress: List<Action>? = null,
        clickAnalyticsEvent: ClickEvent? = null
    ) : this(
        expressionOrValueOf(text),
        styleId,
        onPress,
        clickAnalyticsEvent
    )

    @Transient
    private val viewFactory = ViewFactory()

    @Transient
    private val preFetchHelper: PreFetchHelper = PreFetchHelper()

    @Transient
    private val styleManager: StyleManager = StyleManager()

    override fun buildView(rootView: RootView): View {
        onPress?.let {
            preFetchHelper.handlePreFetch(rootView, it)
        }

        val button = viewFactory.makeButton(rootView.getContext(), styleManager.getButtonStyle(styleId))

        button.setOnClickListener { view ->
            onPress?.let {
                this@Button.handleEvent(rootView, view, it)
            }
            clickAnalyticsEvent?.let {
                BeagleEnvironment.beagleSdk.analytics?.trackEventOnClick(it)
            }
        }

        observeBindChanges(rootView, button, text) {
            button.text = it
        }
        return button
    }
}
