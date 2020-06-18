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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.ScrollView
import br.com.zup.beagle.android.components.NetworkImage
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Screen

class ScrollViewFragment : Fragment() {

    private val style = Style(margin = EdgeValue(all = UnitValue(50.0, UnitType.REAL)), flex = Flex(shrink = 0.0))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            child = Container(
                children = listOf(
                    Text("1 Text\n2 Text\n3 Text\n4 Text\n5 Text\n6 Text\n7 Text\n8 Text\n9 Text\n10 Text.")
                        .applyFlex(Flex(shrink = 0.0)),
                    buildScrollView()
                )
            ).applyFlex(Flex(grow = 1.0))
        )

        return context?.let { declarative.toView(this) }
    }

    private fun buildScrollView() = ScrollView(
        children = listOf(
            NetworkImage(
                path = "https://www.petlove.com.br/images/breeds/193436/profile/original/beagle-p.jpg?1532538271"
            ).applyStyle(Style(
                cornerRadius = CornerRadius(30.0),
                flex = Flex(shrink = 0.0),
                size = Size(width = 200.unitReal(), height = 200.unitReal()))
            ),
            Text("Text 1").applyStyle(style),
            Text("Text 2").applyStyle(style),
            Text("Text 3").applyStyle(style),
            Text("Text 4").applyStyle(style),
            Text("Text 5").applyStyle(style),
            Text("Text 6").applyStyle(style),
            Text("Text 7").applyStyle(style),
            Text("Text 8").applyStyle(style),
            Text("Text 9").applyStyle(style),
            Text("Text 10").applyStyle(style)
        )
    )

    companion object {

        fun newInstance(): ScrollViewFragment {
            return ScrollViewFragment()
        }
    }
}