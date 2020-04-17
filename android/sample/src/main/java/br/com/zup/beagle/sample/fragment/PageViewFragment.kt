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
import br.com.zup.beagle.utils.toView
import br.com.zup.beagle.widget.core.Alignment
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextAlignment

class PageViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = PageView(
            pageIndicator = PageIndicator(
                selectedColor = "#000000",
                unselectedColor = "#888888"
            ),
            pages = listOf(
                Text("Page 1", alignment = TextAlignment.CENTER).applyFlex(
                    Flex(
                        alignSelf = Alignment.CENTER,
                        grow = 1.0
                    )
                ),
                Text("Page 2", alignment = TextAlignment.CENTER).applyFlex(
                    Flex(
                        alignSelf = Alignment.CENTER,
                        grow = 1.0
                    )
                ),
                Text("Page 3", alignment = TextAlignment.CENTER).applyFlex(
                    Flex(
                        alignSelf = Alignment.CENTER,
                        grow = 1.0
                    )
                )
            )
        )

        return context?.let { declarative.toView(this) }
    }

    companion object {

        fun newInstance(): PageViewFragment {
            return PageViewFragment()
        }
    }
}