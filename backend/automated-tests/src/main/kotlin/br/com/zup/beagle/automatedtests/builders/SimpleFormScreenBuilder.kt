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

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.action.SubmitForm
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.form.SimpleForm
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.TextInput

object SimpleFormScreenBuilder: ScreenBuilder {
    override fun build(): Screen {
        return Screen(
            navigationBar = NavigationBar(
                title = "Beagle SimpleForm",
                showBackButton = true,
                navigationBarItems = listOf(
                    NavigationBarItem(
                        text = "",
                        image = ImagePath.Local.justMobile("informationImage"),
                        action = Alert(
                            title = "SimpleForm",
                            message = "",
                            labelOk = "OK"
                        )
                    )
                )
            ),
            child = ScrollView(
                scrollDirection = ScrollAxis.VERTICAL,
                children = listOf(
                    SimpleForm(
                        onSubmit = listOf(
                            Alert(
                                title = "Alert",
                                message = "The email is @{myContext.value}"
                            )
                        ),
                        children = listOf(
                            TextInput(
                                value = "",
                                onBlur = listOf(
                                    SetContext(
                                        contextId = "myContext",
                                        value = "@{onBlur.value}"
                                    )
                                ),
                                placeholder = "@{myContext.value}"
                            ),
                            Button(
                                text = "submit form",
                                onPress = listOf(
                                    SubmitForm()
                                ))
                        ),
                        context = ContextData(
                            id = "myContext",
                            value = "Email"
                        )

                    )
                )
            )
        )
    }
}