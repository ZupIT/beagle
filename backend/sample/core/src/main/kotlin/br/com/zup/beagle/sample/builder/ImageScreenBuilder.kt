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
import br.com.zup.beagle.sample.constants.LOGO_BEAGLE
import br.com.zup.beagle.sample.constants.TITLE_SCREEN
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.layout.ScrollAxis
import br.com.zup.beagle.widget.layout.navigationBarItem
import br.com.zup.beagle.widget.layout.screen
import br.com.zup.beagle.widget.layout.scrollView
import br.com.zup.beagle.widget.ui.image

object ImageScreenBuilder : ScreenBuilder {
    override fun build() = screen {
        navigationBar {
            navigationBarItems {
                +navigationBarItem {
                    text = ""
                    image = "informationImage"
                    withAction(showNativeDialog {
                        title = "Image"
                        message = "This widget will define a image view natively using the server driven " +
                            "information received through Beagle."
                        buttonText = "OK"
                    })
                }
            }
        }
        withChild(
            scrollView {
                scrollDirection = ScrollAxis.VERTICAL
                children {
                    +createText("Image")
                    +image {
                        name = LOGO_BEAGLE
                    }
                }
            }
        )
    }

    private fun createText(text: String) = br.com.zup.beagle.widget.ui.text {
        withText(text)
        style = TITLE_SCREEN
    }
}