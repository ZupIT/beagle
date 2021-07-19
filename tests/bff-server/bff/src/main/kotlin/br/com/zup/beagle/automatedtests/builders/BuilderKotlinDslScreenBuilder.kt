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

package br.com.zup.beagle.automatedtests.builders

import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.layout.container
import br.com.zup.beagle.layout.screen
import br.com.zup.beagle.ui.text
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object BuilderKotlinDslScreenBuilder {
    fun build(): Screen{
        return screen {
            navigationBar{
                title = "Navigation Bar Title"
                showBackButton = true
            }
            child{
                container {
                    children{
                        +text {
                            text = valueOf("@{kotlinDSL}")
                        }
                        +Text("@{kotlinDSL}").applyFlex(Flex(alignSelf = AlignSelf.FLEX_START))
                        +Button("Beagle Button")
                    }
                    context{
                        id = "kotlinDSL"
                        value = "Hello There"
                    }
                }.applyFlex(Flex(grow = 1.0,justifyContent = JustifyContent.CENTER))
            }
        }
    }
}