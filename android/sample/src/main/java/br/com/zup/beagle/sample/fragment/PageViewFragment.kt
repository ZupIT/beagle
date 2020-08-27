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
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.page.PageIndicator
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.android.components.page.PageView
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.core.TextAlignment

class PageViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = PageView(
            context = ContextData(
                id = "pages",
                value = listOf(
                    "Page 1",
                    "Page 2",
                    "Page 3"
                )
            ),
            pageIndicator = PageIndicator(
                selectedColor = "#000000",
                unselectedColor = "#888888"
            ),
            children = listOf(
                Text(text = expressionOf("@{pages[0]}"), alignment = valueOf(TextAlignment.CENTER)).applyFlex(
                    Flex(
                        alignSelf = AlignSelf.CENTER,
                        grow = 1.0
                    )
                ),
                Text(text = expressionOf("@{pages[1]}"), alignment = valueOf(TextAlignment.CENTER)).applyFlex(
                    Flex(
                        alignSelf = AlignSelf.CENTER,
                        grow = 1.0
                    )
                ),
                Text(text = expressionOf("@{pages[2]}"), alignment = valueOf(TextAlignment.CENTER)).applyFlex(
                    Flex(
                        alignSelf = AlignSelf.CENTER,
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