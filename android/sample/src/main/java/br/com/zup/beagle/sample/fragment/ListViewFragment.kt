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
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ListDirection
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.Text

class ListViewFragment : Fragment() {

    private val flex = Flex(size = Size(width = 100.unitReal(), height = 100.unitReal()))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            navigationBar = NavigationBar(title = "List"),
            child = Container(children = listOf(buildListView()))
        )
        return context?.let { declarative.toView(this) }
    }

    private fun buildListView() = ListView(
        direction = ListDirection.HORIZONTAL,
        rows = listOf(
            Text("0000"),
            Text("0001").applyFlex(flex),
            Text("0002"),
            Text("0003"),
            Text("0004"),
            LazyComponent(
                path = "http://www.mocky.io/v2/5e4e91c02f00001f2016a8f2",
                initialState = Text("Loading LazyComponent...")
            ),
            Text("0005"),
            Text("0006"),
            Text("0007"),
            Text("0008"),
            Text("0009"),
            Text("0010"),
            Text("0011"),
            Text("0012"),
            Text("0013"),
            Image(name = "beagle"),
            Text("0014"),
            Text("0015"),
            Text("0016"),
            NetworkImage(
                path = "https://www.petlove.com.br/images/breeds/193436/profile/original/beagle-p.jpg?1532538271"
            ),
            Text("0017"),
            Text("0018"),
            Text("0019"),
            Text("0020"),
            Container(children = listOf(Text("Text1"), Text("Text2")))
        )
    )

    companion object {

        fun newInstance(): ListViewFragment {
            return ListViewFragment()
        }
    }
}