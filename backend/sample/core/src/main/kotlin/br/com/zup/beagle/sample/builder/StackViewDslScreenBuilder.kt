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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.action.showNativeDialog
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.SCREEN_TEXT_STYLE
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.ui.text

object StackViewDslScreenBuilder : ScreenBuilder {

    override fun build() = screen {
        navigationBar = navigationBar {
            title = "Beagle StackView"
            showBackButton = true
            navigationBarItems {
                navigationBarItem {
                    text = ""
                    image = "informationImage"
                    action = showNativeDialog {
                        title = "StackView"
                        message = "Implements widgets on top of each other."
                        buttonText = "OK"
                    }
                }
            }
        }
        child = stack {
            children {
                text {
                    text = "Text 1"
                    style = SCREEN_TEXT_STYLE
                }.flex {
                    margin(EdgeValue(
                        top = 5.unitReal()
                    ))
                }

                text {
                    text = "Text 2"
                    style = SCREEN_TEXT_STYLE
                }
                text {
                    text = "Text 3"
                    style = SCREEN_TEXT_STYLE
                }
            }
        }
    }
}