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

package br.com.zup.beagle.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.LazyComponent
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.components.layout.ScrollView
import br.com.zup.beagle.ext.applyStyle

class LazyComponentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            navigationBar = NavigationBar(title = "List"),
            child = Container(children = listOf(buildScrollView())).applyFlex(Flex(grow = 1.0))
        )
        return declarative.toView(this)
    }

    private fun buildScrollView() = ScrollView(
        children = listOf(
            Image(
                ImagePath.Remote(
                    "https://www.petlove.com.br/images/breeds/193436/profile/original/beagle-p.jpg?1532538271"
                )
            ).applyStyle(Style(cornerRadius = CornerRadius(30.0))),
            LazyComponent(
                path = "http://www.mocky.io/v2/5e4e91c02f00001f2016a8f2",
                initialState = Text("Loading LazyComponent...")
            )
        )
    )

    companion object {
        fun newInstance(): LazyComponentFragment {
            return LazyComponentFragment()
        }
    }
}
