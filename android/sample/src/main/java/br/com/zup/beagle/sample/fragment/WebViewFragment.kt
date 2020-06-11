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
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.widget.ui.WebView


class WebViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            child = WebView(
                url = "https://zup.com.br"
            ).applyFlex(
                Flex(
                    size = Size(
                        width = 100.unitPercent(),
                        height = 100.unitPercent()
                    )
                )
            )
        )
        return declarative.toView(this)
    }

    companion object {
        fun newInstance() = WebViewFragment()
    }
}