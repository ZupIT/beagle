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
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.get
import br.com.zup.beagle.android.utils.toAndroidColor
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.widget.core.TextAlignment

data class Text(
    val text: Bind<String>,
    val styleId: Bind<String>? = null,
    val textColor: Bind<String>? = null,
    val alignment: Bind<TextAlignment>? = null
) : WidgetView() {
    constructor(
        text: String,
        styleId: String? = null,
        textColor: String? = null,
        alignment: TextAlignment? = null
    ) : this(
        Bind.valueOf(text),
        Bind.valueOfNullable(styleId),
        Bind.valueOfNullable(textColor),
        Bind.valueOfNullable(alignment)
    )

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val textView = viewFactory.makeTextView(rootView.getContext())
        textView.setTextWidget(this, rootView)
        return textView
    }

    private fun TextView.setTextWidget(text: Text, rootView: RootView) {
        text.text.get(rootView) {
            this.text = it
        }

        text.styleId?.get(rootView) {
            this.setStyle(it)
        } ?: run {
            this.setStyle("")
        }

        text.textColor?.get(rootView) {
            this.setTextColor(it)
        }

        text.alignment?.get(rootView) {
            this.setAlignment(it)
        }
    }

    private fun TextView.setAlignment(alignment: TextAlignment?) {
        when (alignment) {
            TextAlignment.CENTER -> this.gravity = Gravity.CENTER
            TextAlignment.RIGHT -> this.gravity = Gravity.END
            else -> this.gravity = Gravity.START
        }
    }

    private fun TextView.setStyle(styleId: String) {
        val designSystem = BeagleEnvironment.beagleSdk.designSystem
        designSystem?.textStyle(styleId)?.let {
            TextViewCompat.setTextAppearance(this, it)
        }
    }

    private fun TextView.setTextColor(color: String?) {
        color?.let {
            this.setTextColor(color.toAndroidColor())
        }
    }
}