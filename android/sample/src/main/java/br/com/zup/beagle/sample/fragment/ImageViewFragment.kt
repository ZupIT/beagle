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
import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.Size

class ImageViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val declarative = Screen(
            child = Container(
                children = listOf(
                    Image(
                        path = ImagePath.Remote(
                            url = "https://camo.githubusercontent.com/476cecf6bc0acaabb0012031c2b9406088eb8cd8e466c16" +
                                "70324090a1d29af72/68747470733a2f2f67626c6f627363646e2e676974626f6f6b2e636f6d2f737061" +
                                "6365732532462d4d2d5179376a5a6255707a475250354762435a2532466176617461722e706e67",
                            placeholder = ImagePath.Local("imageBeagle")
                        ),
                        mode = ImageContentMode.FIT_XY
                    ).applyStyle(
                        Style(
                            size = Size(height = 100.unitReal())
                        )
                    )
                )
            ).applyFlex(
                Flex(
                    flexDirection = FlexDirection.COLUMN
                )
            )
        )

        return declarative.toView(this)
    }

    companion object {
        fun newInstance(): ImageViewFragment {
            return ImageViewFragment()
        }
    }
}
