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

package br.com.zup.beagle.mockdata

import android.content.Context
import android.view.View
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.WidgetView
import io.mockk.mockk

class CustomWidget : WidgetView(), BindingAdapter {
    override fun buildView(context: Context): View {
        return mockk()
    }

    override fun onBind(widget: Widget, view: View) {
        return mockk()
    }

    override fun getBindAttributes(): List<Bind<*>> {
        return mockk()
    }
}