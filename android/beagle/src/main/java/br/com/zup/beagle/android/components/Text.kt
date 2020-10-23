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

import android.view.Gravity
import android.view.View
import android.widget.TextView
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOrValueOf
import br.com.zup.beagle.android.context.expressionOrValueOfNullable
import br.com.zup.beagle.android.context.valueOfNullable
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.utils.toAndroidColor
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.core.TextAlignment

/**
 * A text widget will define a text view natively using the server driven information received through Beagle.
 *
 * @param text defines the text view content. This attribute must be declared and it cannot be null.
 * @param styleId
 *              will reference a style in your local styles file to be applied on this text view.
 *              This attribute can be set as null.
 * @param textColor defines the text color natively.
 * @param alignment defines the text content alignment inside the text view.
 *
 */
@RegisterWidget
data class Text(
    val text: Bind<String>,
    val styleId: String? = null,
    val textColor: Bind<String>? = null,
    val alignment: Bind<TextAlignment>? = null
) : WidgetView() {

    constructor(
        text: String,
        styleId: String? = null,
        textColor: String? = null,
        alignment: TextAlignment? = null
    ) : this(
        expressionOrValueOf(text),
        styleId,
        expressionOrValueOfNullable(textColor),
        valueOfNullable(alignment)
    )

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val textView = viewFactory.makeTextView(rootView.getContext(), styleManagerFactory.getTextStyle(styleId))

        textView.setTextWidget(this, rootView)
        return textView
    }

    private fun TextView.setTextWidget(text: Text, rootView: RootView) {
        observeBindChanges(rootView, this, text.text) {
            it?.let { this.text = it }
        }

        text.textColor?.let {
            observeBindChanges(rootView, this, it) { value ->
                value?.let { color -> this.setTextColor(color) }
            }
        }

        text.alignment?.let {
            observeBindChanges(rootView, this, it) { value ->
                value?.let { alignment -> this.setAlignment(alignment) }
            }
        }
    }

    private fun TextView.setAlignment(alignment: TextAlignment?) {
        gravity = when (alignment) {
            TextAlignment.CENTER -> Gravity.CENTER
            TextAlignment.RIGHT -> Gravity.END
            else -> Gravity.START
        }
    }

    private fun TextView.setTextColor(color: String?) {
        color?.toAndroidColor()?.let { androidColor -> setTextColor(androidColor) }
    }
}
