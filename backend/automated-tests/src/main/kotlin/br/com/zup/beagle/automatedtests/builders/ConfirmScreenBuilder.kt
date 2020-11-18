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
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object ConfirmScreenBuilder {

    data class ConfirmTest(val title: String, val message: String)

    fun build() = Screen(
        child = Container(
            context = ContextData(
                id = "confirmContext",
                value = ConfirmTest(title = "ConfirmTitleViaExpression", message = "ConfirmMessageViaExpression")
            ),
            children =
            listOf(
                Text(text = "Confirm Screen"),
                createButton("JustAMessage", "null" ,"ConfirmMessage"),
                createButton("JustAMessageViaExpression", "null" ,
                    "@{confirmContext.message}"),
                createButton("TitleAndMessage", "ConfirmTitle" ,
                    "ConfirmMessage"),
                createButton("TitleAndMessageViaExpression", "@{confirmContext.title}" ,
                    "@{confirmContext.message}"),
                triggerActionConfirm(),
                customLabelConfirms()
            )
        )
    )

    private fun createButton(text: String, titleConfirm: String, confirmMessage: String): Button =
        Button(
            text = text,
            onPress = listOf(
                Confirm(title = titleConfirm, message = confirmMessage)
        )
    )

    private fun customLabelConfirms(): Container = Container(listOf(
        Button(
            text = "CustomConfirmLabel",
            onPress = listOf(
                Confirm(
                    title = null,
                    message = "ConfirmMessage",
                    labelOk = "CustomLabelOk",
                    labelCancel = "CustomLabelCancel"
                )
            )
        )
    ))

    private fun triggerActionConfirm(): Container = Container(listOf(
        Button(
            text = "TriggersAnActionWhenConfirmed",
            onPress = listOf(
                Confirm(
                    title = null,
                    message = "ConfirmMessage",
                    onPressOk = Alert(message = "Confirm ok clicked")
                )
            )
        ),
        Button(
            text = "TriggersAnActionWhenCanceled",
            onPress = listOf(
                Confirm(
                    title = null,
                    message = "CancelMessage",
                    onPressCancel = Alert(message = "Confirm cancel clicked")
                )
            )
        )
    ))
}