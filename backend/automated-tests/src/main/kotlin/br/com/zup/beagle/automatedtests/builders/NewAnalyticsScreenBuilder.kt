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
import br.com.zup.beagle.widget.action.Confirm
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button

object NewAnalyticsScreenBuilder {
    fun build() = Screen(
        child = Container(
            listOf(
                Button(
                    text = "Alert with no specific analytics configuration",
                    onPress = listOf(
                        Alert(message = "AlertMessage")
                    )
                ),
//                Button(
//                    text = "Confirm with no specific analytics configuration",
//                    onPress = listOf(
//                        Alert(message = "AlertMessage")
//                    )
//                ),
//                Button(
//                    text = "Alert with specific analytics configuration",
//                    onPress = listOf(
//                        Alert(message = "AlertMessage")
//                    )
//                ),
                Button(
                    text = "Confirm with specific analytics configuration",
                    onPress = listOf(
                        Confirm(title = "Confirm Title",
                            message = "Confirm Message",
                            onPressOk = Alert(
                                title = "Ok",
                                message = "Confirmed"),
                            onPressCancel = Alert(
                                title = "Cancel",
                                message = "Cancelled"),
                            labelOk = "Accept",
                            labelCancel = "Deny"
                        )
                    )
                )


            )
        )
    )
}