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

package br.com.zup.beagle.android.action

import android.view.View
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOrValueOf
import br.com.zup.beagle.android.context.expressionOrValueOfNullable
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.BeagleJson
import br.com.zup.beagle.newanalytics.ActionAnalyticsConfig

/**
 * This action will show dialogues natively, such as an error alert indicating alternative flows, business system
 * errors and others.
 *
 * @param title defines the title on the Dialog.
 * @param message defines the Dialog message.
 * @param labelOk define text of button positive in dialog.
 * @param labelCancel define text of button negative in dialog.
 * @param onPressOk define action of button positive in dialog.
 * @param onPressCancel define action of button negative in dialog.
 *
 */
@BeagleJson(name = "confirm")
data class Confirm(
    val title: Bind<String>? = null,
    val message: Bind<String>,
    val onPressOk: Action? = null,
    val onPressCancel: Action? = null,
    val labelOk: String? = null,
    val labelCancel: String? = null,
    override var analytics: ActionAnalyticsConfig? = null,
) : AnalyticsAction {

    constructor(
        title: String? = null,
        message: String,
        onPressOk: Action? = null,
        onPressCancel: Action? = null,
        labelOk: String? = null,
        labelCancel: String? = null,
        analytics: ActionAnalyticsConfig? = null,
    ) : this(
        title = expressionOrValueOfNullable(title),
        message = expressionOrValueOf(message),
        onPressOk = onPressOk,
        onPressCancel = onPressCancel,
        labelOk = labelOk,
        labelCancel = labelCancel,
        analytics = analytics
    )

    override fun execute(rootView: RootView, origin: View) {
        ViewFactory.makeAlertDialogBuilder(rootView.getContext())
            .setTitle(title?.let { evaluateExpression(rootView, origin, it) } ?: "")
            .setMessage(evaluateExpression(rootView, origin, message))
            .setPositiveButton(labelOk
                ?: rootView.getContext().getString(android.R.string.ok)) { dialogBox, _ ->
                dialogBox.dismiss()
                onPressOk?.let {
                    handleEvent(
                        rootView,
                        origin,
                        it,
                        analyticsValue = "onPressOk"
                    )
                }
            }
            .setNegativeButton(labelCancel
                ?: rootView.getContext().getString(android.R.string.cancel)) { dialogBox, _ ->
                dialogBox.dismiss()
                onPressCancel?.let {
                    handleEvent(
                        rootView,
                        origin,
                        it,
                        analyticsValue = "onPressCancel"
                    )
                }
            }
            .show()
    }
}
