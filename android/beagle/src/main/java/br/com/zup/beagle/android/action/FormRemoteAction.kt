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
import br.com.zup.beagle.analytics2.ActionAnalyticsConfig
import br.com.zup.beagle.android.components.form.core.Constants
import br.com.zup.beagle.android.components.form.core.FormResult
import br.com.zup.beagle.android.components.form.core.FormSubmitter
import br.com.zup.beagle.android.widget.RootView

/**
 *  Defines the type of operation submitted by this form. It will map these values to Http methods.
 *
 * @property GET
 * @property POST
 * @property PUT
 * @property DELETE
 *
 */
@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
enum class FormMethodType {
    /**
     * The GET method requests a representation of the specified resource.
     * Requests using GET should only retrieve data.
     *
     */
    GET,

    /**
     * The POST method is used to submit an entity to the specified resource,
     * often causing a change in state or side effects on the server.
     *
     */
    POST,

    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     *
     */
    PUT,

    /**
     * The DELETE method deletes the specified resource.
     *
     */
    DELETE
}

@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
internal typealias ResultListener = (result: FormResult) -> Unit

/**
 *  Define remote action, when you want to do some request when submit the form.
 *
 * @param path defines the URL path to the back-end service which will receive this form inputs.
 * @param method defines the type of operation submitted by this form. It will map these values to Http methods.
 *
 */
@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
data class FormRemoteAction(
    val path: String,
    val method: FormMethodType,
    override var analytics: ActionAnalyticsConfig? = null
) : ActionAnalytics(), AsyncAction by AsyncActionImpl() {

    @Transient
    internal lateinit var formsValue: Map<String, String>

    @Transient
    internal lateinit var resultListener: ResultListener

    @Transient
    private val formSubmitter: FormSubmitter = FormSubmitter()

    override fun execute(rootView: RootView, origin: View) {
        formSubmitter.submitForm(this, formsValue) {
            resultListener(it)
            onActionFinished()
        }
    }
}
