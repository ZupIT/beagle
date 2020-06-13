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

package br.com.zup.beagle.android.processor

import android.content.Context
import android.view.View
import br.com.zup.beagle.android.widget.core.WidgetView
import br.com.zup.beagle.android.widget.form.InputWidget
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.Widget
import io.mockk.mockk

internal const val WIDGET_INSTANCE_PROPERTY = "widgetInstance"
internal const val VIEW_PROPERTY = "view"

@RegisterWidget
data class FieldOnlyWidget(val a: Boolean = true,
                           val b: Long = 123L,
                           val c: String = "Hello") : WidgetView() {

    override fun buildView(context: Context): View {
        return mockk()
    }

    override fun onBind(widget: Widget, view: View) {
        return mockk()
    }
}

@RegisterWidget
data class CustomInputWidget(val text: String = "") : InputWidget() {
    override fun onErrorMessage(message: String) {
        return mockk()
    }

    override fun getValue(): Any {
        return mockk()
    }

    override fun buildView(context: Context): View {
        return mockk()
    }

    override fun onBind(widget: Widget, view: View) {
        return mockk()
    }
}