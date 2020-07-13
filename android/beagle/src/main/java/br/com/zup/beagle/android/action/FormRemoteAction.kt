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
import br.com.zup.beagle.android.components.form.core.Constants
import br.com.zup.beagle.android.components.form.core.FormResult
import br.com.zup.beagle.android.components.form.core.FormSubmitter
import br.com.zup.beagle.android.widget.RootView

@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
enum class FormMethodType {
    GET,
    POST,
    PUT,
    DELETE
}

@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
internal typealias ResultListener = (result: FormResult) -> Unit

@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
data class FormRemoteAction(
    val path: String,
    val method: FormMethodType
) : Action {

    @Transient
    internal lateinit var formsValue: Map<String, String>

    @Transient
    internal lateinit var resultListener: ResultListener

    @Transient
    private val formSubmitter: FormSubmitter = FormSubmitter()

    override fun execute(rootView: RootView, origin: View) {
        formSubmitter.submitForm(this, formsValue) {
            resultListener(it)
        }
    }


}