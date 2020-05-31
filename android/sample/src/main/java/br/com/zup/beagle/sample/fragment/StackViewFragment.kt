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
import br.com.zup.beagle.action.showNativeDialog
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.utils.toView
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Stack
import br.com.zup.beagle.widget.layout.navigationBarItem
import br.com.zup.beagle.widget.layout.screen
import br.com.zup.beagle.widget.layout.stack
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.text

class StackViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = screen {
            withChild(br.com.zup.beagle.widget.layout.container {
                children {
                    +text {
                        text = "Text 1"
                    }.flex {
                        margin {
                            EdgeValue(
                                top = 5.unitReal()
                            )
                        }
                    }

                    +text {
                        text = "Text 2"
                    }
                    +text {
                        text = "Text 3"
                    }
                }
            })
        }

        return context?.let { declarative.toView(this) }
    }

    companion object {

        fun newInstance(): StackViewFragment {
            return StackViewFragment()
        }
    }
}
