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

import br.com.zup.beagle.action.alert
import br.com.zup.beagle.ext.style
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.layout.container
import br.com.zup.beagle.layout.navigationBarItem
import br.com.zup.beagle.layout.screen
import br.com.zup.beagle.layout.scrollView
import br.com.zup.beagle.ui.listView
import br.com.zup.beagle.ui.text
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Text

object ListViewDslScreenBuilder : ScreenBuilder {
    override fun build() = screen{
        navigationBar {
            title = "Beagle ListView"
            showBackButton = true
            navigationBarItems {
                +navigationBarItem{
                    text = ""
                    image{
                        mobileId = "informationImage"
                    }
                    action{
                        alert{
                            title = valueOf("ListView")
                            message{
                                valueOf("Is a Layout component that will define a list of views natively. " +
                                "These views could be any Server Driven Component.")
                            }
                            labelOk("OK")
                        }
                    }
                }
            }
        }
        child{
            scrollView {
                scrollDirection = ScrollAxis.VERTICAL
                children{
                    +getStaticListView(ListDirection.VERTICAL)
                    +getStaticListView(ListDirection.HORIZONTAL)
                    +getDynamicListView(ListDirection.VERTICAL)
                    +getDynamicListView(ListDirection.HORIZONTAL)
                }
            }
        }
    }

    private fun getStaticListView(listDirection: ListDirection) = container {
        children {
            +Text("Static $listDirection ListView").style{
                margin {
                    bottom = 10.unitReal()
                }
            }

            +listView {
                direction = listDirection
                children {
                    +(1..10).map(::createText)
                }
            }

        }
    }.style {
        margin {
            bottom = 20.unitReal()
        }
    }

    private fun getDynamicListView(listDirection: ListDirection) = container{
        children{

            +text {
                text{valueOf("Dynamic $listDirection ListView")}
            }.style {
                margin {
                    bottom = 10.unitReal()
                }
            }

            +listView {
                direction = listDirection
                children {
                    +(0 until 20).map(::createText)
                }
            }

        }
    }

    private fun createText(index: Int) = text{
        text = valueOf("Hello $index")
    }
}