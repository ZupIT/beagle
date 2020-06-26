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

import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.context.valueOfNullable
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.utils.toAndroidColor
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.widget.core.TextAlignment

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
        valueOf(text),
        styleId,
        valueOfNullable(textColor),
        valueOfNullable(alignment)
    )

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val textView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            viewFactory.makeTextView(rootView.getContext(), getStyleId(this.styleId))
            else viewFactory.makeTextView(rootView.getContext())

        textView.setTextWidget(this, rootView)
        return textView
    }

    private fun TextView.setTextWidget(text: Text, rootView: RootView) {
        observeBindChanges(rootView, text.text) {
            this.text = it
        }

        text.styleId?.let {
            setStyle(it)
        }

        text.textColor?.let {
            observeBindChanges(rootView, it) { value ->
                this.setTextColor(value)
            }
        }

        text.alignment?.let {
            observeBindChanges(rootView, it) { value ->
                this.setAlignment(value)
            }
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

    private fun getStyleId(styleName: String?) : Int =
            BeagleEnvironment.beagleSdk.designSystem?.textStyle(styleName ?:"")?:0

    private fun TextView.setTextColor(color: String?) {
        color?.let {
            this.setTextColor(color.toAndroidColor())
        }
    }
}