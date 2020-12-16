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

package br.com.zup.beagle.android.mockdata

import android.view.View
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import io.mockk.mockk

interface PersonInterface

data class Person(val names: ArrayList<String>): PersonInterface

class CustomWidget(
    val arrayList: ArrayList<Person>?,
    val pair: Pair<Person, String>?,
    val charSequence: CharSequence?,
    val personInterface: PersonInterface
) : WidgetView() {
    override fun buildView(rootView: RootView): View {
        return mockk()
    }
}