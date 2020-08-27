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
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue

class ImageViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            child = Container(
                children = listOf(
                    Image(
                        path = ImagePath.Remote(
                            "https://cdn-images-1.medium.com/max/1200/1*kjiNJPB3Y-ZVmjxco_bORA.png",
                            placeholder = ImagePath.Local("imageBeagle")
                        )
                    ),
                    Text(text = "Opa!!!")
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