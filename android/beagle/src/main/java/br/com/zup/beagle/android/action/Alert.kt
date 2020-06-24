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

import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView

data class Alert(
    val title: Bind<String>?,
    val message: Bind<String>,
    val onPressOk: Action? = null,
    val labelOk: String? = null
) : Action {
    constructor(
        title: String?,
        message: String,
        onPressOk: Action? = null,
        labelOk: String? = null
    ) : this(
        title = Bind.valueOfNullable(title),
        message = Bind.valueOf(message),
        onPressOk = onPressOk,
        labelOk = labelOk
    )

    @Transient
    internal var viewFactory: ViewFactory = ViewFactory()

    override fun execute(rootView: RootView) {
        viewFactory.makeAlertDialogBuilder(rootView.getContext())
            .setTitle(title?.let { evaluateExpression(rootView, it) } ?: "")
            .setMessage(evaluateExpression(rootView, message))
            .setPositiveButton(labelOk ?: rootView.getContext().getString(android.R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                onPressOk?.let {
                    handleEvent(rootView, it, "onPressOk")
                }
            }
            .show()
    }
}
